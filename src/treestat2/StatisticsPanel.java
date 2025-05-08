/*
 * StatisticsPanel.java
 *
 * Copyright (C) 2002-2011 Alexei Drummond and Andrew Rambaut
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
 * BEAST is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BEAST; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package treestat2;

import jam.framework.Exportable;
import jam.panels.OptionsPanel;
import jam.table.TableRenderer;
import jam.util.IconUtils;
import treestat2.statistics.SummaryStatisticDescription;
import treestat2.statistics.TreeSummaryStatistic;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class StatisticsPanel extends OptionsPanel implements Exportable {


    /**
     *
     */
    private static final long serialVersionUID = -8026203872020056264L;
    TreeStatFrame frame;

    // Get available statistics from the TreeStatisticRegistry
    private final List<Class<? extends TreeSummaryStatistic>> availableStatistics = TreeStatisticRegistry.getAvailableStatistics();

    TreeStatData treeStatData;

    JScrollPane scrollPane1;
    JScrollPane scrollPane2 = null;
    JTable includedStatisticsTable = null;
    JTable availableStatisticsTable;
    AvailableStatisticsTableModel availableStatisticsTableModel;
    IncludedStatisticsTableModel includedStatisticsTableModel = null;
    TreeSummaryStatisticLabel statisticLabel = new TreeSummaryStatisticLabel(null);

    public StatisticsPanel(TreeStatFrame frame, TreeStatData treeStatData) {

        this.frame = frame;
        this.treeStatData = treeStatData;

        setOpaque(false);

        Icon includeIcon = IconUtils.getIcon(this.getClass(), "images/include.png");
        Icon excludeIcon = IconUtils.getIcon(this.getClass(), "images/exclude.png");

        // Available statistics
        availableStatisticsTableModel = new AvailableStatisticsTableModel();
        TableSorter sorter = new TableSorter(availableStatisticsTableModel);
        availableStatisticsTable = new JTable(sorter);

        availableStatisticsTable.getColumnModel().getColumn(0).setCellRenderer(
                new TableRenderer(SwingConstants.LEFT, new Insets(0, 4, 0, 4)));

        availableStatisticsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
			public void valueChanged(ListSelectionEvent evt) {
                statisticsTableSelectionChanged(false);
            }
        });

        scrollPane1 = new JScrollPane(availableStatisticsTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel buttonPanel1 = createAddRemoveButtonPanel(
                includeStatisticAction, includeIcon, null,
                excludeStatisticAction, excludeIcon, null,
                javax.swing.BoxLayout.Y_AXIS);

        // Included statistics
        includedStatisticsTableModel = new IncludedStatisticsTableModel();
        sorter = new TableSorter(includedStatisticsTableModel);
        includedStatisticsTable = new JTable(sorter);

        includedStatisticsTable.getColumnModel().getColumn(0).setCellRenderer(
                new TableRenderer(SwingConstants.LEFT, new Insets(0, 4, 0, 4)));

        includedStatisticsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
			public void valueChanged(ListSelectionEvent evt) {
                statisticsTableSelectionChanged(true);
            }
        });

        scrollPane2 = new JScrollPane(includedStatisticsTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 0.5;
        c.weighty = 0.75;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(6, 6, 6, 6);
        c.gridx = 0;
        c.gridy = 0;
        add(scrollPane1, c);

        c.weightx = 0;
        c.weighty = 0.75;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(6, 0, 6, 0);
        c.gridx = 1;
        c.gridy = 0;
        add(buttonPanel1, c);

        c.weightx = 0.5;
        c.weighty = 0.75;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(6, 6, 6, 6);
        c.gridx = 2;
        c.gridy = 0;
        add(scrollPane2, c);

        c.weightx = 1.0;
        c.weighty = 0.25;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(6, 6, 6, 6);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        add(statisticLabel, c);

        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    }

    JPanel createAddRemoveButtonPanel(Action addAction, Icon addIcon, String addToolTip,
                                      Action removeAction, Icon removeIcon, String removeToolTip, int axis) {

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, axis));
        buttonPanel.setOpaque(false);
        JButton addButton = new JButton(addAction);
        if (addIcon != null) {
            addButton.setIcon(addIcon);
            addButton.setText(null);
        }
        addButton.setToolTipText(addToolTip);
        addButton.putClientProperty("JButton.buttonType", "textured");
        addButton.setOpaque(false);
        addAction.setEnabled(false);

        JButton removeButton = new JButton(removeAction);
        if (removeIcon != null) {
            removeButton.setIcon(removeIcon);
            removeButton.setText(null);
        }
        removeButton.setToolTipText(removeToolTip);
        removeButton.putClientProperty("JButton.buttonType", "textured");
        removeButton.setOpaque(false);
        removeAction.setEnabled(false);

        buttonPanel.add(addButton);
        buttonPanel.add(new JToolBar.Separator(new Dimension(6, 6)));
        buttonPanel.add(removeButton);

        return buttonPanel;
    }

    @Override
	public JComponent getExportableComponent() {
        return this;
    }

    public void dataChanged() {
        availableStatisticsTableModel.fireTableDataChanged();
        includedStatisticsTableModel.fireTableDataChanged();
    }

    private void statisticsTableSelectionChanged(boolean includedTable) {
        if (includedTable) {
            int index = includedStatisticsTable.getSelectedRow();
            if (index != -1) {
                availableStatisticsTable.clearSelection();
                TreeSummaryStatistic tss = treeStatData.statistics.get(index);
                statisticLabel.setSummaryStatisticDescription(
                        tss.getClass().getAnnotation(SummaryStatisticDescription.class));
                excludeStatisticAction.setEnabled(true);
            } else {
                excludeStatisticAction.setEnabled(false);
            }
        } else {
            int index = availableStatisticsTable.getSelectedRow();
            if (index != -1) {
                includedStatisticsTable.clearSelection();
                Class<? extends TreeSummaryStatistic> ssd = availableStatistics.get(index);
                statisticLabel.setSummaryStatisticDescription(
                        ssd.getAnnotation(SummaryStatisticDescription.class));
                includeStatisticAction.setEnabled(true);
            } else {
                includeStatisticAction.setEnabled(false);
            }
        }
    }

    Action includeStatisticAction = new AbstractAction("->") {
        /**
         *
         */
        private static final long serialVersionUID = -7179224487959650620L;

        @Override
		public void actionPerformed(ActionEvent ae) {
            int[] indices = availableStatisticsTable.getSelectedRows();
            for (int i = indices.length - 1; i >= 0; i--) {
                Class<? extends TreeSummaryStatistic> ssd = availableStatistics.get(indices[i]);
                TreeSummaryStatistic tss = null;
                try {
                    tss = createStatistic(ssd);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InstantiationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                if (tss != null) {
                    treeStatData.statistics.add(tss);
                }
            }
            if (frame != null) {
            	frame.fireDataChanged();
            }
            dataChanged();
        }
    };

    Action excludeStatisticAction = new AbstractAction("<-") {
        /**
         *
         */
        private static final long serialVersionUID = -3904236403703620633L;

        @Override
		public void actionPerformed(ActionEvent ae) {
            int[] indices = includedStatisticsTable.getSelectedRows();
            for (int i = indices.length - 1; i >= 0; i--) {
                treeStatData.statistics.remove(indices[i]);
            }

            if (frame != null) {
            	frame.fireDataChanged();
            }
            dataChanged();
        }
    };

    public TreeSummaryStatistic<?> createStatistic(Class<? extends TreeSummaryStatistic> tssClass) throws IllegalAccessException, InstantiationException {

        SummaryStatisticDescription ssd = TreeSummaryStatistic.getSummaryStatisticDescription(tssClass);

        if (!ssd.allowsTaxonList() &&
                !ssd.allowsDouble() && !ssd.allowsInteger() && !ssd.allowsString()) {
            return tssClass.newInstance();
        }

        OptionsPanel optionPanel = new OptionsPanel();

        optionPanel.addSpanningComponent(new JLabel(ssd.description()));

        final JRadioButton wholeTreeRadio = new JRadioButton("For the whole tree", false);
        final JRadioButton taxonSetRadio = new JRadioButton("Using a given taxon set", false);
        final JComboBox<Object> taxonSetCombo = new JComboBox<>();
        final JTextField valueField;

        if (ssd.allowsTaxonList()) {

            for (Object taxonSet : treeStatData.taxonSets) {
                taxonSetCombo.addItem(taxonSet);
            }

            ButtonGroup group = new ButtonGroup();

            ItemListener listener = e -> taxonSetCombo.setEnabled(taxonSetRadio.isSelected());

            if (ssd.allowsWholeTree()) {
                group.add(wholeTreeRadio);
                wholeTreeRadio.addItemListener(listener);

                optionPanel.addSpanningComponent(wholeTreeRadio);
                optionPanel.addSeparator();
            }

            if (ssd.allowsTaxonList()) {
                group.add(taxonSetRadio);
                taxonSetRadio.addItemListener(listener);

                optionPanel.addSpanningComponent(taxonSetRadio);
                optionPanel.addComponentWithLabel("Taxon Set: ", taxonSetCombo);
                optionPanel.addSeparator();
            }

            if (ssd.allowsTaxonList()) {
                taxonSetRadio.setSelected(true);
            }
            if (ssd.allowsWholeTree()) {
                System.out.println("wholeTreeRadio selected");
                wholeTreeRadio.setSelected(true);
            }
        }

        if (ssd.allowsDouble() || ssd.allowsInteger() || ssd.allowsString()) {
            if (ssd.allowsDouble()) {
                valueField = new JTextField();
                valueField.setColumns(12);
                optionPanel.addComponentWithLabel(ssd.valueName(), valueField);
            } else if (ssd.allowsInteger()) {
                valueField = new WholeNumberField();
                valueField.setColumns(12);
                optionPanel.addComponentWithLabel(ssd.valueName(), valueField);
            } else {  // allowsString
                valueField = new JTextField();
                valueField.setColumns(24);
                optionPanel.addComponentWithLabel(ssd.valueName(), valueField);
            }
        } else {
            valueField = null;
        }

        JOptionPane optionPane = new JOptionPane(optionPanel,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null,
                null,
                null);
        optionPane.setBorder(new EmptyBorder(12, 12, 12, 12));

        JDialog dialog = optionPane.createDialog(frame, ssd.name());
        //		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.pack();
        dialog.setVisible(true);

        if (optionPane.getValue() == null) {
            System.out.println("optionPane.getValue() == null");
            return null;
        }

        int result = (Integer) optionPane.getValue();
        if (result == -1 || result == JOptionPane.CANCEL_OPTION) {
            System.out.println("optionPane.getValue() == " + result);
            return null;
        }

        TreeSummaryStatistic<?> statistic = tssClass.newInstance();

        if (wholeTreeRadio.isSelected()) {
            statistic = tssClass.newInstance();
        } else if (taxonSetRadio.isSelected()) {
            TreeStatData.TaxonSet t = (TreeStatData.TaxonSet) taxonSetCombo.getSelectedItem();
            Set<String> taxa = new TreeSet<String>();
            for (Object aTaxa : t.taxa) {
                taxa.add((String) aTaxa);
            }
            statistic.setTaxonList(t.name, taxa);
        } else {
            //System.out.println("No radio selected!");
            //return null;
        }

        if (ssd.allowsDouble()) {
            Double value = Double.parseDouble(valueField.getText());
            statistic.setDouble(value);
        } else if (ssd.allowsInteger()) {
            assert valueField instanceof WholeNumberField;

            Integer value = ((WholeNumberField) valueField).getValue();
            statistic.setInteger(value);
        } else if (ssd.allowsString()) {

            System.out.println("SetString!");

            String value = valueField.getText();
            statistic.setString(value);
        }

        return statistic;
    }

    public static class AvailableStatisticsTableModel extends AbstractTableModel {

        /**
         *
         */
        private static final long serialVersionUID = 86401307035717809L;

        // Get available statistics from the TreeStatisticRegistry
        private final List<Class<? extends TreeSummaryStatistic>> availableStatistics = TreeStatisticRegistry.getAvailableStatistics();

        public AvailableStatisticsTableModel() {
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return availableStatistics.size();
        }

        @Override
        public Object getValueAt(int row, int col) {
            // Return the statistic name and category for each row
            if (col == 0) {
                return TreeSummaryStatistic.getSummaryStatisticDescription(availableStatistics.get(row)).name();
            }
            return TreeSummaryStatistic.getSummaryStatisticDescription(availableStatistics.get(row)).category().getPrettyName();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        @Override
        public String getColumnName(int column) {
            // Set column names
            if (column == 0) return "Statistic Name";
            return "Category";
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }


    class IncludedStatisticsTableModel extends AbstractTableModel {

        /**
         *
         */
        private static final long serialVersionUID = -7280629792388705376L;

        public IncludedStatisticsTableModel() {
        }

        @Override
		public int getColumnCount() {
            return 1;
        }

        @Override
		public int getRowCount() {
            if (treeStatData == null || treeStatData.statistics == null) return 0;

            return treeStatData.statistics.size();
        }

        @Override
		public Object getValueAt(int row, int col) {
            if (treeStatData == null || treeStatData.statistics == null) return null;
            if (col == 0)
                return treeStatData.statistics.get(row).getName();
            return TreeSummaryStatistic.Utils.getDescription(treeStatData.statistics.get(row)).name();
        }

        @Override
		public boolean isCellEditable(int row, int col) {
            return false;
        }

        @Override
		public String getColumnName(int column) {
            if (column == 0) return "Statistic Name";
            return "Description";
        }

        @Override
		public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }

    class TreeSummaryStatisticLabel extends JLabel {

        /**
         *
         */
        private static final long serialVersionUID = -5204925491148650874L;

        public TreeSummaryStatisticLabel(SummaryStatisticDescription statistic) {
            setSummaryStatisticDescription(statistic);
            setVerticalAlignment(SwingConstants.TOP);
            setHorizontalAlignment(SwingConstants.LEFT);
        }

        public void setSummaryStatisticDescription(SummaryStatisticDescription statistic) {
            String html = "";
            if (statistic != null) {
                html = "<html><body><h3>" + statistic.name() + "</h3>" +
                        "<em>" + statistic.description() + "</em>";

                html += "<ul>";
                if (!statistic.allowsNonultrametricTrees()) {
                    html += "<li>Trees must be ultrametric.</li>";
                } else if (!statistic.allowsUnrootedTrees()) {
                    html += "<li>Trees must be rooted.</li>";
                }
                if (!statistic.allowsPolytomies()) {
                    html += "<li>Trees must be strictly bifurcating.</li>";
                }
//                html += "</ul>";
//                String ref = statistic.getSummaryStatisticReference();
//                if (ref != null && !ref.equals("") && !ref.equals("-")) {
//                    html += "Reference: " + ref;
//                }
                html += "</body></html>";
            }

            setText(html);
        }
    }

}
