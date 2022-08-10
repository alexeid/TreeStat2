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

package treestat2.statistics;


import beast.base.evolution.tree.Node;
import beast.base.evolution.tree.Tree;
import beast.base.evolution.tree.TreeUtils;

import java.util.Set;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name = "Monophyly",
        description = "1 if taxa are monophyletic.",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsTaxonList = true,
        allowsWholeTree = true)
public class MonophylyStatistic extends AbstractTreeSummaryStatistic<Double> {

    @Override
	public void setTaxonList(String name, Set<String> taxonList) {
        this.taxonListName = name;
        this.taxonList = taxonList;
    }

    @Override
	public Double[] getSummaryStatistic(Tree tree) {

        if (taxonList == null) {
            return new Double[]{1.0};
        }
        //try {

        Node node = TreeUtils.getCommonAncestorNode(tree, taxonList);

        if (node == null) throw new RuntimeException("No node found that is MRCA of " + taxonList);
        return new Double[]{TreeUtils.getDescendantLeaves(tree, node).size() == taxonList.size()? 1.0 : 0.0};
    }

    @Override
	public String getName() {
        if (characterState != null) {
            return super.getName() + characterState + ")";
        } else if (taxonList != null) {
            return super.getName() + "(" + taxonListName + ")";
        } else {
            return super.getName();
        }
    }

    public String getSummaryStatisticDescription() {
        if (characterState != null) {
            return "1 if " + characterState + " monophyletic.";
        } else if (taxonList != null) {
            return "1 if the given taxon list is monophyletic, 0 otherwise.";
        }
        return "1 if the set of taxa are monophyletic. In order to use this statistic, a taxon set must be defined (see the Taxon Set tab).";
    }

    private String characterState = null;
}

