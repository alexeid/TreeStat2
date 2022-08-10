package treestat2.statistics;

import beast.base.evolution.tree.Tree;
import beast.base.evolution.tree.TreeIntervals;

/**
 * Returns the number of lineages that exist at time t.
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name = "LineageCount(t)",
        description = "The number of lineages in the tree at time t (measured back from most recent tip).",
        category = SummaryStatisticDescription.Category.POPULATION_GENETIC,
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsTaxonList = false,
        allowsInteger = false,
        allowsDouble = true)
public class LineageCountStatistic extends AbstractTreeSummaryStatistic<Integer> {

    @Override
	public void setDouble(double value) {
        this.t = value;
    }

	@Override
	public Integer[] getSummaryStatistic(Tree tree) {

        try {
            TreeIntervals intervals = new TreeIntervals(tree);
            double totalTime = 0.0;
            for (int i = 0; i < intervals.getIntervalCount(); i++) {
                totalTime += intervals.getInterval(i);
                if (totalTime > t) {
                    return new Integer[] { intervals.getLineageCount(i) };
                }
            }
            return new Integer[] { 1 };
        } catch (Exception e) {
            return new Integer[] {};
        }
	}

	public String getSummaryStatisticName() {
        return "LineageCount(" + t + ")";
    }

	public String getSummaryStatisticDescription() {
        return getSummaryStatisticName() + " is the number of lineages that exists in the genealogy at " +
            "time " + t + ".";
    }

	public static final Factory FACTORY = new Factory() {

		@Override
		public String getValueName() { return "The time (t):"; }
    };

	double t = 1.0;
}
