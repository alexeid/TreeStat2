package treestat2.statistics;

import beast.base.evolution.tree.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author John Tay
 */

@SummaryStatisticDescription(
        name = "Second Internal Node Height",
        description = "The height of the second internal node of the tree.",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false)

public class SecondInternalNodeHeight extends AbstractTreeSummaryStatistic<Double> {
    @Override
    public Double[] getSummaryStatistic(Tree tree) {
        double second_branch_length = 0.0;
        if (tree.getRoot().getChildCount() < 3) {
            List<Double> child_list = new ArrayList<Double>();
            child_list.add(tree.getRoot().getChild(0).getLength());
            child_list.add(tree.getRoot().getChild(1).getLength());
            second_branch_length = Collections.min(child_list);
        } else {
            List<Double> child_list = new ArrayList<Double>();
            child_list.add(tree.getRoot().getChild(0).getLength());
            child_list.add(tree.getRoot().getChild(1).getLength());
            child_list.add(tree.getRoot().getChild(2).getLength());
            second_branch_length = Collections.min(child_list);
        }
        return new Double[]{tree.getRoot().getHeight() - second_branch_length};
    }
}
