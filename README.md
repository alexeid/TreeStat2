TreeStat2
=========

Software for producing statistics on phylogenetic trees. 
The scope of TreeStat2 is the processing into statistics of: 
  (1) Time-trees 
  (2) Sampled-ancestor trees and 
  (3) Structured (multi-type) time-trees.
  
This package depends on [BEAST2](https://github.com/CompEvol/beast2/) see also [https://beast2.org/](https://beast2.org/)

---
## Example Usage

### Compute One Statistic for a Single Tree File

To compute the `TreeLength` statistic for `example.trees` and save the result to `computed-stats.log`:

```bash
applauncher TreeStatApp --stats TreeLength --tree-file example.trees --out-file computed-stats.log
```

---

### Compute Multiple Statistics for Multiple Tree Files

To compute both `TreeLength` and `CCD1ExpectedRFDistance` for two input files (`example1.trees` and `example2.trees`) and save the output to tagged log files:

```bash
applauncher TreeStatApp --stats TreeLength CCD1ExpectedRFDistance --tree-files example1.trees 
example2.trees --out-tag -stats.log
```

This will generate:
- `example1-stats.log`
- `example2-stats.log`

If no `--out-tag` is specified, the default suffix `-treestats.log` is used. For example:

```bash
applauncher TreeStatApp --stats TreeLength CCD1ExpectedRFDistance --tree-files example1.trees 
example2.trees
```

Generates:
- `example1-treestats.log`
- `example2-treestats.log`

---

### Using a Control File Instead of Command-Line Stats

You can also define the statistics in a plain text control file. For example, create a file named `config`:

```
TreeLength
CCD1ExpectedRFDistance
```

Then run:

```bash
applauncher TreeStatApp --control-file config --tree-files example1.trees example2.trees 
--out-tag -stats.log
```

This will also generate:
- `example1-stats.log`
- `example2-stats.log`
