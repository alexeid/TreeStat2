package treestat2.statistics;

import beast.base.evolution.tree.Node;
import beast.base.evolution.tree.Tree;

@SummaryStatisticDescription(
        name = "Sampling times interval",
        description = "The difference in ages between the youngest and oldest tips.",
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false)
public class SamplingTimesInterval extends AbstractTreeSummaryStatistic<Double> {
	@Override
	public Double[] getSummaryStatistic(Tree tree) {
		int nTips = tree.getLeafNodeCount();
		Double[] oldestTipAge = {0.0};
		Node [] nodes = tree.getNodesAsArray();
		for (int i = 0; i <  nTips; i++){
			Double newTipAge = nodes[i].getHeight();
			if (newTipAge > oldestTipAge[0]){
				oldestTipAge[0] = newTipAge;
			}
		}
		
		return oldestTipAge;
	}
}
