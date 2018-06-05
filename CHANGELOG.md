# Change Log

### Unreleased

* Convert `SymbolEntry.fragmentNumber` to an integer.
* Convert `SymbolEntry.sourceLine` to an integer.
* Ensure that `SymbolEntryIndexDiff` correctly handles differences in both members as well as types.
* Add `SymbolEntry.isType()` and `SymbolEntry.isMember()` helper methods.
* Add `SymbolEntryIndexDiff.printToString()` to display differences in human readable form.
* Add `SymbolEntryIndexDiff.hasDifferences()` to determine if there is any differences.

### [v0.04](https://github.com/realityforge/gwt-symbolmap/tree/v0.04) (2018-06-05)
[Full Changelog](https://github.com/realityforge/gwt-symbolmap/compare/v0.03...v0.04)

* Add `SoycSizeMaps.readFromGzFile(...)` for reading from a gzipped file.

### [v0.03](https://github.com/realityforge/gwt-symbolmap/tree/v0.03) (2018-06-05)
[Full Changelog](https://github.com/realityforge/gwt-symbolmap/compare/v0.02...v0.03)

* Add `SymbolEntryIndex.getSymbolEntries()` method that returns all the entries in the order they appear
  in the input file.
* Add `SymbolEntryIndexDiff` class to help derive diffs between symbol indexes.
* Add `SoycSizeMaps` class to ready in soyc size maps. Useful when comparing differences between builds.

### [v0.02](https://github.com/realityforge/gwt-symbolmap/tree/v0.02) (2018-03-15)
[Full Changelog](https://github.com/realityforge/gwt-symbolmap/compare/v0.01...v0.02)

* Add tests to enforce behaviour of entries and index.
* Enhance `SymbolEntryIndex` to add the ability to lookup entry by javascript name or jsni identifier.
* Change `SymbolEntryIndex.findSymbolsByClassName(...)` to return symbols.
* Add `SymbolEntry.toString()` method that emits serialized form of symbol entry.
* Rename `SymbolEntryIndex.findMembersByPatterns(..)` to `SymbolEntryIndex.findSymbolsByPatterns(..)`.
* Add `SymbolEntryIndex.findSymbolsByPatterns(..)` method that accepts string patterns.
* Fixed bug in `SymbolEntryIndex.findSymbolsByPatterns(..)` that would return symbols of non-matching members if
  classes matched.

### [v0.01](https://github.com/realityforge/gir/tree/v0.01) (2018-03-14)
[Full Changelog](https://github.com/realityforge/gir/compare/2edd3c5155779ee1a5b830a7056d374efc33002e...v0.01)

‎🎉	 Initial project released ‎🎉.
