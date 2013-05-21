package beast.app.treestat.statistics;

import beast.evolution.tree.Tree;
import beast.evolution.tree.coalescent.TreeIntervals;

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

	public LineageCountStatistic() {
		this.t = 1.0;
	}

    public void setDouble(double value) {
        this.t = value;
    }

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

		public String getValueName() { return "The time (t):"; }
    };

	double t = 1.0;
}