package beast.app.treestat.statistics;

import beast.evolution.tree.Tree;

/**
 * @author Sebastian Duchene
 */
@SummaryStatisticDescription(
        name = "Number of tips",
        description = "The number of tips in the tree.",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false)
public class NumberOfTips extends AbstractTreeSummaryStatistic<Double> {
	@Override
	public Double[] getSummaryStatistic(Tree tree) {
		return new Double[] { (double) tree.getLeafNodeCount() };
	}
}
