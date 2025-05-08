/*
 * InternalBranchLengths.java
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

package treestat2.statistics;

import beast.base.evolution.tree.Node;
import beast.base.evolution.tree.Tree;

import java.util.Set;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name = "Internal Branch Lengths",
        description = "The branch lengths of the internal edges in the tree.",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false)
public class InternalBranchLengths extends AbstractTreeSummaryStatistic<Double> {

    @Override
    public String getStatisticLabel(Tree tree, int i) {
        return "Branch " + Integer.toString(i + 1);
    }

    @Override
    public Double[] getSummaryStatistic(Tree tree) {

        int internalNodeCount = tree.getInternalNodeCount();
        Double[] stats = new Double[internalNodeCount - 1];
        int count = 0;
        for (Node node : tree.getInternalNodes()) {
            if (!node.isRoot()) {
                stats[count++] = node.getLength();
            }
        }
        return stats;
    }

    @Override
    public void setTaxonList(String name, Set<String> taxonList) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    @Override
    public void setInteger(int value) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    @Override
    public void setDouble(double value) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    @Override
    public void setString(String value) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }
}