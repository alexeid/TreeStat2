package treestat2.statistics;

import beast.base.evolution.tree.Tree;
import beastlabs.evolution.tree.RNNIMetric;
import ccd.model.WrappedBeastTree;
import treestat2.TreeStatUtils;

/**
 * @author Lars Berling
 */
@SummaryStatisticDescription(
        name = "RNNI jump distance trace",
        description = "The Ranked NNI (RNNI) distance trace from a fixed tree to all other trees.",
        category = SummaryStatisticDescription.Category.BAYESIAN_PHYLOGENETIC)
public class RNNIJumpDistance extends AbstractTreeSummaryStatistic<Integer> {
    @Override
    public Integer[] getSummaryStatistic(Tree tree) {

        // TODO Figure out how to do this...

        Tree fixed_tree = tree.copy();

        RNNIMetric rnniMetric = new RNNIMetric(tree.getTaxaNames());

        int rnni = (int) rnniMetric.distance(tree, fixed_tree);

        return new Integer[]{rnni};
    }
}
