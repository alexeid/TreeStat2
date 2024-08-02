/*
 * RankProportionStatistic.java
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

import beast.base.evolution.tree.Tree;
import ccd.algorithms.TreeDistances;
import ccd.model.AbstractCCD;
import ccd.model.WrappedBeastTree;
import treestat2.TreeStatUtils;
import treestat2.ccd.CCDHandler;

/**
 * @author Walter Xie
 */
@SummaryStatisticDescription(
        name = "CCD0 RF distance",
        description = "The Robinsons-Fould (RF) distance computed by using CCD0 (observed clades) point estimator for tree distribution.",
        category = SummaryStatisticDescription.Category.BAYESIAN_PHYLOGENETIC,
        allowsNonultrametricTrees = true, // TODO
        allowsPolytomies = false, // TODO
        allowsUnrootedTrees = false)
public class CCD0RFDistance extends AbstractTreeSummaryStatistic<Integer> {

    @Override
	public Integer[] getSummaryStatistic(Tree tree) {

        WrappedBeastTree mapTree = TreeStatUtils.getCCDHandler().getMapTreeCCD0();

        int rf = TreeDistances.robinsonsFouldDistance(new WrappedBeastTree(tree), mapTree);

        return new Integer[]{rf};
    }

}
