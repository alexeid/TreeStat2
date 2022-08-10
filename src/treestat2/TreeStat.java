package treestat2;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import beast.base.core.BEASTObject;
import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.core.Input.Validate;
import beast.base.core.Loggable;
import beast.base.evolution.tree.Tree;
import treestat2.statistics.AbstractTreeSummaryStatistic;

@Description("Logger for a tree statistic")
public class TreeStat extends BEASTObject implements Loggable {
	final public Input<Tree> treeInput = new Input<>("tree", "tree for which a statistic is logged", Validate.REQUIRED);
	final public Input<AbstractTreeSummaryStatistic<?>> statInput = new Input<>("state", "tree statistic to be logged", Validate.REQUIRED);

	Tree tree;
	AbstractTreeSummaryStatistic<?> stat;
	List<String> labels;
	
	@Override
	public void initAndValidate() {
		tree = treeInput.get();
		stat = statInput.get();
	}

	@Override
	public void init(PrintStream out) {
		Map<String, ?> map = stat.getStatistics(tree);
		labels = new ArrayList<>();
		labels.addAll(map.keySet());
		Collections.sort(labels);
		for (int i = 0; i < labels.size(); i++) {
			out.print(labels.get(i) + "\t");
		}
	}

	@Override
	public void log(long sample, PrintStream out) {
		Map<String, ?> map = stat.getStatistics(tree);
		for (int i = 0; i < labels.size(); i++) {
			out.print(map.get(labels.get(i)) + "\t");
		}
	}
	
	@Override
	public void close(PrintStream out) {
		// nothing to do
	}

}
