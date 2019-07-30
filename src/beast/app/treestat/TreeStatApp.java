/*
 * TreeStatApp.java
 *
 * Copyright (C) 2002-2013 Alexei Drummond and Andrew Rambaut
 *
 * This file is part of BEAST2.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership and licensing.
 *
 * BEAST2 is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 *  BEAST is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BEAST; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package beast.app.treestat;

import beast.app.treestat.statistics.SummaryStatisticDescription;
import beast.app.treestat.statistics.TreeSummaryStatistic;
import beast.evolution.tree.Tree;
import beast.evolution.tree.TreeUtils;
import beast.util.NexusParser;
import beast.util.NexusParserListener;
import jam.framework.SingleDocApplication;
import jam.mac.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class TreeStatApp extends SingleDocApplication {

    public TreeStatApp(String nameString, String aboutString, Icon icon, String websiteURLString, String helpURLString) {
        super(nameString, aboutString, icon, websiteURLString, helpURLString);
    }

    static TreeSummaryStatistic processLine(String[] parts) {
        try {
            Class<? extends TreeSummaryStatistic> tssClass = (Class<? extends TreeSummaryStatistic>) Class.forName(parts[0]);
            SummaryStatisticDescription ssd = TreeSummaryStatistic.getSummaryStatisticDescription(tssClass);

            TreeSummaryStatistic statistic = tssClass.newInstance();

            if (ssd.allowsDouble()) {
                statistic.setDouble(Double.parseDouble(parts[1]));
            } else if (ssd.allowsInteger()) {
                statistic.setInteger(Integer.parseInt(parts[1]));
            } else if (ssd.allowsString()) {  // allowsString
                statistic.setString(parts[1]);
            }
            return statistic;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static void processTreeFile(File inFile, final File outFile, List<TreeSummaryStatistic> statistics) throws IOException {

        BufferedReader r = new BufferedReader(new FileReader(inFile));
        String line = r.readLine();
        r.close();

        SortedMap<String,SortedMap<Integer,Object>> bigMap = new TreeMap<>();

        final PrintWriter writer = new PrintWriter(new FileWriter(outFile));

        final NexusParser nexusParser = new NexusParser();
        nexusParser.addListener(new NexusParserListener() {

            Tree firstTree;
            boolean isUltrametric;
            boolean isBinary;


            public void treeParsed(int treeIndex, Tree tree) {

                if (treeIndex == 0) {
                    firstTree = tree;
                    isUltrametric = TreeUtils.isUltrametric(tree, 1e-8);
                    isBinary = TreeUtils.isBinary(tree);
                    boolean stop = false;

                    // check that the trees conform with the requirements of the selected statistics
                    for (int i = 0; i < statistics.size(); i++) {
                        TreeSummaryStatistic tss = statistics.get(i);

                        SummaryStatisticDescription ssd = TreeSummaryStatistic.Utils.getDescription(tss);

                        String label = tss.getName();

                        if (!isUltrametric && !ssd.allowsNonultrametricTrees()) {
                            throw new RuntimeException("Warning: These trees may not be ultrametric and this is\na requirement of the " +
                                            label + " statistic.");
                        }

                        if (!isBinary && !ssd.allowsPolytomies()) {
                            throw new RuntimeException("Warning: These trees may not be strictly bifurcating and this is\na requirement of the " +
                                            label + " statistic.");
                        }
                    }
                }

                for (int i = 0; i < statistics.size(); i++) {
                    TreeSummaryStatistic tss = statistics.get(i);
                    Map<String,Object> stats = tss.getStatistics(tree);
                    for (String key : stats.keySet()) {
                        putInBigMap(treeIndex, key, stats.get(key), bigMap);
                    }
                }
            }
        });

        if (line.toUpperCase().startsWith("#NEXUS")) {
                nexusParser.parseFile(inFile);
        } else {
            //reader.close();
            //importer = new NewickImporter(reader);
        }

        writeBigMap(writer, nexusParser.trees.size(), bigMap);


        writer.flush();
        writer.close();
    }

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

    // Main entry point
    static public void main(String[] args) throws IOException {

        if (args.length > 0) {
            System.out.println("Processing control file: " + args[0]);
            File file = new File(args[0]);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            List<TreeSummaryStatistic> statistics = new ArrayList<>();

            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split(" ");
                TreeSummaryStatistic statistic = processLine(parts);
                if (statistic != null) {
                    System.out.println("  Adding statistic: " + statistic.getName());
                    statistics.add(statistic);
                }
                line = reader.readLine();
            }

            for (int i = 1; i < args.length; i++) {
                String fileName = args[i];

                File inFile = new File(fileName);
                File outFile = new File(fileName + ".log");

                System.out.println("  Processing tree file: " + fileName);
                processTreeFile(inFile, outFile, statistics);
            }
        } else {

            // There is a major issue with languages that use the comma as a decimal separator.
            // To ensure compatibility between programs in the package, enforce the US locale.
            Locale.setDefault(Locale.US);

            //if (OSType.isMac()) {
            if (Utils.getMacOSXVersion().startsWith("10.5")) {
                System.setProperty("apple.awt.brushMetalLook", "true");
            }
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.showGrowBox", "true");
            UIManager.put("SystemFont", new Font("Lucida Grande", Font.PLAIN, 13));
            UIManager.put("SmallSystemFont", new Font("Lucida Grande", Font.PLAIN, 11));
            //}

            try {

                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                java.net.URL url = TreeStatApp.class.getResource("images/TreeStat.png");
                Icon icon = null;

                if (url != null) {
                    icon = new ImageIcon(url);
                }

                String nameString = "TreeStat2";
                String aboutString = "<html><center><p>Tree Statistic Calculation Tool<br>" +
                        "<p>by<br>" +
                        "Andrew Rambaut and Alexei J. Drummond</p>" +
                        "<p>Institute of Evolutionary Biology, University of Edinburgh<br>" +
                        "<a href=\"mailto:a.rambaut@ed.ac.uk\">a.rambaut@ed.ac.uk</a></p>" +
                        "<p>Department of Computer Science, University of Auckland<br>" +
                        "<a href=\"mailto:alexei@cs.auckland.ac.nz\">alexei@cs.auckland.ac.nz</a></p>" +
                        "<p>Visit the BEAST page:<br>" +
                        "<a href=\"http://beast.bio.ed.ac.uk/\">http://beast.bio.ed.ac.uk/</a></p>" +
                        "</center></html>";

                String websiteURLString = "https://github.com/alexeid/TreeStat2/";
                String helpURLString = "https://github.com/alexeid/TreeStat2/";

                TreeStatApp app = new TreeStatApp(nameString, aboutString, icon,
                        websiteURLString, helpURLString);
                app.setDocumentFrame(new TreeStatFrame(app, nameString));
                app.initialize();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(new JFrame(), "Fatal exception: " + e,
                        "Please report this to the authors",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
