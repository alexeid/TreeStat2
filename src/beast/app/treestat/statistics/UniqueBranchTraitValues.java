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

import java.util.*;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name="Unique Branch Trait Values",
        description="The (sorted) unique trait values of given name associated with the edges of the tree.",
        allowsNonultrametricTrees = true,
        allowsString = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false)
public class UniqueBranchTraitValues extends AbstractTreeSummaryStatistic<Object> {

    String traitName = "rate";

    public int getStatisticDimensions(Tree tree) {
        return uniqueTraits(tree).size();
    }

    public String getStatisticLabel(Tree tree, int i) {
        return traitName + " " + Integer.toString(i + 1);
    }

    public Double[] getSummaryStatistic(Tree tree) {

        return (Double[]) uniqueTraits(tree).toArray();
    }

    private SortedSet<Double> uniqueTraits(Tree tree) {

        SortedSet traits = new TreeSet<>();

        Node[] nodes = tree.getNodesAsArray();
        for (Node node : nodes) {
            Object traitValue =  node.getMetaData(traitName);
            if (traitValue != null) {
                traits.add(traitValue);
            }
        }

        return traits;
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
        traitName = value;
    }

    public static final Factory FACTORY = new Factory() {

        public TreeSummaryStatistic createStatistic() {
            return new UniqueBranchTraitValues();
        }

        public String getSummaryStatisticName() {
            return "Unique Branch Rates";
        }

        public String getSummaryStatisticDescription() {

            return "The (sorted) unique rates found on the branches of the tree.";
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