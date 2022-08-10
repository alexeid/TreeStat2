package treestat2.statistics;

import beast.base.evolution.tree.Tree;
import beast.base.evolution.tree.TreeUtils;
import treestat2.statistics.AbstractTreeSummaryStatistic;
import treestat2.statistics.SummaryStatisticDescription;

@SummaryStatisticDescription(
        name = "Relative trunk length",
        description = "The ratio of the trunk length to the tree length",
        allowsNonultrametricTrees = true,
        allowsPolytomies = false,
        allowsUnrootedTrees = false
)
public class RelativeTrunkLength extends AbstractTreeSummaryStatistic<Double> {

    @Override
	public Double[] getSummaryStatistic(Tree tree) {
        double treeLength = TreeUtils.getTreeLength(tree, tree.getRoot());
        return new Double[]{Double.valueOf(TreeUtils.getTrunkLength(tree, tree.getRoot()) / treeLength)};
    }
}
