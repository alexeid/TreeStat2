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

import java.util.*;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name = "tMRCA",
        description = "The time of the most recent common ancestor.",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsTaxonList = true,
        allowsWholeTree = true)
public class TMRCASummaryStatistic extends AbstractTreeSummaryStatistic<Double> {

    private TMRCASummaryStatistic() {
        this.taxonList = null;
    }

    public void setTaxonList(String name, Set<String> taxonList) {
        this.taxonListName = name;
        this.taxonList = taxonList;
    }

    public Double[] getSummaryStatistic(Tree tree) {
        if (taxonList == null) {
            return new Double[]{tree.getRoot().getHeight()};
        }
        //try {
        Node node = TreeUtils.getCommonAncestorNode(tree, taxonList);
        if (node == null) throw new RuntimeException("No node found that is MRCA of " + taxonList);
        return new Double[]{node.getHeight()};
        //} catch (Tree.MissingTaxonException e) {
        //	throw new RuntimeException("Missing taxon!");
        //}
    }

    public String getSummaryStatisticName() {
        if (characterState != null) {
            return "tMRCA(" + characterState + ")";
        } else if (taxonList != null) {
            return "tMRCA(" + taxonListName + ")";
        } else {
            return "tMRCA";
        }
    }

    public String getSummaryStatisticDescription() {
        if (characterState != null) {
            return "The time of the most recent common ancestor of the character state " + characterState;
        } else if (taxonList != null) {
            return "The time of the most recent common ancestor of the given taxon list";
        }
        return "The time of the most recent common ancestor of a set of taxa. In order to use this statistic, a taxon set must be defined (see the Taxon Set tab).";
    }

    private String characterState = null;
}

