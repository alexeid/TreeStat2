/*
 * IntervalKStatistic.java
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
import beast.base.evolution.tree.TreeIntervals;

/**
 * Returns the total time in the genealogy in which exactly k lineages are present.
 *
 * @author Alexei Drummond
 * @version $Id: IntervalKStatistic.java,v 1.2 2005/09/28 13:50:56 rambaut Exp $
 */
@SummaryStatisticDescription(
        name = "TotalTime(k)",
        description = "The total amount of time in the genealogy that exactly k lineages exist.",
        category = SummaryStatisticDescription.Category.POPULATION_GENETIC,
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsTaxonList = false,
        allowsInteger = true,
        allowsDouble = false)
public class IntervalKStatistic extends AbstractTreeSummaryStatistic<Double> {

    @Override
	public void setInteger(int value) {
        this.k = value;
    }

    @Override
	public Double[] getSummaryStatistic(Tree tree) {

        double totalTime = 0.0;
        try {
            TreeIntervals intervals = new TreeIntervals(tree);

            int intervalCount = intervals.getIntervalCount();
            for (int i = 0; i < intervalCount; i++) {
                if (intervals.getLineageCount(i) == k) {
                    totalTime += intervals.getInterval(i);
                }
            }
        } catch (Exception e) {
        }
        return new Double[]{totalTime};
    }

    public String getSummaryStatisticName() {
        return "TotalTime(" + k + ")";
    }

    public String getSummaryStatisticDescription() {
        return getSummaryStatisticName() + " is the total amount of time in the genealogy that " +
                "exactly " + k + " lineages exist.";
    }

    public static final TreeSummaryStatistic.Factory FACTORY = new TreeSummaryStatistic.Factory() {

        @Override
		public String getValueName() {
            return "The number of lineages (k):";
        }
    };

    int k = 2;
}
