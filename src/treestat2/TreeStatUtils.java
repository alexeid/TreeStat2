package treestat2;

import beast.base.evolution.tree.Tree;
import beast.base.evolution.tree.TreeUtils;
import beast.base.parser.NexusParser;
import beast.base.parser.NexusParserListener;
import ccd.model.AbstractCCD;
import treestat2.ccd.CCDHandler;
import treestat2.statistics.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

/**
 * @author Alexei Drummond
 */
public class TreeStatUtils {

    private static CCDHandler ccdHandler;

    /**
     * Store the value of the named statistic from the given state
     *
     * @param index the index of the tree (first tree is index 0)
     * @param key   the name of the statistic
     * @param value the value of the statistic for the given index
     */
    static void putInBigMap(int index, String key, Object value, SortedMap<Integer, SortedMap<Integer, Object>> bigMap, List<String> statisticsNames) {

        int statIndex = statisticsNames.indexOf(key);
        if (statIndex == -1) {
            throw new RuntimeException("Statistic '" + key + "' not found in:" + Arrays.toString(statisticsNames.toArray()));
        }

        SortedMap<Integer, Object> innerMap = bigMap.get(statIndex);
        if (innerMap == null) {
            innerMap = new TreeMap<>();
            bigMap.put(statIndex, innerMap);
        }
        innerMap.put(index, value);
    }

    static void writeBigMap(PrintWriter writer, int numTrees, SortedMap<Integer, SortedMap<Integer, Object>> bigMap, List<String> statisticsNames) {

        // Write out the first line of the statistics file
        writer.print("state");
        for (Integer key : bigMap.keySet()) {
            writer.print("\t" + statisticsNames.get(key));
        }
        writer.println();

        for (int state = 0; state < numTrees; state++) {

            writer.print(state);

            for (Integer key : bigMap.keySet()) {
                SortedMap<Integer, Object> innerMap = bigMap.get(key);
                Object value = innerMap.get(state);
                if (value == null) {
                    writer.print("\t0");
                } else writer.print("\t" + value);
            }
            writer.println();
        }
    }

    static void processTreeFile(File inFile, File outFile, ProcessTreeFileListener listener, List<TreeSummaryStatistic> statistics) throws IOException {
        processTreeFile(inFile, outFile, listener, statistics, 0.1);
        // default burnin for CCD construction is 0.1
    }

    static void processTreeFile(File inFile, final File outFile, ProcessTreeFileListener listener, List<TreeSummaryStatistic> statistics, double ccdBurnIn) throws IOException {

        NexusParser nexusParserCCD = new NexusParser();
        nexusParserCCD.parseFile(inFile);
        ccdHandler = new CCDHandler(nexusParserCCD.trees, ccdBurnIn);

        SortedMap<Integer, SortedMap<Integer, Object>> allStats = new TreeMap<>();
        List<String> statisticsNames = new ArrayList<>();

        BufferedReader r = new BufferedReader(new FileReader(inFile));
        String line = r.readLine();
        r.close();

        listener.startProcessing();

        final PrintWriter writer = new PrintWriter(new FileWriter(outFile));

        final NexusParser nexusParser = new NexusParser();
        nexusParser.addListener(new NexusParserListener() {

            // Tree firstTree;
            boolean isUltrametric;
            boolean isBinary;

            @Override
            public void treeParsed(int treeIndex, Tree tree) {

                if (treeIndex == 0) {
                    // firstTree = tree;
                    isUltrametric = TreeUtils.isUltrametric(tree, 1e-8);
                    isBinary = TreeUtils.isBinary(tree);
                    boolean stop = false;

                    // check that the trees conform with the requirements of the selected statistics
                    for (int i = 0; i < statistics.size(); i++) {
                        TreeSummaryStatistic tss = statistics.get(i);

                        if (tss instanceof RequiresReferenceTree refStat) {
                            int refIndex = refStat.getReferenceTreeIndex();
                            Tree refTree = nexusParserCCD.trees.get(refIndex);
                            refStat.setFixedReferenceTree(refTree);
                        }

                        SummaryStatisticDescription ssd = TreeSummaryStatistic.Utils.getDescription(tss);

                        String label = tss.getName();

                        if (!isUltrametric && !ssd.allowsNonultrametricTrees()) {
                            if (!listener.warning("Warning: These trees may not be ultrametric and this is\na requirement of the " +
                                    label + " statistic. Do you wish to continue?")) {
                                stop = true;
                                break;
                            }
                            // don't ask the question again...
                            isUltrametric = true;
                        }

                        if (!isBinary && !ssd.allowsPolytomies()) {
                            if (!listener.warning("Warning: These trees may not be strictly bifurcating and this is\na requirement of the " +
                                    label + " statistic. Do you wish to continue?")) {
                                stop = true;
                                break;
                            }
                            // don't ask the question again...
                            isBinary = true;
                        }

                        for (int j = 0; j < tss.getDimension(tree); j++) {
                            String l = tss.getStatisticLabel(tree, j);
                            statisticsNames.add(l);
                        }
                    }

                    if (stop) {
                        listener.processingHalted();
                    }
                }

                for (TreeSummaryStatistic tss : statistics) {
                    Map<String, Object> stats = tss.getStatistics(tree);
                    for (String key : stats.keySet()) {
                        TreeStatUtils.putInBigMap(treeIndex, key, stats.get(key), allStats, statisticsNames);
                    }
                }

                listener.progress("Processing Tree " + treeIndex + "...");
            }
        });

        if (line.toUpperCase().startsWith("#NEXUS")) {
            try {
                nexusParser.parseFile(inFile);
            } catch (FileNotFoundException fnfe) {
                listener.error("Unable to open file", "File not found '" + inFile + "'");
            } catch (IOException ioe) {
                listener.error("Unable to read file", "Unable to read file: '" + inFile + "' " + ioe);
            } catch (Exception e) {
                listener.error("Error", e.toString());
            }
        }

        TreeStatUtils.writeBigMap(writer, nexusParser.trees.size(), allStats, statisticsNames);

        writer.flush();
        writer.close();

        //TODO log CCD tree
        boolean useCCD0 = useCCD0(statistics);
        boolean useCCD1 = useCCD1(statistics);
        if (useCCD0)
            writeCCDMAPTree(CCDStats.Model.CCD0, outFile);
        if (useCCD1)
            writeCCDMAPTree(CCDStats.Model.CCD1, outFile);
        if (useCCD0 || useCCD1) {
            writeCCDSummary(outFile);
        }

        listener.processingComplete(nexusParser.trees.size());
    }

    private static void writeCCDSummary(File outFile) throws IOException {
        File file = changeExtension(outFile, "-CCD.tsv");

        Files.write(file.toPath(), ccdHandler.getCCDSummary(), Charset.defaultCharset());
    }

    private static boolean useCCD0(List<TreeSummaryStatistic> statistics) {
        return statistics.stream()
                .anyMatch(obj -> obj instanceof CCDStats stat && stat.getCCDModel().equals(CCDStats.Model.CCD0));
    }

    private static boolean useCCD1(List<TreeSummaryStatistic> statistics) {
        return statistics.stream()
                .anyMatch(obj -> obj instanceof CCDStats stat && stat.getCCDModel().equals(CCDStats.Model.CCD1));
    }

    public static File changeExtension(File f, String newExtension) {
        String fileName = f.getName();
        int i = fileName.lastIndexOf('.');

        String name;
        if (i > 0) {
            name = fileName.substring(0, i);
        } else {
            name = fileName; // No extension found
        }

        return new File(f.getParent(), name + newExtension);
    }

    private static void writeCCDMAPTree(CCDStats.Model ccdModel, final File outFile) throws IOException {
        File file = changeExtension(outFile, "-" + ccdModel.name() + "-MAP.tree");

        final PrintWriter writer = new PrintWriter(new FileWriter(file));

        AbstractCCD abstractCCD = ccdHandler.getCCD(ccdModel);
        Tree mapTree = abstractCCD.getMAPTree();
        writer.println(mapTree.getRoot().toNewick());
        writer.close();
    }


    public static CCDHandler getCCDHandler() {
        if (ccdHandler == null)
            throw new IllegalArgumentException("CCDHandler is not initialized !");
        return ccdHandler;
    }


}