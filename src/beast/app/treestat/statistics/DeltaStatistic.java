/*
 * DeltaStatistic.java
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
import beast.evolution.tree.Tree;
import beast.evolution.tree.TreeUtils;
import beast.math.Binomial;

/**
 * @author Alexei Drummond
 */
@Citation(value="Pybus and Harvey (2000)")
@SummaryStatisticDescription(
        name="Delta",
        description = "The delta-statistic is a summary of the information contained in the inter-node " +
                "intervals of a genealogy; under the assumption of a constant-size population, " +
                "it follows a normal distribution with mean of zero and a standard-deviation " +
                "of one (Pybus and Harvey 2000). Thus, the null hypothesis that the population has a constant " +
                "population size may be tested with 1 -  2*pnorm(abs(delta.stat(phy))) for a two-tailed test, " +
                "or 1 - pnorm(abs(delta.stat(phy))) for a one-tailed test, both returning the corresponding P-value.",
        category = SummaryStatisticDescription.Category.POPULATION_GENETIC,
        allowsNonultrametricTrees = false,
        allowsPolytomies = true,
        allowsUnrootedTrees = false)

public class DeltaStatistic extends AbstractTreeSummaryStatistic<Double> {

    private DeltaStatistic() { }

    public Double[] getSummaryStatistic(Tree tree) {

        int n = tree.getLeafNodeCount();
        double[] g = TreeUtils.getIntervals(tree);

        double T = 0; // total branch length
        for (int j = 2; j <= n; j++) {
            T += Binomial.choose2(j) * g[j-2];
        }

        double sum = 0.0;
        for (int i = n; i > 2; i--) {
            for (int k = n; k >= i; k--) {
                sum += Binomial.choose2(k) * g[k-2];
            }
        }

        double delta = ((T / 2.0) - (sum * (1.0 / (n - 2.0)))) / (T * Math.sqrt(1.0/(12.0*(n - 2.0))));
        return new Double[] { delta };
    }
}
