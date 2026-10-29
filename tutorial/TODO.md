# TODO: migrate the tutorial XML to BEAST 3 (BEAST v2.8)

| File | What it is |
|---|---|
| `updated_aln268.xml` | Original BEAST 2.7 analysis (tested with BEAST 2.7.3). Unchanged; use it with BEAST 2.7. |
| `updated_aln268_b3_draft.xml` | **Draft** BEAST 3 conversion. Does **not** run yet. |

## What is blocking the migration

The analysis is a multi-type birth-death model built on two packages that have no BEAST 3 release yet:

| Package | Required by the XML | BEAST 3 status |
|---|---|---|
| **MultiTypeTree** (v8.0.0) | Tree (`StructuredCoalescentMultiTypeTree`), `SCMigrationModel`, `TypeSet`, `ExcludablePrior`, 7 operators (`TypedSubtreeExchange`, `TypedWilsonBalding`, `NodeRetype`, 2× `NodeShiftRetype`, `MultiTypeUniform`, `MultiTypeTreeScale`), 6 loggers (`TreeLengthLogger`, `TypeChangeCounts`, `NodeTypeCounts`, `TreeRootTypeLogger`, `MAPTreeLogger`, `TypedNodeTreeLogger`) | **Not migrated** (still an Ant / BEAST 2 build) |
| **BDMM** (v2.0.0) | Tree prior `bdmm.evolution.speciation.BirthDeathMigrationModel` | **Not migrated** (still an Ant / BEAST 2 build). `bdmm-prime` on Maven Central is a different package. |
| feast | `AlignmentFromFasta`, `TraitSetFromTaxonSet` | Migrated: `io.github.tgvaughan:feast:11.0.1` |

The TreeStat2 part of the analysis (`treestat2.TreeStat2Logger` with `GetTypeChanges="2"`) is already
BEAST 3 compatible. However, `GetTypeChanges` reads the `type` annotations of a multi-type tree, so this example
can't be demonstrated without MultiTypeTree.

Validation of the draft with BEAST 3 currently fails at the first MultiTypeTree element:

```
beast -validate updated_aln268_b3_draft.xml

Class multitypetree.evolution.tree.StructuredCoalescentMultiTypeTree could not be found.
This XML requires the following packages that are not installed: BDMM v2.0.0, MultiTypeTree v8.0.0
```

## What has been done in the draft

`updated_aln268_b3_draft.xml` was generated from `updated_aln268.xml` by the BEAST 3 migration script
(`beast3-migration/skills/xml-migration/convert_b2_to_b3.py`): 42 changes, 10 warnings.

- `version="2.7"` → `version="2.8"`; namespace updated.
- All `RealParameter` / `BooleanParameter` → BEAST 3 typed parameters (`RealScalarParam`, `RealVectorParam`,
  `SimplexParam`, `BoolVectorParam`) with a `domain` (`Real`, `PositiveReal`, `UnitInterval`) replacing
  `lower`/`upper`.
- `Prior` wrappers removed: the distribution is applied to the parameter directly, wrapped in `IID` for vector
  parameters (`RPrior`, `rateMatrixPrior`).
- Core classes → BEAST 3 spec classes: `TreeLikelihood`, `SiteModel`, `HKY`, `Frequencies`, `StrictClockModel`,
  `ScaleOperator`, `DeltaExchangeOperator`, `UpDownOperator`, `ESS`; `TreeHeightLogger` → `TreeStatLogger`.

## What is left to do

1. **Wait for (or do) BEAST 3 migrations of MultiTypeTree and BDMM**, then add them to the BEAST 2.8 package
   directory and run `beast -validate updated_aln268_b3_draft.xml` until it ends with `Done!`.
2. **Fix parameter types at MultiTypeTree/BDMM inputs.** The script converted *every* parameter, including those
   passed straight into BEAST 2 classes that expect `RealParameter`/`BooleanParameter`. Match them to the input
   types of the migrated packages:
   - `SCMigrationModel`: `rateMatrix`, `rateMatrixScaleFactor`, `popSizes`, `popSizesScaleFactor`
     (`RealParameter.0`–`RealParameter.3`).
   - `BirthDeathMigrationModel`: `R0`, `samplingProportion`, `migrationMatrix` (`rateMatrix`), `frequencies`
     (`geo-frequencies`), `samplingRateChangeTimes`, `reverseTimeArrays`, `becomeUninfectiousRate`.
   - `ExcludablePrior`: `x` (`samplingProportion`) and `xInclude`.
3. **Fix mixed old/new classes in the priors.** The `Beta` distribution inside `ExcludablePrior` and the
   `Function$Constant` mean of the `Exponential` gamma-shape prior are still BEAST 2 classes, but now receive
   BEAST 3 parameters. Replace them with their spec equivalents, or with whatever the migrated `ExcludablePrior`
   expects.
4. **Review the converter warnings:**
   - `gammaShape` and `mutationRate` were given `domain="PositiveReal"` by inference from their role (no bounds in
     the source). Confirm.
   - `samplingProportion` (`UnitInterval`) is moved by a `ScaleOperator`, which can propose values above 1.
     Consider a bounded operator, e.g. `beast.base.spec.evolution.operator.IntervalScaleOperator`, or confirm that
     the operator respects the domain.
   - `TreeLikelihood`: optionally switch to `ThreadedTreeLikelihood` for multi-core performance.
5. **Update the header:** `required="BEAST.base v2.7.4:BDMM v2.0.0:MultiTypeTree v8.0.0"` still lists the
   BEAST 2.7 package versions; set it to the BEAST 2.8 / migrated versions.
6. When it validates and runs, rename it to `updated_aln268_b3.xml` (or replace `updated_aln268.xml`), update
   `treestat2logger.md`, and delete this file.
