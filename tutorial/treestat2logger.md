# Tutorial for using TreeStat2Logger

Written by John Tay 24 August 2023

Currently, only the following tree statistics are implemented in TreeStat2Logger:
ExternalInternalRatio, GetTypeChanges, SecondInternalNodeHeight, and TreeHeight.

To access these tree statistics, you must clone the latest TreeStat2 repo and build it with Maven
(requires Java 25 and Maven 3.9+).

```
git clone https://github.com/alexeid/TreeStat2
cd TreeStat2
mvn package
```

The built package will be located in target/TreeStat2.vX.X.X.zip (for a development version,
target/TreeStat2.vX.X.X-SNAPSHOT.zip). Unzip it into a `TreeStat2` folder in your BEAST 2.8 package
directory: `~/Library/Application Support/BEAST/2.8` on macOS, or `~/.beast/2.8` on Linux. For
example, on macOS:

```
mkdir -p ~/Library/Application\ Support/BEAST/2.8/TreeStat2
unzip target/TreeStat2.v*.zip -d ~/Library/Application\ Support/BEAST/2.8/TreeStat2
```

## Example with GetTypeChanges on MTBD

These instructions will assist in setting up TreeStat2Logger a multitype birth-death model on some
simulated data.
GetTypeChanges must take a discrete nominal input greater than 1.

### Requirements

Ensure that the following packages are installed in your BEAST 2.8 package directory (see above):
BEAST v2.8, bdmm, feast, MultiTypeTree, and TreeStat2 (with its dependencies CCD and BEASTLabs).

> [!Note]
> The example analysis `tutorial/updated_aln268.xml` was written and tested for BEAST2.7.3. It
> needs BEAST v2.8 releases of bdmm and MultiTypeTree to run on BEAST v2.8, which are not available
> yet. A draft BEAST v2.8 conversion is in `tutorial/updated_aln268_b3_draft.xml`; see
> [TODO.md](TODO.md) for what is blocking it and what remains to be done.

###  

The following code logs the trace and statistics from the MCMC, as well as the number of type
changes.

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

The following line assigns "getTypeChanges" for an analysis with 2 types, outputting to the
trace-log file "updated_aln268.log":

```
  <log id="getTypeChanges" spec="treestat2.TreeStat2Logger" tree="@Tree.t:updated_aln268" GetTypeChanges="2" />
```

We now point to the type changes variable with idref="getTypeChanges":

```
  <log idref="getTypeChanges" spec="treestat2.TreeStat2Logger" tree="@Tree.t:updated_aln268" GetTypeChanges="2" />
```

### Note on other tree statistics

The other currently implemented tree statistics rely on a true/false check, starting initially as
false.
The following examples will log the respective tree statistics:

```
<log id="externalInternalRatio" spec="treestat2.TreeStat2Logger" tree="@Tree.t:updated_aln268" ExternalInternalRatio=true />
<log id="secondInternalNodeHeight" spec="treestat2.TreeStat2Logger" tree="@Tree.t:updated_aln268" SecondInternalNodeHeight=true />
<log id="treeHeight" spec="treestat2.TreeStat2Logger" tree="@Tree.t:updated_aln268" TreeHeight=true />
```
