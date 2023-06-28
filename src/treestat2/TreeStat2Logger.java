package treestat2;


import beast.base.core.*;
import beast.base.core.Input.Validate;
import beast.base.evolution.tree.Tree;
import beast.base.inference.CalculationNode;
import treestat2.statistics.ExternalInternalRatio;
import treestat2.statistics.GetTypeChanges;
import treestat2.statistics.SecondInternalNodeHeight;
import treestat2.statistics.TreeHeight;

import java.io.PrintStream;

/**
 * @author John Tay
 */
@Description("Logger to report statistics of a tree")
public class TreeStat2Logger extends CalculationNode implements Loggable, Function {
    final public Input<Tree> treeInput = new Input<>("tree", "tree to report statistic for.", Validate.REQUIRED);
    final public Input<Boolean> TreeHeightInput = new Input<>("TreeHeight", "If true, tree height will be logged.", false);
    final public Input<Boolean> SecondInternalNodeHeightInput = new Input<>("SecondInternalNodeHeight", "If true, second internal node height will be logged.", false);
    final public Input<Integer> GetTypeChangesInput = new Input<>("GetTypeChanges", "Number of types for type changes logging.",0, Validate.REQUIRED);
    final public Input<Boolean> ExternalInternalRatioInput = new Input<>("ExternalInternalRatio", "If true, xxternal to internal branch length ratio will be logged.", false);

    boolean TreeHeight;
    boolean secondInternalNodeHeight;
    int getTypeChanges;
    boolean externalInternalRatio;

    @Override
    public void initAndValidate() {

        // This confusing line is because the default situation is to log
        // the tree height: we want the height to be logged only if neither of
        // these defaults is altered.

        TreeHeight = TreeHeightInput.get();
        secondInternalNodeHeight = SecondInternalNodeHeightInput.get();
        getTypeChanges = GetTypeChangesInput.get();
        externalInternalRatio = ExternalInternalRatioInput.get();

        if (!TreeHeight) {
            Log.warning.println("TreeStat2Logger " + getID() + " logs nothing. Set TreeHeight=true or at least something");
        }

    	if (!secondInternalNodeHeight) {
    		Log.warning.println("TreeStat2Logger " + getID() + " logs nothing. Set SecondInternalNodeHeight=true or at least something");
    	}

        if (getTypeChanges<2) {
            Log.warning.println("TreeStat2Logger " + getID() + " logs nothing. Set GetTypeChanges=[number of types]");
        }

        if (!externalInternalRatio) {
            Log.warning.println("TreeStat2Logger " + getID() + " logs nothing. Set ExternalInternalRatio=true or at least something");
        }
    }

    @Override
    public void init(PrintStream out) {
        final Tree tree = treeInput.get();
        if (TreeHeight) {
            out.print(tree.getID() + ".TreeHeight\t");
        }
        if (secondInternalNodeHeight) {
            out.print(tree.getID() + ".SecondInternalNodeHeight\t");
        }
        if (getTypeChanges>1) {
            out.print(tree.getID() + ".GetTypeChanges\t");
        }
        if (externalInternalRatio) {
            out.print(tree.getID() + ".ExternalInternalRatio\t");
        }
    }

    @Override
    public void log(long sample, PrintStream out) {
        final Tree tree = treeInput.get();
        if (TreeHeight) {
            TreeHeight treeHeightCalculator = new TreeHeight();
            Double[] summaryStatistic = treeHeightCalculator.getSummaryStatistic(tree);
            double TreeHeightValue = summaryStatistic[0];
            out.print(TreeHeightValue + "\t");
        }
        if (secondInternalNodeHeight) {
            SecondInternalNodeHeight secondInternalNodeHeightCalculator = new SecondInternalNodeHeight();
            Double[] summaryStatistic = secondInternalNodeHeightCalculator.getSummaryStatistic(tree);
            double secondInternalNodeHeightValue = summaryStatistic[0];
            out.print(secondInternalNodeHeightValue + "\t");
        }
        if (getTypeChanges > 1) {
            GetTypeChanges getTypeChangesCalculator = new GetTypeChanges();
            getTypeChangesCalculator.setString(Integer.toString(getTypeChanges));
            String[] summaryStatistic = getTypeChangesCalculator.getSummaryStatistic(tree);
            String getTypeChangesOutput = summaryStatistic[0];
            out.print(getTypeChangesOutput + "\t");
        }
        if (externalInternalRatio) {
            ExternalInternalRatio externalInternalRatioCalculator = new ExternalInternalRatio();
            Double[] summaryStatistic = externalInternalRatioCalculator.getSummaryStatistic(tree);
            double secondInternalNodeHeightValue = summaryStatistic[0];
            out.print(secondInternalNodeHeightValue + "\t");
        }
    }


	@Override
    public void close(PrintStream out) {
        // nothing to do
    }

    @Override
    public int getDimension() {
        return 0;
    }

    @Override
    public double getArrayValue(int dim) {
        return 0;
    }
}
