open module treestat2 {
    requires beast.pkgmgmt;
    requires beast.base;
    requires ccd;
    requires beast.labs;
    requires jam;
    requires commons.math3;
    requires java.desktop;

    exports treestat2;
    exports treestat2.ccd;
    exports treestat2.statistics;

    provides beast.base.core.BEASTInterface with
        treestat2.TreeStat,
        treestat2.TreeStat2Logger,
        treestat2.statistics.B1Statistic,
        treestat2.statistics.BetaTreeDiversityStatistic,
        treestat2.statistics.BranchLengths,
        treestat2.statistics.BranchRates,
        treestat2.statistics.CCD0ExpectedRFDistance,
        treestat2.statistics.CCD0Information,
        treestat2.statistics.CCD0RFDistance,
        treestat2.statistics.CCD1ExpectedRFDistance,
        treestat2.statistics.CCD1Information,
        treestat2.statistics.CCD1RFDistance,
        treestat2.statistics.CCD2ExpectedRFDistance,
        treestat2.statistics.CCD2Information,
        treestat2.statistics.CCD2RFDistance,
        treestat2.statistics.CherryStatistic,
        treestat2.statistics.CladeMRCAAttributeStatistic,
        treestat2.statistics.CladeMeanAttributeStatistic,
        treestat2.statistics.CollessIndex,
        treestat2.statistics.DeltaStatistic,
        treestat2.statistics.ExternalBranchLengths,
        treestat2.statistics.ExternalBranchRates,
        treestat2.statistics.ExternalInternalRatio,
        treestat2.statistics.FuLiD,
        treestat2.statistics.GammaStatistic,
        treestat2.statistics.GetTypeChanges,
        treestat2.statistics.InternalBranchLengths,
        treestat2.statistics.InternalBranchRates,
        treestat2.statistics.InternalNodeAttribute,
        treestat2.statistics.IntervalKStatistic,
        treestat2.statistics.LineageCountStatistic,
        treestat2.statistics.LongestBranchLength,
        treestat2.statistics.LttSlopeRatio,
        treestat2.statistics.MRCAOlderThanStatistic,
        treestat2.statistics.MonophylyStatistic,
        treestat2.statistics.Nbar,
        treestat2.statistics.NodeHeights,
        treestat2.statistics.NumberOfTips,
        treestat2.statistics.RFJumpDistance,
        treestat2.statistics.RNNIJumpDistance,
        treestat2.statistics.RankBranchLength,
        treestat2.statistics.RelativeTrunkLength,
        treestat2.statistics.RootBranchTrait,
        treestat2.statistics.RootToTipLengths,
        treestat2.statistics.SAStatistic,
        treestat2.statistics.SamplingTimesInterval,
        treestat2.statistics.SecondInternalNodeHeight,
        treestat2.statistics.SingleChildCountStatistic,
        treestat2.statistics.SingleChildTransitionCounts,
        treestat2.statistics.SortedNodeAges,
        treestat2.statistics.TMRCASummaryStatistic,
        treestat2.statistics.TimeMaximumLineages,
        treestat2.statistics.TopologyStringStatistic,
        treestat2.statistics.TreeHeight,
        treestat2.statistics.TreeLength,
        treestat2.statistics.TreenessStatistic,
        treestat2.statistics.UniqueBranchTraitValues;
}
