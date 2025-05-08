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

package treestat2.statistics;

import beast.base.core.Log;
import beast.base.evolution.tree.Node;
import beast.base.evolution.tree.Tree;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
public class UniqueBranchTraitValues extends AbstractTreeSummaryStatistic<Double> {

    String traitName = "rate";

    public int getStatisticDimensions(Tree tree) {
        return uniqueTraits(tree).size();
    }

    @Override
	public String getStatisticLabel(Tree tree, int i) {
        return traitName + " " + Integer.toString(i + 1);
    }

    @Override
	public Double[] getSummaryStatistic(Tree tree) {

        Set<Double> uniqueValues = uniqueTraits(tree);
        Double[] values = new Double[uniqueValues.size()];
        return uniqueValues.toArray(values);
    }

    private SortedSet<Double> uniqueTraits(Tree tree) {

        SortedSet<Double> traits = new TreeSet<>();

        Node[] nodes = tree.getNodesAsArray();
        for (Node node : nodes) {
            Object traitValue = node.getMetaData(traitName);
            Double val = 0.0;
            if (traitValue != null) {
                if (traitValue instanceof Double) {
                    val = (Double)traitValue;
                } else if (traitValue instanceof String) {
                    try {
                        val = Double.parseDouble((String)traitValue);
                    } catch (NumberFormatException e) {
                        Log.warning("trait " + traitName + " on node " + node.getID() + "/" + node.getNr() + " could not be parsed to a double. Setting to zero.");
                    }
                }
                traits.add(val);
            }
        }

        return traits;
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
        traitName = value;
    }

    public static final Factory FACTORY = new Factory() {

        @Override
		public TreeSummaryStatistic<?> createStatistic() {
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