/*
 * Nbar.java
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

import beast.core.Citation;
import beast.evolution.tree.Node;
import beast.evolution.tree.Tree;

/**
 * @author Alexei Drummond
 */
@Citation(value="Kirkpatrick & Slatkin (1992)")
@SummaryStatisticDescription(
        name = "N_bar",
        description = "The mean number of nodes above an external node.",
        category = SummaryStatisticDescription.Category.TREE_SHAPE,
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false)
public class Nbar extends AbstractTreeSummaryStatistic<Double> {

	public Double[] getSummaryStatistic(Tree tree) {
		double NBar = 0.0;
		for (Node node : tree.getExternalNodes()) {
			while (!node.isRoot()) {
				node = node.getParent();
				NBar += 1.0;
			}
		}
		return new Double[] { NBar / tree.getLeafNodeCount()};
	}
}
