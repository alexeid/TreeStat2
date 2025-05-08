TreeStat2
=========

Software for producing statistics on phylogenetic trees.
The scope of TreeStat2 is the processing into statistics of:
(1) Time-trees
(2) Sampled-ancestor trees and
(3) Structured (multi-type) time-trees.

This package depends on [BEAST2](https://github.com/CompEvol/beast2/) see
also [https://beast2.org/](https://beast2.org/)

---

## Example Usage

Below are some example commands. For a full list of options, run:

```bash
applauncher TreeStatApp --help
```

> [!Note]
> Depending on your installation, you may need to use the full path to applauncher.
> For example:
> `Applications/BEAST 2.7.7/bin/applauncher`

### Compute One Statistic for a Single Tree File

To compute the `TreeLength` statistic for `example.trees` and save the result to
`computed-stats.log`:

```bash
applauncher TreeStatApp --stats TreeLength --tree-file example.trees --out-file computed-stats.log
```

---

### Compute Multiple Statistics for Multiple Tree Files

To compute both `TreeLength` and `CCD1ExpectedRFDistance` for two input files
(`example1.trees` and `example2.trees`) and save the output to tagged log files:

```bash
applauncher TreeStatApp --stats TreeLength CCD1ExpectedRFDistance --tree-files example1.trees example2.trees --out-tag -stats.log
```

This will generate:

- `example1-stats.log`
- `example2-stats.log`

If no `--out-tag` is specified, the default suffix `-treestats.log` is used. For example:

```bash
applauncher TreeStatApp --stats TreeLength CCD1ExpectedRFDistance --tree-files example1.trees example2.trees
```

Generates:

- `example1-treestats.log`
- `example2-treestats.log`

---

### Using a Control File Instead of Command-Line Stats

You can also define the statistics in a plain text control file. For example, create a file named
`config`:

```
TreeLength
CCD1ExpectedRFDistance
```

Then run:

```bash
applauncher TreeStatApp --control-file config --tree-files example1.trees example2.trees --out-tag -stats.log
```

This will also generate:

- `example1-stats.log`
- `example2-stats.log`

### Further Input Options

In addition to the shorthand statistic names (e.g., `TreeLength`, `CCD1ExpectedRFDistance`),
you may also specify the **fully qualified class names** directly in a file `config`:

```
treestat2.statistics.TreeLength
treestat2.statistics.CCD1ExpectedRFDistance
```

or on the command line

```bash
applauncher TreeStatApp --stats treestat2.statistics.TreeLength  treestat2.statistics.CCD1ExpectedRFDistance --tree-file example.trees
```

> [!Important]
> This is exactly how shorthand names are resolved internally.
>
> Specifying the full class path allows TreeStat to use statistics from other packages or custom
> implementations
> — as long as they follow the expected `TreeSummaryStatistic` interface.
>
> This feature is currently **experimental and untested**!

### CCD burn-in option

The `--ccd-burn-in` (or `-b`) option allows you to control the burn-in proportion used when
constructing the CCD (Conditional Clade Distribution) from the input tree set.
This value must be a `double` between `0.0`(inclusive) and `1.0`(exclusive).

> [!Note]
> CCD0 construction uses a default burn-in of `0.1` and enforces this as a minimum to ensure
> proper initialization.

### Jump Distance Statistics

You can calculate traces of tree distances for all trees to a specified fixed tree in the sample.
Currently the RNNI and RF metrics are supported.

```bash
applauncher TreeStatApp --stats RNNIJumpDistance 10 RFJumpDistance 10 --tree-file example.trees
```

The above command would calculate the RF and RNNI distance of all trees in `example.trees` to the
10th tree in the sample, which is used as the fixed reference tree.
These traces can then be put into tracer to generate a pseudo ESS estimate.

> [!Important]
> The trace of tree distances contains the sample itself, i.e. at index 10 there will be a 0 in
> the trace. This will likely lower the ESS estimates!

It is also possible to calculate these traces for multiple trees in one command, for example if
you want to calculate the trace for sample `10` and sample `56` use the following command:

```bash
applauncher TreeStatApp --stats RNNIJumpDistance 10 RNNIJumpDistance 56 --tree-file example.trees
```
