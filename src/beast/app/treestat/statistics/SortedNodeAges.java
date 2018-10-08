/*
 * NodeHeights.java
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

import java.util.Arrays;
import java.util.List;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name = "Sorted Node Ages",
        description = "The ages of each internal node in the tree.",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false)
public class SortedNodeAges extends AbstractTreeSummaryStatistic<Double> {

    public String getStatisticLabel(Tree tree, int i) {
        return "Age " + Integer.toString(i + 1);
    }

    public Double[] getSummaryStatistic(Tree tree) {

        List<Node> internalNodes = tree.getInternalNodes();
        Double[] stats = new Double[internalNodes.size()];
        for (int i = 0; i < internalNodes.size(); i++) {
            stats[i] = internalNodes.get(i).getHeight();
        }
        Arrays.sort(stats);

        return stats;
    }

//    public void setTaxonList(String name, Set<String> taxonList) {
//        throw new UnsupportedOperationException("not implemented in this statistic");
//    }
//
//    public void setInteger(int value) {
//        throw new UnsupportedOperationException("not implemented in this statistic");
//    }
//
//    public void setDouble(double value) {
//        throw new UnsupportedOperationException("not implemented in this statistic");
//    }
//
//    public void setString(String value) {
//        throw new UnsupportedOperationException("not implemented in this statistic");
//    }
}
