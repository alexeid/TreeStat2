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
import beast.base.evolution.tree.TreeUtils;

import java.util.*;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name = "Branch Lengths",
        description = "The branch lengths of all the edges in the tree, once tree has been rotated alphabetically",
        allowsNonultrametricTrees = true,
        allowsPolytomies = false,
        allowsUnrootedTrees = false)
public class BranchLengths extends AbstractTreeSummaryStatistic<Double> {

    String[] statsNames;
    String[] cladeStrings;

    @Override
    public String getStatisticLabel(Tree tree, int i) {
        if (statsNames == null) initStatsNames(tree);
        return statsNames[i];
    }

    @Override
    public Double[] getSummaryStatistic(Tree tree) {

        if (statsNames == null) initStatsNames(tree);
        Double[] stats = new Double[tree.getNodeCount() - 1];

        for (Node node : tree.getNodesAsArray()) {

            if (!node.isRoot()) {
                List<String> leafSet = TreeUtils.getDescendantLeavesSortedList(tree, node);
                String key = Arrays.toString(leafSet.toArray());

                for (int j = 0; j < cladeStrings.length; j++) {
                    if (key.equals(cladeStrings[j])) {
                        stats[j] = node.getParent().getHeight() - node.getHeight();
                    }
                }
            }
        }
        return stats;
    }

    void initStatsNames(Tree tree) {
        TreeUtils.rotateTreeAlphabetically(tree.getRoot());
        Node[] nodes = tree.listNodesPostOrder(null, null);

        statsNames = new String[nodes.length - 1];
        cladeStrings = new String[nodes.length - 1];

        for (int j = 0; j < statsNames.length; j++) {
            if (nodes[j].isLeaf()) statsNames[j] = nodes[j].getID();
            else statsNames[j] = "Node " + nodes[j].getNr();
            List<String> leafSet = TreeUtils.getDescendantLeavesSortedList(tree, nodes[j]);
            cladeStrings[j] = Arrays.toString(leafSet.toArray());
        }
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