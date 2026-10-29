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
import ccd.model.CCD2;
import treestat2.TreeStatUtils;
import treestat2.ccd.CCDHandler;

/**
 * @author Walter Xie
 */
@SummaryStatisticDescription(
        name = "CCD2 information content (log(p))",
        description = "The information content of each sampled tree T of the sample " +
                "given by log(Pr(T)) where Pr(T) is the probability of T in the CCD2.",
        category = SummaryStatisticDescription.Category.BAYESIAN_PHYLOGENETIC,
        allowsNonultrametricTrees = true, // TODO
        allowsPolytomies = false, // TODO
        allowsUnrootedTrees = false)
public class CCD2Information extends AbstractTreeSummaryStatistic<Double> implements CCDStats<Double> {

    @Override
    public Double[] getSummaryStatistic(Tree tree) {

        CCDHandler ccdHandler = TreeStatUtils.getCCDHandler();
        CCD2 ccd2 = ccdHandler.getCCD2();

        double p = ccd2.getProbabilityOfTree(tree);
        double logp = -Math.log(p);

        return new Double[]{logp};
    }


    @Override
    public Model getCCDModel() {
        return Model.CCD2;
    }

}
