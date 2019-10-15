package beast.app.treestat;

import beast.app.treestat.statistics.SummaryStatisticDescription;
import beast.app.treestat.statistics.TreeSummaryStatistic;
import beast.evolution.tree.Tree;
import beast.evolution.tree.TreeUtils;
import beast.util.NexusParser;
import beast.util.NexusParserListener;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Alexei Drummond
 */
public class TreeStatUtils {

    /**
     * Store the value of the named statistic from the given state
     * @param index the index of the tree (first tree is index 0)
     * @param key the name of the statistic
     * @param value the value of the statistic for the given index
     */
    static void putInBigMap(int index, String key, Object value, SortedMap<String,SortedMap<Integer,Object>> bigMap) {

        SortedMap<Integer,Object> innerMap = bigMap.get(key);
        if (innerMap == null) {
            innerMap = new TreeMap<>();
            bigMap.put(key, innerMap);
        }
        innerMap.put(index, value);
    }

    static void writeBigMap(PrintWriter writer, int numTrees, SortedMap<String,SortedMap<Integer,Object>> bigMap) {

        // Write out the first line of the statistics file
        writer.print("state");
        for (String key : bigMap.keySet()) {
            writer.print("\t" + key);
        }
        writer.println();

        for (int state = 0; state < numTrees; state++) {

            writer.print(state);

            for (String key : bigMap.keySet()) {
                SortedMap<Integer,Object> innerMap = bigMap.get(key);
                Object value = innerMap.get(state);
                if (value == null) {
                    writer.print("\t0");
                } else writer.print("\t"+value);
            }
            writer.println();
        }
    }

    static void processTreeFile(File inFile, final File outFile, ProcessTreeFileListener listener, List<TreeSummaryStatistic> statistics) throws IOException {

        SortedMap<String,SortedMap<Integer,Object>> bigMap = new TreeMap<>();

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
                    }

                    if (stop) {
                        listener.processingHalted();
                    }
                }

                for (TreeSummaryStatistic tss : statistics) {
                    Map<String, Object> stats = tss.getStatistics(tree);
                    for (String key : stats.keySet()) {
                        TreeStatUtils.putInBigMap(treeIndex, key, stats.get(key), bigMap);
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
                listener.error( "Unable to read file","Unable to read file: '" + inFile + "' " + ioe);
            } catch (Exception e) {
                listener.error("Error", e.toString());
            }
        }

        TreeStatUtils.writeBigMap(writer, nexusParser.trees.size(), bigMap);

        writer.flush();
        writer.close();

        listener.processingComplete(nexusParser.trees.size());
    }
}