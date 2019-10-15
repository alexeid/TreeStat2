/*
 * TMRCASummaryStatistic.java
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

package beast.app.treestat.statistics;

import beast.evolution.tree.Node;
import beast.evolution.tree.Tree;
import beast.evolution.tree.TreeUtils;

/**
 * @author Alexei Drummond
 */

@SummaryStatisticDescription(
        name="Mean Clade Attribute",
        description="Extracts the mean of a named attribute for a clade defined by a taxon set.",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsWholeTree = true,
        allowsTaxonList = true,
        allowsString = true)
public class CladeMeanAttributeStatistic extends AbstractTreeSummaryStatistic<Double> {

	public CladeMeanAttributeStatistic() {
        this.attributeName = "";
    }

    @Override
	public void setString(String value) {
        this.attributeName = value;
    }


    @Override
	public Double[] getSummaryStatistic(Tree tree) {
        Node node;
        if (taxonList == null) {
            node = tree.getRoot();
        } else {
            node = TreeUtils.getCommonAncestorNode(tree, taxonList);
            if (node == null) throw new RuntimeException("No clade found that contains " + taxonList);
        }

        sumAttribute = 0.0;
        nodeCount = 0;
        getAttribute(node);

        double meanAttribute = sumAttribute / nodeCount;

        return new Double[]{meanAttribute};
    }

    private double sumAttribute;
    private int nodeCount;

    private void getAttribute(Node node) {
        if (!node.isLeaf()) {
            for (Node child : node.getChildren()) {
                getAttribute(child);
            }
        }

        Object item = node.getMetaData(attributeName);
        if (item != null && item instanceof Number) {
            sumAttribute += ((Number) item).doubleValue();
            nodeCount++;
        } else {
            // just ignore it
        }
    }

    public String getSummaryStatisticName() {
        if (taxonList != null) {
            return attributeName + "(" + taxonListName + ")";
        } else {
            return attributeName + "(root)";
        }
    }

    public String getSummaryStatisticDescription() {
        if (taxonList != null) {
            return "Gets the mean of an attribute for a clade defined by a taxon set";
        }
        return "Gets the mean of an attribute across the tree.";
    }

    public static final Factory FACTORY = new Factory() {

        @Override
		public TreeSummaryStatistic<?> createStatistic() {
            return new CladeMeanAttributeStatistic();
        }

        @Override
		public String getValueName() {
            return "The attribute name:";
        }

        @Override
		public boolean allowsWholeTree() {
            return true;
        }

        @Override
		public boolean allowsTaxonList() {
            return true;
        }

        @Override
		public boolean allowsString() {
            return true;
        }
    };

    private String attributeName = null;
}