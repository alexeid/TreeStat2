package beast.app.treestat.statistics;

import beast.app.treestat.statistics.AbstractTreeSummaryStatistic;
import beast.app.treestat.statistics.SummaryStatisticDescription;
import beast.app.treestat.statistics.SummaryStatisticDescription.Category;
import beast.core.Citation;
import beast.evolution.tree.Tree;

@Citation("Gavryushkina A, Welch D, Stadler T, Drummond AJ (2014) \nBayesian inference of sampled ancestor trees for epidemiology and fossil calibration. \nPLoS Comput Biol 10(12): e1003919. doi:10.1371/journal.pcbi.1003919")
@SummaryStatisticDescription(
        name = "Sampled ancestor count",
        description = "The number of internal sampled nodes.",
        category = Category.TREE_SHAPE,
        allowsNonultrametricTrees = true,
        allowsPolytomies = false,
        allowsUnrootedTrees = false
)
public class SAStatistic extends AbstractTreeSummaryStatistic<Integer> {

    @Override
	public Integer[] getSummaryStatistic(Tree tree) {
        tree.getDirectAncestorNodeCount();
        return new Integer[]{Integer.valueOf(tree.getDirectAncestorNodeCount())};
    }
}
