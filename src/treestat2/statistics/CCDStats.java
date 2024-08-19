package treestat2.statistics;

public interface CCDStats<T> extends TreeSummaryStatistic<T> {

    enum Model {
        CCD0,
        CCD1,
        CCD2
    }

    Model getCCDModel();
}
