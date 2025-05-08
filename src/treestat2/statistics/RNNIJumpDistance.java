package treestat2.statistics;

import beast.base.evolution.tree.Tree;
import beastlabs.evolution.tree.RNNIMetric;

/**
 * @author Lars Berling
 */
@SummaryStatisticDescription(
        name = "RNNI jump distance trace",
        description = "The Ranked NNI (RNNI) distance trace from a fixed tree to all other trees.",
        category = SummaryStatisticDescription.Category.BAYESIAN_PHYLOGENETIC,
        allowsInteger = true)
public class RNNIJumpDistance extends AbstractTreeSummaryStatistic<Integer> implements RequiresReferenceTree {

    private int fixedTreeIndex = 0;

    private Tree fixedReferenceTree;

    @Override
    public Integer[] getSummaryStatistic(Tree tree) {
        if (fixedReferenceTree == null) {
            throw new IllegalStateException("Reference tree was not set in " + getClass().getSimpleName());
        }

        RNNIMetric rnniMetric = new RNNIMetric(tree.getTaxaNames());

        int rnni = (int) rnniMetric.distance(tree, fixedReferenceTree);

        return new Integer[]{rnni};
    }

    @Override
    public void setInteger(int value) {
        this.fixedTreeIndex = value;
    }

    @Override
    public int getReferenceTreeIndex() {
        return fixedTreeIndex;
    }

    @Override
    public void setFixedReferenceTree(Tree fixedReferenceTree) {
        this.fixedReferenceTree = fixedReferenceTree;
    }

    @Override
    public String getStatisticLabel(Tree tree, int i) {
        SummaryStatisticDescription desc = this.getClass().getAnnotation(SummaryStatisticDescription.class);
        if (desc != null) {
            String name = desc.name();
            return name + " (" + fixedTreeIndex + ")";
        }
        // In case name is somehow not set?...
        return "RNNIJumpDistance(" + fixedTreeIndex + ")";
    }
}
