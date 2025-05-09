/*
 * CollessIndex.java
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

import beast.base.core.Citation;
import beast.base.evolution.tree.Node;
import beast.base.evolution.tree.Tree;

/**
 * @author Alexei Drummond
 */
@Citation(value = "Colless (1982)")
@SummaryStatisticDescription(
        name = "Colless tree-imbalance",
        description = "The normalized sum of differences of number of children in left and right subtrees over all internal nodes",
        category = SummaryStatisticDescription.Category.TREE_SHAPE,
        allowsNonultrametricTrees = true,
        allowsPolytomies = false,
        allowsUnrootedTrees = false)
public class CollessIndex extends AbstractTreeSummaryStatistic<Double> {

    /**
     * Assumes strictly bifurcating tree.
     */
    @Override
    public Double[] getSummaryStatistic(Tree tree) {

        double C = 0.0;
        for (Node node : tree.getInternalNodes()) {

            int r = node.getChild(0).getLeafNodeCount();
            int s = node.getChild(1).getLeafNodeCount();

            C += Math.abs(r - s);
        }

        int n = tree.getLeafNodeCount();
        C *= 2.0 / (n * (n - 3) + 2);
        return new Double[]{C};
    }
}