package beast.app.treestat.statistics;

import beast.app.treestat.statistics.AbstractTreeSummaryStatistic;
import beast.app.treestat.statistics.SummaryStatisticDescription;
import beast.evolution.tree.Tree;
import beast.evolution.tree.TreeUtils;

@SummaryStatisticDescription(
        name = "Relative trunk length",
        description = "The ratio of the trunk length to the tree length",
        allowsNonultrametricTrees = true,
        allowsPolytomies = false,
        allowsUnrootedTrees = false
)
public class RelativeTrunkLength extends AbstractTreeSummaryStatistic<Double> {

    public Double[] getSummaryStatistic(Tree tree) {
        double treeLength = TreeUtils.getTreeLength(tree, tree.getRoot());
        return new Double[]{Double.valueOf(TreeUtils.getTrunkLength(tree, tree.getRoot()) / treeLength)};
    }
}
