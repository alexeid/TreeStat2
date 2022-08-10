/*
 * BetaTreeDiversityStatistic.java
 *
 * Copyright (C) 2002-2011 Alexei Drummond and Andrew Rambaut
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
import beast.base.evolution.tree.TreeParser;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name="Beta Diversity",
        description="The ratio of shared diversity to total diversity (tree length) between given taxa and the remainder.",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsWholeTree =true,
        allowsCharacter = false,
        allowsCharacterState= false,
        allowsTaxonList = true)
public class BetaTreeDiversityStatistic extends AbstractTreeSummaryStatistic<Double> {

    @Override
	public Double[] getSummaryStatistic(Tree tree) {
        if (taxonList == null) {
            return new Double[]{1.0};
        }

        double TL = TreeUtils.getTreeLength(tree, tree.getRoot());

        double betaDiversity = (TL - getUniqueBranches(tree, tree.getRoot())) / TL;

        return new Double[]{betaDiversity};

    }

    private double getUniqueBranches(Tree tree, Node node) {

        if (node.isLeaf()) {
            return node.getLength();
        } else {
            double length = 0;
            if (isUnique(taxonList, tree, node)) {
                length = node.getLength();
            }
            for (Node child : node.getChildren()) {
                length += getUniqueBranches(tree, child);
            }
            return length;
        }
    }

    private boolean isUnique(Set<String> taxonList, Tree tree, Node node) {
        Set<String> taxa = TreeUtils.getDescendantLeaves(tree, node);
        int count = 0;
        for (String taxon : taxa) {
            count += taxonList.contains(taxon) ? 1 : 0;
        }
        boolean unique = (count == 0) || (count == taxa.size());

        return unique;
    }

    public String getSummaryStatisticName() {
        if (taxonList != null) {
            return "betaDiversity(" + taxonListName + ")";
        } else {
            return "betaDiversity(null)";
        }
    }

    public String getSummaryStatisticDescription() {
        return "The beta diversity of the given taxonList assuming the remaining taxa are from the second location.";
    }

    public static void main(String[] arg) throws Exception {

        Tree tree = new TreeParser("((A:1,B:1):1,(C:1, D:1):1);");

        BetaTreeDiversityStatistic statistic = new BetaTreeDiversityStatistic();

        Set<String> taxa = new TreeSet<String>();
        taxa.add("A");
        taxa.add("C");

        statistic.setTaxonList("AC", taxa);

        System.out.println(statistic.getSummaryStatistic(tree)[0]);
    }
}

