/*
 * InternalNodeAttribute.java
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

import beast.evolution.tree.Tree;
import beast.evolution.tree.TreeUtils;

import java.util.Set;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name="Tree Topology",
        description = "The tree topology in sorted newick format",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false)
public class TopologyStringStatistic extends AbstractTreeSummaryStatistic<String> {

    @Override
	public String[] getSummaryStatistic(Tree tree) {

        return new String[] {TreeUtils.sortedNewickTopology(tree.getRoot(), true)};
    }

    @Override
	public void setTaxonList(String name, Set<String> taxonList) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }
}