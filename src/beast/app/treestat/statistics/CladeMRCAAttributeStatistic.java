/*
 * TMRCASummaryStatistic.java
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

import beast.evolution.tree.Node;
import beast.evolution.tree.Tree;
import beast.evolution.tree.TreeUtils;

import java.util.Set;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name="Clade MRCA Attribute",
        description="Extracts a named attribute for the MRCA of a clade defined by a taxon set.",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsWholeTree = true,
        allowsTaxonList = true,
        allowsString = true)
public class CladeMRCAAttributeStatistic extends AbstractTreeSummaryStatistic<Double> {

    private CladeMRCAAttributeStatistic() {
        this.attributeName = "";
    }

    public void setString(String value) {
        this.attributeName = value;
    }


    public Double[] getSummaryStatistic(Tree tree) {
        Node node;
        if (taxonList == null) {
            node = tree.getRoot();
        } else {
            node = TreeUtils.getCommonAncestorNode(tree, taxonList);
            if (node == null) throw new RuntimeException("No clade found that contains " + taxonList);
        }
        Object item = node.getMetaData(attributeName);
        if (item == null) {
            throw new RuntimeException("Attribute, " + attributeName + ", missing from clade");
        }
        if (item instanceof Number) {
            return new Double[]{((Number) item).doubleValue()};
        }
        if (item instanceof Object[]) {
            Object[] array = (Object[]) item;
            Double[] values = new Double[array.length];
            for (int i = 0; i < array.length; i++) {
                values[i] = ((Number) array[i]).doubleValue();
            }
            return values;
        }
        if (item.toString().equals("true")) {
            return new Double[]{1.0};
        } else if (item.toString().equals("false")) {
            return new Double[]{0.0};
        }
        return null;
    }

    public String getSummaryStatisticName() {
        if (taxonList != null) {
            return attributeName + "(" + taxonListName + ")";
        } else {
            return attributeName + "(root)";
        }
    }

    public String getSummaryStatisticDescription() {
        if (taxonList != null) {
            return "Extracts a named attribute at the MRCA of a clade defined by a taxon set";
        }
        return "Extracts a named attribute at the root of the tree.";
    }

    public static final Factory FACTORY = new Factory() {

        public TreeSummaryStatistic createStatistic() {
            return new CladeMRCAAttributeStatistic();
        }

        public String getSummaryStatisticName() {
            return "Clade MRCA Attribute";
        }

        public String getSummaryStatisticDescription() {
            return "Extracts a named attribute for the MRCA of a clade defined by a taxon set";
        }

        public String getSummaryStatisticReference() {
            return "-";
        }

        public String getValueName() {
            return "The attribute name:";
        }
    };

    private String attributeName = null;
}