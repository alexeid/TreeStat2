package beast.app.treestat.statistics;

import beast.evolution.tree.Tree;
import beast.evolution.tree.coalescent.TreeIntervals;


/**
 * Returns the time to the maximum number of lineages.
 * @author Sebastian Duchene
 */
@SummaryStatisticDescription(
        name = "Time of maximum lineages",
        description = "The time to the maximum number of lineages (measured back from most recent tip).",
        category = SummaryStatisticDescription.Category.PHYLOGENETIC,
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsTaxonList = false,
        allowsInteger = false
        )
//        allowsDouble = true)
public class TimeMaximumLineages extends AbstractTreeSummaryStatistic<Double> {
	
	@Override
	public Double[] getSummaryStatistic(Tree tree) {

        try {
            TreeIntervals intervals = new TreeIntervals(tree);
            Double[] intervalTimes = new Double[intervals.getIntervalCount()];
            Double[] lineagesPerInterval = new Double[intervals.getIntervalCount()];
            for (int i = 0; i < intervals.getIntervalCount(); i++) {
                intervalTimes[i] = intervals.getIntervalTime(i);
                lineagesPerInterval[i] = intervals.getLineageCount(i) + 0.0;
            }
            
            Double maxLineages = lineagesPerInterval[0];
            Double maxTimeLineages = 0.0;
            for(int i = 0; i < intervalTimes.length; i++){
            	if(lineagesPerInterval[i] > maxLineages){
            		maxLineages = lineagesPerInterval[i];
            		maxTimeLineages = intervalTimes[i];
            	}

            }
            
            return new Double[] {maxTimeLineages};

        } catch (Exception e) {
            return new Double[] {};
        }
	}
}