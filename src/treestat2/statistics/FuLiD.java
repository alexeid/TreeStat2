/*
 * FuLiD.java
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
@Citation(value="Fu and Li (1993)")
@SummaryStatisticDescription(
        name="Fu & Li's D",
        description = "The normalized difference between total tree length and external branch tree length. " +
                "Note that this implementation uses branch lengths rather than the counts of mutations (this " +
                "has a nice side-effect of taking into account complex mutation models.)",
        category = SummaryStatisticDescription.Category.POPULATION_GENETIC,
        allowsNonultrametricTrees = false,
        allowsPolytomies = false,
        allowsUnrootedTrees = false)
public class FuLiD extends AbstractTreeSummaryStatistic<Double> {

	@Override
	public Double[] getSummaryStatistic(Tree tree) {

		double externalLength = TreeUtils.getExternalLength(tree);
		double internalLength = TreeUtils.getInternalLength(tree);

        int n = tree.getLeafNodeCount();

		double total = externalLength + internalLength;

		// difference in expectations
		double D = total - a(n)*externalLength;

		// normalized
		D /= Math.sqrt(u(n)*total + (v(n)*(total*total)));

		return new Double[] { D };
	}

	private double a(int n) {
		double a = 0.0;
		for (int k = 1; k < n; k++) {
			a += 1/(double)k;
		}
		return a;
	}

	private double b(int n) {
		double b = 0.0;
		for (int k = 1; k < n; k++) {
			b += 1/(double)(k*k);
		}
		return b;
	}

	private double c(int n) {

		if (n==2) return 1.0;

		double an = a(n);

		double c = 2.0 * (n * an - 2.0 * (n - 1.0));
		c /= (n-1)*(n-2);

		return c;
	}

	private double v(int n) {
		double an2 = a(n);
		an2 *= an2;

		double v = 1 + (an2/(b(n)+an2)) * (c(n)-((n+1)/(n-1)));

		return v;
	}

	private double u(int n) {
		return a(n) - 1 - v(n);
	}
}
