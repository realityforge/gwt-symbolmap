# Change Log

### Unreleased

* Upgrade the `au.com.stocksoftware.idea.codestyle` artifact to version `1.17`.
* Upgrade the `org.realityforge.guiceyloops` artifact to version `0.106`.
* Upgrade the `org.realityforge.org.jetbrains.annotations` artifact to version `1.7.0`.

### [v0.09](https://github.com/realityforge/gwt-symbolmap/tree/v0.09) (2019-10-11) · [Full Changelog](https://github.com/realityforge/gwt-symbolmap/compare/v0.08...v0.09)

* Upgrade the `org.realityforge.javax.annotation` artifact to version `1.0.1`.
* Upgrade the `org.realityforge.guiceyloops` artifact to version `0.102`.
* Upgrade the `au.com.stocksoftware.idea.codestyle` artifact to version `1.14`.
* Move to J2CL compatible variant of jetbrains annotations.
* Remove `{@inheritDoc}` as it only explicitly indicates that the default behaviour at the expense of significant visual clutter.

### [v0.08](https://github.com/realityforge/gwt-symbolmap/tree/v0.08) (2018-06-18) · [Full Changelog](https://github.com/realityforge/gwt-symbolmap/compare/v0.07...v0.08)

* Remove unused dependency on anodoc library.
* Replace usage of the `com.google.code.findbugs:jsr305:jar` dependency with the more lightweight
  `org.realityforge.javax.annotation:javax.annotation:jar` dependency.
* Cleanup POM by using transitive dependencies when appropriate.

### [v0.07](https://github.com/realityforge/gwt-symbolmap/tree/v0.07) (2018-06-07) · [Full Changelog](https://github.com/realityforge/gwt-symbolmap/compare/v0.06...v0.07)

* Sort the diff emitted by `SoycSizeMapsDiff.printToString()` based on ref field.

### [v0.06](https://github.com/realityforge/gwt-symbolmap/tree/v0.06) (2018-06-07) · [Full Changelog](https://github.com/realityforge/gwt-symbolmap/compare/v0.05...v0.06)

* Change default behaviour of `SoycSizeMapsDiff` to ignore differences in vars as vars seem to be
  created for new class literals and will always differ if a class is added or removed from the
  set being compiled.

### [v0.05](https://github.com/realityforge/gwt-symbolmap/tree/v0.05) (2018-06-05) · [Full Changelog](https://github.com/realityforge/gwt-symbolmap/compare/v0.04...v0.05)

* Convert `SymbolEntry.fragmentNumber` to an integer.
* Convert `SymbolEntry.sourceLine` to an integer.
* Ensure that `SymbolEntryIndexDiff` correctly handles differences in both members as well as types.
* Add `SymbolEntry.isType()` and `SymbolEntry.isMember()` helper methods.
* Add `SymbolEntryIndexDiff.printToString()` to display differences in human readable form.
* Add `SymbolEntryIndexDiff.hasDifferences()` to determine if there is any differences.
* Add `SoycSizeMapsDiff` class to help compare soyc size reports.

### [v0.04](https://github.com/realityforge/gwt-symbolmap/tree/v0.04) (2018-06-05) · [Full Changelog](https://github.com/realityforge/gwt-symbolmap/compare/v0.03...v0.04)

* Add `SoycSizeMaps.readFromGzFile(...)` for reading from a gzipped file.

### [v0.03](https://github.com/realityforge/gwt-symbolmap/tree/v0.03) (2018-06-05) · [Full Changelog](https://github.com/realityforge/gwt-symbolmap/compare/v0.02...v0.03)

* Add `SymbolEntryIndex.getSymbolEntries()` method that returns all the entries in the order they appear
  in the input file.
* Add `SymbolEntryIndexDiff` class to help derive diffs between symbol indexes.
* Add `SoycSizeMaps` class to ready in soyc size maps. Useful when comparing differences between builds.

### [v0.02](https://github.com/realityforge/gwt-symbolmap/tree/v0.02) (2018-03-15) · [Full Changelog](https://github.com/realityforge/gwt-symbolmap/compare/v0.01...v0.02)

* Add tests to enforce behaviour of entries and index.
* Enhance `SymbolEntryIndex` to add the ability to lookup entry by javascript name or jsni identifier.
* Change `SymbolEntryIndex.findSymbolsByClassName(...)` to return symbols.
* Add `SymbolEntry.toString()` method that emits serialized form of symbol entry.
* Rename `SymbolEntryIndex.findMembersByPatterns(..)` to `SymbolEntryIndex.findSymbolsByPatterns(..)`.
* Add `SymbolEntryIndex.findSymbolsByPatterns(..)` method that accepts string patterns.
* Fixed bug in `SymbolEntryIndex.findSymbolsByPatterns(..)` that would return symbols of non-matching members if
  classes matched.

### [v0.01](https://github.com/realityforge/gir/tree/v0.01) (2018-03-14) · [Full Changelog](https://github.com/realityforge/gir/compare/2edd3c5155779ee1a5b830a7056d374efc33002e...v0.01)

‎🎉	 Initial project released ‎🎉.
