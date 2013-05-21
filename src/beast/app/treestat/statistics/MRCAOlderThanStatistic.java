package beast.app.treestat.statistics;

import beast.evolution.tree.Tree;
import beast.evolution.tree.coalescent.TreeIntervals;

/**
 * @author Alexei Drummond
 */
@SummaryStatisticDescription(
        name = "MRCAOlderThan(t)",
        description = "True if MRCA older than given time, false otherwise",
        category = SummaryStatisticDescription.Category.POPULATION_GENETIC,
        allowsNonultrametricTrees = true,
        allowsPolytomies = true,
        allowsUnrootedTrees = false,
        allowsWholeTree = true,
        allowsTaxonList = false,
        allowsInteger = false,
        allowsDouble = true)
public class MRCAOlderThanStatistic extends AbstractTreeSummaryStatistic<Boolean> {

    public MRCAOlderThanStatistic() {
        this.t = 1.0;
    }

    public void setDouble(double value) {
        this.t = value;
    }

    public Boolean[] getSummaryStatistic(Tree tree) {

        try {
            TreeIntervals intervals = new TreeIntervals(tree);

            double totalTime = 0.0;
            for (int i = 0; i < intervals.getIntervalCount(); i++) {
                totalTime += intervals.getInterval(i);
                if (totalTime > t) {
                    return new Boolean[]{true};
                }
            }
            return new Boolean[]{false};
        } catch (Exception e) {
            return new Boolean[]{};

        }
    }

    public String getSummaryStatisticName() {
        return "MRCAOlderThan(" + t + ")";
    }

    public String getSummaryStatisticDescription() {
        return getSummaryStatisticName() + " is 1 if the MRCA of the genealogy older than " +
                "time " + t + ".";
    }

    public static final Factory FACTORY = new Factory() {

        public TreeSummaryStatistic createStatistic() {
            return new MRCAOlderThanStatistic();
        }

        public String getSummaryStatisticName() {
            return "MRCAOlderThan(t)";
        }

        public String getSummaryStatisticDescription() {
            return getSummaryStatisticName() + " is 1 if the MRCA of the genealogy older than " +
                    "time t.";
        }

        public String getSummaryStatisticReference() {
            return "-";
        }

        public String getValueName() {
            return "The time (t):";
        }
    };

    double t = 1.0;
}