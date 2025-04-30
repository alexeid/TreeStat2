package treestat2;

import treestat2.statistics.*;

import java.util.ArrayList;
import java.util.List;

public class TreeStatisticRegistry {

    private static final List<Class<? extends TreeSummaryStatistic>> statistics = new ArrayList<>();

    static {
        // add generic tree statistics here
        statistics.add(TreeLength.class);
        statistics.add(RelativeTrunkLength.class);
        statistics.add(TreeHeight.class);
        statistics.add(NodeHeights.class);
        statistics.add(SortedNodeAges.class);
        statistics.add(BranchLengths.class);
        statistics.add(BranchRates.class);
        statistics.add(InternalBranchLengths.class);
        statistics.add(InternalBranchRates.class);
        statistics.add(ExternalBranchLengths.class);
        statistics.add(ExternalBranchRates.class);

        statistics.add(RootBranchTrait.class);
        statistics.add(UniqueBranchTraitValues.class);
        statistics.add(InternalNodeAttribute.class);
        statistics.add(RootToTipLengths.class);
        statistics.add(TMRCASummaryStatistic.class);
        statistics.add(MonophylyStatistic.class);

        statistics.add(CladeMRCAAttributeStatistic.class);
        statistics.add(CladeMeanAttributeStatistic.class);
        statistics.add(BetaTreeDiversityStatistic.class);
        statistics.add(TopologyStringStatistic.class);
        statistics.add(TimeMaximumLineages.class);
        statistics.add(SamplingTimesInterval.class);
        statistics.add(LttSlopeRatio.class);
        statistics.add(NumberOfTips.class);

        statistics.add(B1Statistic.class);
        statistics.add(CollessIndex.class);
        statistics.add(CherryStatistic.class);
        statistics.add(SingleChildCountStatistic.class);
        statistics.add(SAStatistic.class);
        statistics.add(SingleChildTransitionCounts.class);
        statistics.add(Nbar.class);
        statistics.add(TreenessStatistic.class);
        statistics.add(GammaStatistic.class);
        statistics.add(DeltaStatistic.class);
        statistics.add(ExternalInternalRatio.class);
        statistics.add(FuLiD.class);
        statistics.add(RankBranchLength.class);
        statistics.add(IntervalKStatistic.class);
        statistics.add(LineageCountStatistic.class);
        statistics.add(MRCAOlderThanStatistic.class);
        statistics.add(LongestBranchLength.class);
        statistics.add(SecondInternalNodeHeight.class);
        statistics.add(GetTypeChanges.class);

        // CCD...
        statistics.add(CCD0RFDistance.class);
        statistics.add(CCD1RFDistance.class);
        statistics.add(CCD0ExpectedRFDistance.class);
        statistics.add(CCD1ExpectedRFDistance.class);
        statistics.add(CCD0Information.class);
        statistics.add(CCD1Information.class);
    }

    public static List<Class<? extends TreeSummaryStatistic>> getAvailableStatistics() {
        return statistics;
    }

    public static void printAvailableOptions() {
        System.out.println("Available control file options:");

        List<String> names = TreeStatisticRegistry.getAvailableStatistics().stream()
                .map(Class::getSimpleName)
                .sorted()
                .toList();

        for (String name : names) {
            System.out.println("  " + name);
        }
    }

}
