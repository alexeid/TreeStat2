/*
 * TreenessStatistic.java
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

import beast.base.core.Citation;
import beast.base.evolution.tree.Tree;
import beast.base.evolution.tree.TreeUtils;

/**
 * @author Alexei Drummond
 */
@Citation("Phillips and Penny (2001)")
@SummaryStatisticDescription(
        name = "Treeness",
        description = "The proportion of the total length of the tree that is taken up by internal branches. " +
                "Interpreted as a signal/(signal+noise) measure for phylogenetic reconstruction.",
        category = SummaryStatisticDescription.Category.PHYLOGENETIC,
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = true)
public class TreenessStatistic extends AbstractTreeSummaryStatistic<Double> {

    @Override
    public Double[] getSummaryStatistic(Tree tree) {

        double externalLength = TreeUtils.getExternalLength(tree);
        double internalLength = TreeUtils.getInternalLength(tree);
        return new Double[]{internalLength / (internalLength + externalLength)};
    }
}
