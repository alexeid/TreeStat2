/*
 * TreeSummaryStatistic.java
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

import beast.evolution.tree.Tree;

import java.util.Map;
import java.util.Set;

/**
 * An interface and collection of tree summary statistics.
 *
 * @version $Id: TreeSummaryStatistic.java,v 1.2 2005/09/28 13:50:56 rambaut Exp $
 *
 * @author Alexei Drummond
 */
public interface TreeSummaryStatistic<T> {

	String getStatisticLabel(Tree tree, int i);

    void setTaxonList(String name, Set<String> taxonList);

    void setInteger(int value);
    void setDouble(double value);
    void setString(String value);

    String getName();

    /**
     * @return the name=value pairs of this summary statistic for the given tree.
     */
    public Map<String,T> getStatistics(Tree tree);

    /**
     * Return the dimension of this statistic for the given tree
     * @param tree
     * @return
     */
    default int getDimension(Tree tree) {
        return getStatistics(tree).keySet().size();
    }

    public class Utils {
        public static SummaryStatisticDescription getDescription(TreeSummaryStatistic<?> tss) {
            return tss.getClass().getAnnotation(SummaryStatisticDescription.class);
        }
    }

    public abstract class Factory {
		public TreeSummaryStatistic<?> createStatistic() {
			throw new RuntimeException("This factory method is not implemented");
		}

		public boolean allowsWholeTree() { return true; }
		public boolean allowsTaxonList() { return false; }
		public boolean allowsInteger() { return false; }
		public boolean allowsDouble() { return false; }
        public boolean allowsString() { return false; }

		public String getValueName() { return ""; }

    }

    static SummaryStatisticDescription getSummaryStatisticDescription(Class<? extends TreeSummaryStatistic> tssClass) {
        return tssClass.getAnnotation(SummaryStatisticDescription.class);
    }
}

