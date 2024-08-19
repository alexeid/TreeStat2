package treestat2.ccd;

import beast.base.evolution.tree.Tree;
import ccd.model.CCD0;
import ccd.model.CCD1;
import ccd.model.CCD2;
import ccd.model.WrappedBeastTree;

import java.util.ArrayList;
import java.util.List;

public class CCDHandler {

    final private CCD0 ccd0;
    final private CCD1 ccd1;
    final private CCD2 ccd2; //TODO bug ?
    final private WrappedBeastTree mapTreeCCD0;
    final private WrappedBeastTree mapTreeCCD1;
    final private WrappedBeastTree mapTreeCCD2;
    private double burnin;

    public CCDHandler(List<Tree> trees, double burnin) {
        ccd0 = new CCD0(trees, burnin);
        // default "Min branch length 1, contemperaneous leaves"
        mapTreeCCD0 = new WrappedBeastTree(ccd0.getMAPTree());

        ccd1 = new CCD1(trees, burnin);
        mapTreeCCD1 = new WrappedBeastTree(ccd1.getMAPTree());

        ccd2 = new CCD2(trees, burnin);
        mapTreeCCD2 = new WrappedBeastTree(ccd2.getMAPTree());

        this.burnin = burnin;
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

    public List<String> getCCDSummary() {
        List<String> summary = new ArrayList<>();
        summary.add("Models\tEntropy\tp(MAPTree)\tClades\tPartitions");
        summary.add("CCD0\t" + ccd0.getEntropy() + "\t" + ccd0.getMaxTreeProbability() +
                "\t" + ccd0.getNumberOfClades() + "\t" + ccd0.getNumberOfCladePartitions());
        summary.add("CCD1\t" + ccd1.getEntropy() + "\t" + ccd1.getMaxTreeProbability() +
                "\t" + ccd1.getNumberOfClades() + "\t" + ccd1.getNumberOfCladePartitions());
//        summary.add("CCD2\t" + ccd2.getEntropy() + "\t" + ccd2.getMaxTreeProbability());
        return summary;
    }



    //    public static WrappedBeastTree generateMAPTreeCCD0(List<Tree> trees, double burnin) {
//        CCD0 ccd0 = new CCD0(trees, burnin);
//        // default "Min branch length 1, contemperaneous leaves"
//        return new WrappedBeastTree(ccd0.getMAPTree());
//    }
//
//    public static WrappedBeastTree generateMAPTreeCCD1(List<Tree> trees, double burnin) {
//        CCD1 ccd1 = new CCD1(trees, burnin);
//        return new WrappedBeastTree(ccd1.getMAPTree());
//    }


}
