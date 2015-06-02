/*
 * TreeStatFrame.java
 *
 * Copyright (C) 2002-2006 Alexei Drummond and Andrew Rambaut
 *
 * This file is part of BEAST.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership and licensing.
 *
 * BEAST is free software; you can redistribute it and/or modify
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
import beast.util.TreeParser;
import jam.framework.Application;
import jam.framework.DocumentFrame;
import jam.util.IconUtils;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class TreeStatFrame extends DocumentFrame {

    private static final long serialVersionUID = -1775448072034877658L;

    private TreeStatData treeStatData = null;

    private JTabbedPane tabbedPane = new JTabbedPane();

    private TaxonSetsPanel taxonSetsPanel;
    private StatisticsPanel statisticsPanel;

    private JLabel statusLabel;
    private JLabel progressLabel;
    private JProgressBar progressBar;

    final Icon gearIcon = IconUtils.getIcon(this.getClass(), "images/gear.png");

    public TreeStatFrame(Application application, String title) {
        super();

        setTitle(title);

        treeStatData = new TreeStatData();

        setImportAction(importTaxaAction);
        setExportAction(processTreeFileAction);

        getOpenAction().setEnabled(false);
        getSaveAction().setEnabled(false);
        getSaveAsAction().setEnabled(false);
    }

    public void initializeComponents() {

        setSize(new java.awt.Dimension(800, 600));

        taxonSetsPanel = new TaxonSetsPanel(this, treeStatData);
        statisticsPanel = new StatisticsPanel(this, treeStatData);

        tabbedPane.addTab("Statistics", null, statisticsPanel);
        tabbedPane.addTab("Taxon Sets", null, taxonSetsPanel);

        statusLabel = new JLabel("No statistics selected");
        processTreeFileAction.setEnabled(false);

        JPanel progressPanel = new JPanel(new BorderLayout(0, 0));
        progressLabel = new JLabel("");
        progressBar = new JProgressBar();
        progressPanel.add(progressLabel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);

        JPanel panel2 = new JPanel(new FlowLayout());
        JButton goButton = new JButton(processTreeFileAction);
        goButton.setFocusable(false);
        goButton.putClientProperty("JButton.buttonType", "textured");
        goButton.setMargin(new Insets(4, 4, 4, 4));
        panel2.add(goButton);
        panel2.add(progressPanel);

        JPanel panel1 = new JPanel(new BorderLayout(0, 0));
        panel1.add(statusLabel, BorderLayout.WEST);
        panel1.add(panel2, BorderLayout.EAST);
        panel1.setBorder(new BorderUIResource.EmptyBorderUIResource(new java.awt.Insets(0, 6, 0, 6)));

        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(panel1, BorderLayout.SOUTH);
        panel.setBorder(new BorderUIResource.EmptyBorderUIResource(new java.awt.Insets(12, 12, 12, 12)));

        getContentPane().setLayout(new java.awt.BorderLayout(0, 0));
        getContentPane().add(panel, BorderLayout.CENTER);
    }

    public void fireDataChanged() {
        if (treeStatData.statistics.size() > 0) {
            statusLabel.setText("" + treeStatData.statistics.size() + " statistics selected");
            processTreeFileAction.setEnabled(true);
        } else {
            statusLabel = new JLabel("No statistics selected");
            processTreeFileAction.setEnabled(false);
        }
        taxonSetsPanel.dataChanged();
        statisticsPanel.dataChanged();

    }

    protected boolean readFromFile(File file) throws IOException {
        return false;
    }

    protected boolean writeToFile(File file) {
        return false;
    }

    public final void doImport() {

        FileDialog dialog = new FileDialog(this,
                "Import Tree File...",
                FileDialog.LOAD);

        dialog.setVisible(true);
        if (dialog.getFile() != null) {
            File file = new File(dialog.getDirectory(), dialog.getFile());

            try {
                importFromFile(file);
            } catch (FileNotFoundException fnfe) {
                JOptionPane.showMessageDialog(this, "Unable to open file: File not found",
                        "Unable to open file",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this, "Unable to read file: " + ioe,
                        "Unable to read file",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    protected void importFromFile(File file) throws Exception {

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = reader.readLine();
        Tree tree = null;

        if (line.toUpperCase().startsWith("#NEXUS")) {
            NexusParser parser = new NexusParser();
            reader.close();
            parser.parseFile(file);
            tree = parser.trees.get(0);
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(line);
            while (line != null) {
                line = reader.readLine();
                builder.append(line);
            }
            reader.close();

            tree = new TreeParser(builder.toString());
        }

        treeStatData.allTaxa = TreeUtils.getDescendantLeaves(tree, tree.getRoot());
        statusLabel.setText(Integer.toString(treeStatData.allTaxa.size()) + " taxa loaded.");
        reader.close();

        fireDataChanged();
    }

    public final void doExport() {

        FileDialog inDialog = new FileDialog(this,
                "Import Tree File...",
                FileDialog.LOAD);

        inDialog.setVisible(true);
        if (inDialog.getFile() != null) {
            File inFile = new File(inDialog.getDirectory(), inDialog.getFile());


            FileDialog outDialog = new FileDialog(this,
                    "Save Log File As...",
                    FileDialog.SAVE);

            outDialog.setVisible(true);
            if (outDialog.getFile() != null) {

                File outFile = new File(outDialog.getDirectory(), outDialog.getFile());

                try {
                    processTreeFile(inFile, outFile);

                } catch (FileNotFoundException fnfe) {
                    JOptionPane.showMessageDialog(this, "Unable to open file: File not found",
                            "Unable to open file",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(this, "Unable to read/write file: " + ioe,
                            "Unable to read/write file",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e,
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    protected void processTreeFile(File inFile, final File outFile) throws IOException {
        processTreeFileAction.setEnabled(false);

        BufferedReader r = new BufferedReader(new FileReader(inFile));
        String line = r.readLine();
        r.close();

        final ProgressMonitorInputStream in = new ProgressMonitorInputStream(
                this,
                "Reading " + inFile.getName(),
                new FileInputStream(inFile));
        in.getProgressMonitor().setMillisToDecideToPopup(0);
        in.getProgressMonitor().setMillisToPopup(0);

        final Reader reader = new InputStreamReader(new BufferedInputStream(in));
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
                    for (int i = 0; i < treeStatData.statistics.size(); i++) {
                        TreeSummaryStatistic tss = treeStatData.statistics.get(i);

                        SummaryStatisticDescription ssd = TreeSummaryStatistic.Utils.getDescription(tss);

                        String label = ssd.name();

                        if (!isUltrametric && !ssd.allowsNonultrametricTrees()) {
                            if (JOptionPane.showConfirmDialog(
                                    TreeStatFrame.this, "Warning: These trees may not be ultrametric and this is\na requirement of the " +
                                    label + " statistic. Do you wish to continue?", "Warning", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                                stop = true;
                                break;
                            }
                            // don't ask the question again...
                            isUltrametric = true;
                        }

                        if (!isBinary && !ssd.allowsPolytomies()) {
                            if (JOptionPane.showConfirmDialog(
                                    TreeStatFrame.this, "Warning: These trees may not be strictly bifurcating and this is\na requirement of the " +
                                    label + " statistic. Do you wish to continue?", "Warning", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                                stop = true;
                                break;
                            }
                            // don't ask the question again...
                            isBinary = true;
                        }
                    }

                    if (stop) {
                        processTreeFileAction.setEnabled(true);
                        return;
                    }
                }

                //writer.print(treeIndex);

                for (int i = 0; i < treeStatData.statistics.size(); i++) {
                    TreeSummaryStatistic tss = treeStatData.statistics.get(i);
                    Map<String,Object> stats = tss.getStatistics(tree);
                    for (String key : stats.keySet()) {
                        putInBigMap(key, treeIndex, stats.get(key));
                    }
                }
                //writer.println();

                in.getProgressMonitor().setNote("Processing Tree " + treeIndex + "...");
            }
        });

        if (line.toUpperCase().startsWith("#NEXUS")) {
            try {
                System.out.println("trying to parse " + inFile);
                nexusParser.parseFile(inFile);
            } catch (Exception e) {
            }
        } else {
            //reader.close();
            //importer = new NewickImporter(reader);
        }

        in.getProgressMonitor().setNote("Writing out statistics...");

        writeBigMap(writer, nexusParser.trees.size());

        progressLabel.setText("" + nexusParser.trees.size() + " trees processed.");
        processTreeFileAction.setEnabled(true);

        writer.flush();
        writer.close();
    }

    private void writeBigMap(PrintWriter writer, int numTrees) {

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

    private int state = 0;

    public void doCopy() {
//		statisticsPanel.doCopy();
    }

    public JComponent getExportableComponent() {

        return statisticsPanel.getExportableComponent();
    }

    public void putInBigMap(String key, int state, Object value) {
        SortedMap<Integer,Object> innerMap = bigMap.get(key);
        if (innerMap == null) {
            innerMap = new TreeMap<Integer, Object>();
            bigMap.put(key, innerMap);
        }
        innerMap.put(state, value);
    }

    protected AbstractAction importTaxaAction = new AbstractAction("Import Taxa...") {
        /**
         *
         */
        private static final long serialVersionUID = -3185667996732228702L;

        public void actionPerformed(java.awt.event.ActionEvent ae) {
            doImport();
        }
    };

    protected AbstractAction processTreeFileAction = new AbstractAction("Process Tree File...", gearIcon) {
        /**
         *
         */
        private static final long serialVersionUID = -8285433136692586532L;

        public void actionPerformed(java.awt.event.ActionEvent ae) {
            doExport();
        }
    };

    SortedMap<String,SortedMap<Integer,Object>> bigMap = new TreeMap<String,SortedMap<Integer,Object>>();

    //NexusParser nexusParser;
}
