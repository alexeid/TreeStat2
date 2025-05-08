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

package treestat2;

import beast.base.evolution.tree.Tree;
import beast.base.evolution.tree.TreeParser;
import beast.base.evolution.tree.TreeUtils;
import beast.base.parser.NexusParser;
import jam.framework.Application;
import jam.framework.DocumentFrame;
import jam.util.IconUtils;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.io.*;

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

    @Override
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

    @Override
	protected boolean readFromFile(File file) throws IOException {
        return false;
    }

    @Override
	protected boolean writeToFile(File file) {
        return false;
    }

    @Override
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
                ioe.printStackTrace();
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
        Tree tree;

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

        String message = Integer.toString(treeStatData.allTaxa.size()) + " taxa loaded.";

        statusLabel.setText(message);
        System.out.println(message);
        reader.close();

        fireDataChanged();
    }

    @Override
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

    protected void processTreeFile(final File inFile, final File outFile) throws IOException {
        processTreeFileAction.setEnabled(false);

        ProcessTreeFileListener listener = new ProcessTreeFileListener() {
            ProgressMonitorInputStream in;

            @Override
            public void startProcessing() {

                try {
                    in = new ProgressMonitorInputStream(
                            TreeStatFrame.this,
                            "Reading " + inFile.getName(),
                            new FileInputStream(inFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                in.getProgressMonitor().setMillisToDecideToPopup(0);
                in.getProgressMonitor().setMillisToPopup(0);
            }

            @Override
            public void processingHalted() {
                processTreeFileAction.setEnabled(true);
            }

            @Override
			public void processingComplete(int numTreesProcessed) {
                in.getProgressMonitor().setNote("Writing out statistics...");
                progressLabel.setText("" + numTreesProcessed + " trees processed.");
                processTreeFileAction.setEnabled(true);
            }

            @Override
            public boolean warning(String message) {
                return JOptionPane.showConfirmDialog(TreeStatFrame.this, message, "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            }

            @Override
            public void error(String errorTitle, String errorMessage) {
                JOptionPane.showMessageDialog(TreeStatFrame.this, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
            }

            @Override
            public void progress(String progress) {

            }

        };

        TreeStatUtils.processTreeFile(inFile, outFile, listener, treeStatData.statistics);
    }

    @Override
	public JComponent getExportableComponent() {
        return statisticsPanel.getExportableComponent();
    }

    protected AbstractAction importTaxaAction = new AbstractAction("Import Taxa...") {
        /**
         *
         */
        private static final long serialVersionUID = -3185667996732228702L;

        @Override
		public void actionPerformed(java.awt.event.ActionEvent ae) {
            doImport();
        }
    };

    protected AbstractAction processTreeFileAction = new AbstractAction("Process Tree File...", gearIcon) {
        /**
         *
         */
        private static final long serialVersionUID = -8285433136692586532L;

        @Override
		public void actionPerformed(java.awt.event.ActionEvent ae) {
            doExport();
        }
    };
}
