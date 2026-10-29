/*
 * CherryStatistic.java
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

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name = "Single child transition counts",
        description = "The number of each type of transition of the given attribute across single child nodes.",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsString = true,
        valueName = "Name of the attribute to count the transitions of:")
public class SingleChildTransitionCounts extends AbstractTreeSummaryStatistic<Integer> {

    String attributeName = "location";

    String[] labels = null;

    /**
     * @param tree the tree to summarize
     * @return a map of statistic names and values
     */
    @Override
    public Map<String, Integer> getStatistics(Tree tree) {

        Map<String, Integer> statistics = new TreeMap<String, Integer>();

        for (Node node : tree.getInternalNodes()) {
            if (node.getChildCount() == 1) {
                Object fromState = node.getMetaData(attributeName);
                Object toState = node.getChild(0).getMetaData(attributeName);
                String key = fromState + "->" + toState;
                Integer count = statistics.get(key);
                if (count == null) {
                    statistics.put(key, 1);
                } else {
                    statistics.put(key, count + 1);
                }
            }
        }
        return statistics;

//        Map<String,Integer> gs = new TreeMap<String, Integer>();
//        for (String key : statistics.keySet()) {
//            gs.put(key, statistics.get(key));
//        }
//        return gs;
    }

    @Override
    public String getName() {
        return super.getName() + "(" + attributeName + ")";
    }


    @Override
    Integer[] getSummaryStatistic(Tree tree) {
        throw new RuntimeException("Should not be used. getStatistics() does job in this class.");
    }

    @Override
    public void setString(String name) {
        this.attributeName = name;
    }
}
