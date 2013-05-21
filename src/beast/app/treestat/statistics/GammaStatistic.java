/*
 * GammaStatistic.java
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
import beast.evolution.tree.TreeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexei Drummond
 */
@Citation(value="Pybus and Harvey (2000)")
@SummaryStatisticDescription(
        name="Gamma",
        description = "The gamma-statistic is a summary of the information contained in the inter-node " +
                "intervals of a phylogeny; under the assumption that the clade diversified " +
                "with constant rates, it follows a normal distribution with mean of zero and a standard-deviation " +
                "of one " +
                "(Pybus and Harvey 2000). Thus, the null hypothesis that the clade diversified with constant " +
                "rates may be tested with 1 -  2*pnorm(abs(gamma.stat(phy))) for a two-tailed test, " +
                "or 1 - pnorm(abs(gamma.stat(phy))) for a one-tailed test, both returning the corresponding P-value.",
        category = SummaryStatisticDescription.Category.SPECIATION,
        allowsNonultrametricTrees = false,
        allowsPolytomies = true,
        allowsUnrootedTrees = false
)
public class GammaStatistic extends AbstractTreeSummaryStatistic<Double> {

    public Double[] getSummaryStatistic(Tree tree) {

        int n = tree.getLeafNodeCount();
        double[] g = TreeUtils.getIntervals(tree);

        double T = 0; // total branch length
        for (int j = 2; j <= n; j++) {
            T += j * g[j-2];
        }

        double sum = 0.0;
        for (int i = 2; i < n; i++) {
            for (int k = 2; k <= i; k++) {
                sum += k * g[k-2];
            }
        }

        double gamma = ((sum / (n-2.0)) - (T / 2.0)) / (T * Math.sqrt(1.0 / (12.0 * (n - 2.0))));
        return new Double[] { gamma };
    }
}
