/*
 * InternalBranchLengths.java
 *
 * Copyright (C) 2002-2009 Alexei Drummond and Andrew Rambaut
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
 * BEAST is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BEAST; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package treestat2.statistics;

import beast.base.evolution.tree.Node;
import beast.base.evolution.tree.Tree;

/**
 * @author John Tay
 */
@SummaryStatisticDescription(
        name = "Type Changes",
        description = "The counts of each type change",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsString = true,
        valueName = "Number of types:")
public class GetTypeChanges extends AbstractTreeSummaryStatistic<String> {

    String attributeName = "";

    @Override
    public String[] getSummaryStatistic(Tree tree) {

        int typecount = Integer.parseInt(attributeName);

        Integer[][] countmatrix = new Integer[typecount][typecount];
        for (int x = 0; x < countmatrix[0].length; x++) {
            for (int y = 0; y < countmatrix.length; y++) {
                countmatrix[x][y] = 0;
            }
        }

        for (Node node : tree.getInternalNodes()) {
            for (int child_index = 0; child_index < node.getChildCount(); child_index++) {
                String parent = node.toNewick();
                String child = node.getChild(child_index).toNewick();
                String fromType = parent.substring(parent.lastIndexOf("type"), parent.lastIndexOf("type") + 5);
                String toType = child.substring(child.lastIndexOf("type"), child.lastIndexOf("type") + 5);

                if (!fromType.equals(toType)) {
                    countmatrix[Integer.parseInt(String.valueOf(fromType.charAt(4)))][Integer.parseInt(String.valueOf(toType.charAt(4)))]++;
                }
            }
        }

        String countstring = convertArrayToString(countmatrix);
        return new String[]{countstring};
    }

    public static String convertArrayToString(Integer[][] array) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                sb.append(array[i][j]).append(" ");
            }
            sb.setLength(sb.length() - 1); // Remove the last space
            sb.append("_");
        }

        sb.setLength(sb.length() - 1); // Remove the last underscore

        return sb.toString();
    }

    @Override
    public String getName() {
        return super.getName() + "(" + attributeName + ")";
    }

    @Override
    public void setString(String name) {
        this.attributeName = name;
    }
}
