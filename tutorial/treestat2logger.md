# Tutorial for using TreeStat2Logger
Written by John Tay 24 August 2023

Currently, only the following tree statistics are implemented in TreeStat2Logger:
ExternalInternalRatio, GetTypeChanges, SecondInternalNodeHeight, and TreeHeight.

## Example with GetTypeChanges on MTBD

These instructions will assist in setting up TreeStat2Logger a multitype birth-death model on some simulated data.
GetTypeChanges must take a discrete nominal input greater than 1.

### Requirements
Ensure that the following packages are located in your ~/.beast/2.7 directory:
BEAST2.7.x, bdmm, feast, MultiTypeTree, and TreeStat2.
Note that the tutorial was tested for BEAST2.7.3

### 
The following code logs the trace and statistics from the MCMC, as well as the number of type changes.
```
<logger id="tracelog" spec="Logger" fileName="updated_aln268.log" logEvery="1000">
  <log idref="posterior"/>
  <log idref="likelihood"/>
  <log idref="prior"/>
  <log idref="treeLikelihood.updated_aln268"/>  
  <log id="treeHeight.t:updated_aln268" spec="beast.base.evolution.tree.TreeHeightLogger" tree="@Tree.t:updated_aln268"/>
  <log id="treeLength.t:updated_aln268" spec="multitypetree.util.TreeLengthLogger" tree="@Tree.t:updated_aln268"/>
  <log id="changeCounts.t:updated_aln268" spec="multitypetree.util.TypeChangeCounts" migrationModel="@migModel.t:updated_aln268" multiTypeTree="@Tree.t:updated_aln268"/>
  <log id="nodeTypeCounts.t:updated_aln268" spec="multitypetree.util.NodeTypeCounts" migrationModel="@migModel.t:updated_aln268" multiTypeTree="@Tree.t:updated_aln268"/>
  <log id="rootTypeLogger.t:updated_aln268" spec="multitypetree.util.TreeRootTypeLogger" multiTypeTree="@Tree.t:updated_aln268"/>
  <log idref="R0.t:updated_aln268"/>
  <log idref="samplingProportion.t:updated_aln268"/>
  <log idref="rateMatrix.t:updated_aln268"/>
  <log idref="geo-frequencies.t:updated_aln268"/>
  <log idref="clockRate.c:updated_aln268"/>
  <log idref="gammaShape.s:updated_aln268"/>
  <log idref="kappa.s:updated_aln268"/>
  <log idref="freqParameter.s:updated_aln268"/>
  <log id="getTypeChanges" spec="treestat2.TreeStat2Logger" tree="@Tree.t:updated_aln268" GetTypeChanges="2" /> ### explanation below
</logger>
<logger id="screenlog" spec="Logger" logEvery="1000">
  <log idref="posterior"/>
  <log id="ESS.0" spec="util.ESS" arg="@posterior"/>
  <log idref="likelihood"/>
  <log idref="prior"/>
  <log idref="getTypeChanges" spec="treestat2.TreeStat2Logger" tree="@Tree.t:updated_aln268" GetTypeChanges="2" /> ### explanation below
</logger>
```

The following line assigns "getTypeChanges" for an analysis with 2 types, outputting to the trace-log file "updated_aln268.log":
```
  <log id="getTypeChanges" spec="treestat2.TreeStat2Logger" tree="@Tree.t:updated_aln268" GetTypeChanges="2" />
```
We now point to the type changes variable with idref="getTypeChanges":
```
  <log idref="getTypeChanges" spec="treestat2.TreeStat2Logger" tree="@Tree.t:updated_aln268" GetTypeChanges="2" />
```

### Note on other tree statistics
The other currently implemented tree statistics rely on a true/false check, starting initially as false.
The following examples will log the respective tree statistics:
```
<log id="externalInternalRatio" spec="treestat2.TreeStat2Logger" tree="@Tree.t:updated_aln268" ExternalInternalRatio=true />
<log id="secondInternalNodeHeight" spec="treestat2.TreeStat2Logger" tree="@Tree.t:updated_aln268" SecondInternalNodeHeight=true />
<log id="treeHeight" spec="treestat2.TreeStat2Logger" tree="@Tree.t:updated_aln268" TreeHeight=true />
```
