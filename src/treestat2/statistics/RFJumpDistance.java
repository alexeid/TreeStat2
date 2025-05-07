package treestat2.statistics;

import beast.base.evolution.tree.Tree;
import ccd.algorithms.TreeDistances;
import ccd.model.WrappedBeastTree;

/**
 * @author Lars Berling
 */
@SummaryStatisticDescription(
        name = "RF jump distance trace",
        description = "The Robinson Foulds (RF) distance trace from a fixed tree to all other trees.",
        category = SummaryStatisticDescription.Category.BAYESIAN_PHYLOGENETIC,
        allowsInteger = true)
public class RFJumpDistance extends AbstractTreeSummaryStatistic<Integer> implements RequiresReferenceTree {

    private int fixedTreeIndex = 0;

    private Tree fixedReferenceTree;

    @Override
    Integer[] getSummaryStatistic(Tree tree) {
        if (fixedReferenceTree == null) {
            throw new IllegalStateException("Reference tree was not set in " + getClass().getSimpleName());
        }

        int rf = TreeDistances.robinsonsFouldDistance(new WrappedBeastTree(tree), new WrappedBeastTree(fixedReferenceTree));

        return new Integer[]{rf};
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
}
