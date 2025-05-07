package treestat2.ccd;

import beast.base.core.Log;
import beast.base.evolution.tree.Tree;
import ccd.model.*;
import treestat2.statistics.CCDStats;

import java.util.ArrayList;
import java.util.List;

public class CCDHandler {

    final private CCD0 ccd0;
    final private CCD1 ccd1;
    final private CCD2 ccd2; //TODO bug ?
    final private WrappedBeastTree mapTreeCCD0;
    final private WrappedBeastTree mapTreeCCD1;
    final private WrappedBeastTree mapTreeCCD2;

    public CCDHandler(List<Tree> trees, double burnin) {

        System.out.println("CCDHandler.CCDHandler:");
        System.out.println("burnin = " + burnin);

        double adjustedBurnin = burnin;
        if (burnin < 0.1) {
            Log.warning("[TreeStatApp] CCD0 construction: Burn-in value too low (" + burnin + "), adjusted to 0.1.");
            adjustedBurnin = 0.1;
        }
        ccd0 = new CCD0(trees, adjustedBurnin);
        // default "Min branch length 1, contemporaneous leaves"
        mapTreeCCD0 = new WrappedBeastTree(ccd0.getMAPTree());

        ccd1 = new CCD1(trees, burnin);
        mapTreeCCD1 = new WrappedBeastTree(ccd1.getMAPTree());

        ccd2 = new CCD2(trees, burnin);
        mapTreeCCD2 = new WrappedBeastTree(ccd2.getMAPTree());

    }

    public WrappedBeastTree getMapTreeCCD0() {
        return mapTreeCCD0;
    }

    public WrappedBeastTree getMapTreeCCD1() {
        return mapTreeCCD1;
    }

    public WrappedBeastTree getMapTreeCCD2() {
        return mapTreeCCD2;
    }

    public CCD0 getCCD0() {
        return ccd0;
    }

    public CCD1 getCCD1() {
        return ccd1;
    }

    public CCD2 getCCD2() {
        return ccd2;
    }

    public AbstractCCD getCCD(CCDStats.Model ccdModel) {
        if (CCDStats.Model.CCD0.equals(ccdModel))
            return getCCD0();
        else if (CCDStats.Model.CCD1.equals(ccdModel))
            return getCCD1();
        else
            throw new IllegalArgumentException("CCD model (" + ccdModel + ") not supported !");
    }

    public List<String> getCCDSummary() {
        List<String> summary = new ArrayList<>();
        summary.add("Models\tEntropy\tp(MAPTree)\tClades\tPartitions\tLeafNodes");
        summary.add("CCD0\t" + ccd0.getEntropy() + "\t" + ccd0.getMaxTreeProbability() +
                "\t" + ccd0.getNumberOfClades() + "\t" + ccd0.getNumberOfCladePartitions() +
                "\t" + ccd0.getNumberOfLeaves());
        summary.add("CCD1\t" + ccd1.getEntropy() + "\t" + ccd1.getMaxTreeProbability() +
                "\t" + ccd1.getNumberOfClades() + "\t" + ccd1.getNumberOfCladePartitions() +
                "\t" + ccd1.getNumberOfLeaves());
//        summary.add("CCD2\t" + ccd2.getEntropy() + "\t" + ccd2.getMaxTreeProbability());
        return summary;
    }

}
