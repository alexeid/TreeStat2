/*
 * RankProportionStatistic.java
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

package treestat2.statistics;

import beast.base.evolution.tree.Node;
import beast.base.evolution.tree.Tree;
import beast.base.evolution.tree.TreeUtils;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name = "Rank branch length",
        description = "The total length of the branches of the given rank.",
        category = SummaryStatisticDescription.Category.POPULATION_GENETIC,
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsInteger = true)
public class RankBranchLength extends AbstractTreeSummaryStatistic<Double> {

    @Override
    public void setInteger(int value) {
        this.rank = value;
    }

    @Override
    public Double[] getSummaryStatistic(Tree tree) {

        double externalLength = TreeUtils.getExternalLength(tree);
        if (rank == 1) {
            return new Double[]{externalLength};
        }

        double rankLength = 0.0;
        for (Node node : tree.getInternalNodes()) {
            if (!node.isRoot()) {
                if (getRank(node) == rank) {
                    rankLength += node.getLength();
                }
            }
        }

        return new Double[]{rankLength};
    }

    private int getRank(Node node) {
        int childCount = node.getChildCount();
        if (childCount == 0) return 1;
        int size = 0;
        for (Node child : node.getChildren()) {
            size += getRank(child);
        }
        return size;
    }

    public String getSummaryStatisticName() {
        return "Rank " + rank + " branch length";
    }

    public String getSummaryStatisticDescription() {
        return "The total length of branches of rank " + rank + ". ";
    }

    public static final TreeSummaryStatistic.Factory FACTORY = new TreeSummaryStatistic.Factory() {

        @Override
        public String getValueName() {
            return "The rank (k):";
        }
    };

    private int rank = 1;
}
