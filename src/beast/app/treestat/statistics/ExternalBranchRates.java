/*
 * ExternalBranchRates.java
 *
 * Copyright (C) 2002-2009 Alexei Drummond and Andrew Rambaut
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

package beast.app.treestat.statistics;

import beast.evolution.tree.Node;
import beast.evolution.tree.Tree;

import java.util.List;
import java.util.Set;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name="External Branch Rates",
        description="The rates associated with the external edges of the tree.",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false)
public class ExternalBranchRates extends AbstractTreeSummaryStatistic<Double> {

	public ExternalBranchRates() {
    }

    public int getStatisticDimensions(Tree tree) {
        return tree.getLeafNodeCount();
    }

    public String getStatisticLabel(Tree tree, int i) {
        return "Branch Rate " + Integer.toString(i + 1);
    }

    public Double[] getSummaryStatistic(Tree tree) {

        int externalNodeCount = tree.getLeafNodeCount();
        Double[] stats = new Double[externalNodeCount];
        int count = 0;
        List<Node> nodes = tree.getExternalNodes();
        for (Node node : nodes) {
            stats[count++] = (Double) node.getMetaData("rate");
        }

        return stats;
    }

    public void setTaxonList(String name, Set<String> taxonList) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    public void setInteger(int value) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    public void setDouble(double value) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    public void setString(String value) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    public static final Factory FACTORY = new Factory() {

        public TreeSummaryStatistic createStatistic() {
            return new ExternalBranchRates();
        }

        public String getSummaryStatisticName() {
            return "External Branch Rates";
        }

        public String getSummaryStatisticDescription() {

            return "The rates of the external edges of the tree.";
        }

        public String getSummaryStatisticReference() {

            return "-";
        }

        public boolean allowsPolytomies() {
            return true;
        }

        public boolean allowsNonultrametricTrees() {
            return true;
        }

        public boolean allowsUnrootedTrees() {
            return false;
        }
    };
}