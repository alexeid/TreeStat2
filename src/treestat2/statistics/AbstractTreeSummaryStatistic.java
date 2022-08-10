/*
 * AbstractTreeSummaryStatistic.java
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

import beast.base.core.BEASTObject;
import beast.base.evolution.tree.Tree;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Andrew Rambaut
 * @author Alexei Drummond
 * @version $Id: AbstractTreeSummaryStatistic.java,v 1.1 2005/09/28 13:50:55 rambaut Exp $
 */
public abstract  class AbstractTreeSummaryStatistic<T> extends BEASTObject implements TreeSummaryStatistic<T> {

    //public int getStatisticDimensions(Tree tree) {
    //    return 1;
    //}

    @Override
	public String getStatisticLabel(Tree tree, int i) {

        return getName();
    }

    @Override
	public Map<String,T> getStatistics(Tree tree) {
        Map<String,T> gs = new TreeMap<String,T>();

        T[] values = getSummaryStatistic(tree);

        for (int i = 0; i < values.length; i++) {
            gs.put(getStatisticLabel(tree, i), values[i]);
        }
        return gs;
    }

    /**
     * @return the value of this summary statistic for the given tree.
     */
    abstract T[] getSummaryStatistic(Tree tree);

    @Override
	public String getName() {
        return Utils.getDescription(this).name();
    }

    final String description() {
        return Utils.getDescription(this).description();
    }

    final boolean allowsWholeTree() {
        return Utils.getDescription(this).allowsWholeTree();
    }

    final boolean allowsInteger() {
        return Utils.getDescription(this).allowsInteger();
    }

    final boolean allowsDouble() {
        return Utils.getDescription(this).allowsDouble();
    }

    final boolean allowsString() {
        return Utils.getDescription(this).allowsString();
    }

    final boolean allowsPolytomies() {
        return Utils.getDescription(this).allowsPolytomies();
    }

    @Override
	public void setTaxonList(String name, Set<String> taxonList) {
        this.taxonList = taxonList;
        this.taxonListName = name;
    }

    public SummaryStatisticDescription getStatisticDescription() {
        return this.getClass().getAnnotation(SummaryStatisticDescription.class);
    }

    @Override
	public void setInteger(int value) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    @Override
	public void setDouble(double value) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    @Override
	public void setString(String value) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    @Override
    public void initAndValidate() {
    	// nothing to do
    }
    
    Set<String> taxonList = null;
    String taxonListName = null;
}
