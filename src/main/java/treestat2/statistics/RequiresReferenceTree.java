package treestat2.statistics;

import beast.base.evolution.tree.Tree;

public interface RequiresReferenceTree {
    int getReferenceTreeIndex();

    void setFixedReferenceTree(Tree fixedReferenceTree);
}
