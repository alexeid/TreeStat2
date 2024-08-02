package treestat2.ccd;

import beast.base.evolution.tree.Tree;
import ccd.model.CCD0;
import ccd.model.CCD1;
import ccd.model.WrappedBeastTree;

import java.util.List;

public class CCDHandler {

    final private CCD0 ccd0;
    final private CCD1 ccd1;
    final private WrappedBeastTree mapTreeCCD0;
    final private WrappedBeastTree mapTreeCCD1;
    private double burnin;

    public CCDHandler(List<Tree> trees, double burnin) {
        ccd0 = new CCD0(trees, burnin);
        // default "Min branch length 1, contemperaneous leaves"
        mapTreeCCD0 = new WrappedBeastTree(ccd0.getMAPTree());

        ccd1 = new CCD1(trees, burnin);
        mapTreeCCD1 = new WrappedBeastTree(ccd1.getMAPTree());

        this.burnin = burnin;
    }

    public WrappedBeastTree getMapTreeCCD0() {
        return mapTreeCCD0;
    }

    public WrappedBeastTree getMapTreeCCD1() {
        return mapTreeCCD1;
    }

    public CCD0 getCCD0() {
        return ccd0;
    }

    public CCD1 getCCD1() {
        return ccd1;
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
