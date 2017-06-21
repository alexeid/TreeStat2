package beast.app.treestat.statistics;

import beast.core.Citation;
import beast.evolution.tree.Tree;
import beast.evolution.tree.coalescent.TreeIntervals;

/**
 * Calculates the two slopes for the tree taking the lineages-through time plot.
 *  The first slope considers two points, the root of the tree and the maximum number of 
 *  lineages. The first slope is:
 *  (maximum number of lineages - 1) / (time to maximum number of lineages)
 *  The second slope is:
 *  (maximum number of lineages - number of lineages at the most recent tip) / 
 *  (age of youngest tip - time to maximum number of lineages)
 * @author Sebastian Duchene
 */
@Citation(value="Saulnier et al. 2017")
@SummaryStatisticDescription(
        name = "Slope ratio for LTT plot",
        description = "Ratio of slope1 and slope2 in the lineages through time plot.",
        category = SummaryStatisticDescription.Category.PHYLOGENETIC,
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsTaxonList = false,
        allowsInteger = false
        )
public class LttSlopeRatio extends AbstractTreeSummaryStatistic<Double>{
		
	public Double[] getSummaryStatistic(Tree tree) {
		LineageCountStatistic lineageCountStat = new LineageCountStatistic();
		TimeMaximumLineages timeMaxLineagesStat = new TimeMaximumLineages();
		Double[] timeMaxLineages = timeMaxLineagesStat.getSummaryStatistic(tree);
		lineageCountStat.setDouble(timeMaxLineages[0]);
		Integer[] ml = lineageCountStat.getSummaryStatistic(tree);
		Double[] maxLineages = {(double) ml[0]};
		Double rootHeight = tree.getRoot().getHeight();
		Double youngestNodeAge = 0.0;
		Double initialNumLineages = 1.0; // A single lineage at the root.
		lineageCountStat.setDouble(0.0);
		Integer[] fnl = lineageCountStat.getSummaryStatistic(tree);
		Double[] finalNumLineages = {(double) fnl[0]}; 
		
		Double slope1 = (maxLineages[0] - initialNumLineages) / (rootHeight - timeMaxLineages[0]);
		Double slope2 = (maxLineages[0] - finalNumLineages[0]) / (timeMaxLineages[0] - youngestNodeAge);
		
		return new Double[] {slope1 / slope2};

	}
}