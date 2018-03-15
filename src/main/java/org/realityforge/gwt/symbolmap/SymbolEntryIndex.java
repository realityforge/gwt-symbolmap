package org.realityforge.gwt.symbolmap;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.intellij.lang.annotations.RegExp;
import static org.testng.Assert.*;

/**
 * Index into which symbolMap can be loaded and assertions invoked on contents of index.
 */
@SuppressWarnings( "WeakerAccess" )
public final class SymbolEntryIndex
{
  /**
   * Index of classnames to entries.
   */
  private final HashMap<String, ArrayList<SymbolEntry>> _classNameToEntry = new HashMap<>();
  /**
   * Index of jsni identifiers to entries.
   */
  private final HashMap<String, SymbolEntry> _jsniToEntry = new HashMap<>();
  /**
   * Index of javascript names to entries.
   */
  private final HashMap<String, SymbolEntry> _jsToEntry = new HashMap<>();

  /**
   * Read and build an index from symbolMap file.
   *
   * @param symbolMapPath the path to load symbolMap from.
   * @return the new index.
   * @throws IOException    if there is an error reading file.
   * @throws ParseException if there is an error parsing file.
   */
  @Nonnull
  public static SymbolEntryIndex readSymbolMapIntoIndex( @Nonnull final Path symbolMapPath )
    throws IOException, ParseException
  {
    final SymbolEntryIndex index = new SymbolEntryIndex();
    SymbolEntry.readSymbolMap( symbolMapPath, index::addEntry );
    return index;
  }

  private void addEntry( @Nonnull final SymbolEntry entry )
  {
    _classNameToEntry
      .computeIfAbsent( entry.getClassName(), e -> new ArrayList<>() )
      .add( entry );
    _jsniToEntry.put( entry.getJsniIdent(), entry );
    _jsToEntry.put( entry.getJsName(), entry );
  }

  /**
   * Verify no entry for ClassName exists in index.
   * If a match is found then fail assertion error.
   *
   * @param classNamePattern the pattern
   */
  public void assertNoClassNameMatches( @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String classNamePattern )
  {
    assertNoClassNameMatches( Pattern.compile( "^" + classNamePattern + "$" ) );
  }

  /**
   * Verify no entry for ClassName exists in index.
   * If a match is found then fail assertion error.
   *
   * @param classNamePattern the pattern
   */
  public void assertNoClassNameMatches( @Nonnull final Pattern classNamePattern )
  {
    final List<SymbolEntry> matches = findSymbolsByClassName( classNamePattern );
    if ( !matches.isEmpty() )
    {
      fail( "Expected that the SymbolMap would have no classNames that match pattern " + classNamePattern +
            " but the following symbols match: " + matches );
    }
  }

  /**
   * Verify that a symbol is either present or not present based on present parameter.
   *
   * @param classNamePattern the pattern to match className.
   * @param present          true if member is expected to be present, false otherwise.
   */
  public void assertSymbol( @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String classNamePattern,
                            final boolean present )
  {
    assertSymbol( Pattern.compile( "^" + classNamePattern + "$" ),
                  present );
  }

  /**
   * Verify that a symbol is either present or not present based on present parameter.
   *
   * @param classNamePattern the pattern to match className.
   * @param present          true if member is expected to be present, false otherwise.
   */
  public void assertSymbol( @Nonnull final Pattern classNamePattern,
                            final boolean present )
  {
    if ( present )
    {
      assertClassNameMatches( classNamePattern );
    }
    else
    {
      assertNoClassNameMatches( classNamePattern );
    }
  }

  /**
   * Verify at least one entry for ClassName exists in index.
   * If no matches are found then fail assertion error.
   *
   * @param classNamePattern the pattern
   */
  public void assertClassNameMatches( @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String classNamePattern )
  {
    assertClassNameMatches( Pattern.compile( "^" + classNamePattern + "$" ) );
  }

  /**
   * Verify at least one entry for ClassName exists in index.
   * If no matches are found then fail assertion error.
   *
   * @param classNamePattern the pattern
   */
  public void assertClassNameMatches( @Nonnull final Pattern classNamePattern )
  {
    final List<SymbolEntry> matches = findSymbolsByClassName( classNamePattern );
    if ( matches.isEmpty() )
    {
      fail( "Expected that the SymbolMap would have at least one classname that matches className " +
            "pattern " + classNamePattern + "." );
    }
  }

  /**
   * Verify no entry for member exists in index.
   * If a match is found then fail assertion error.
   *
   * @param classNamePattern  the pattern to match className.
   * @param memberNamePattern the pattern to match member name.
   */
  public void assertNoMemberMatches( @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String classNamePattern,
                                     @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String memberNamePattern )
  {
    assertNoMemberMatches( Pattern.compile( "^" + classNamePattern + "$" ),
                           Pattern.compile( "^" + memberNamePattern + "$" ) );
  }

  /**
   * Verify no entry for member exists in index.
   * If a match is found then fail assertion error.
   *
   * @param classNamePattern  the pattern to match className.
   * @param memberNamePattern the pattern to match member name.
   */
  public void assertNoMemberMatches( @Nonnull final Pattern classNamePattern, @Nonnull final Pattern memberNamePattern )
  {
    final List<SymbolEntry> matches = findSymbolsByPatterns( classNamePattern, memberNamePattern );
    if ( !matches.isEmpty() )
    {
      fail( "Expected that the SymbolMap would have no members that match: classNamePattern '" + classNamePattern +
            "', memberPattern '" + memberNamePattern + "' but the following entries match: " + matches );
    }
  }

  /**
   * Verify at least one member exists in the index that matches patterns.
   * If a no match is found then fail assertion error.
   *
   * @param classNamePattern  the pattern to match className.
   * @param memberNamePattern the pattern to match member name.
   */
  public void assertMemberMatches( @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String classNamePattern,
                                   @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String memberNamePattern )
  {
    assertMemberMatches( Pattern.compile( "^" + classNamePattern + "$" ),
                         Pattern.compile( "^" + memberNamePattern + "$" ) );
  }

  /**
   * Verify at least one member exists in the index that matches patterns.
   * If a no match is found then fail assertion error.
   *
   * @param classNamePattern  the pattern to match className.
   * @param memberNamePattern the pattern to match member name.
   */
  public void assertMemberMatches( @Nonnull final Pattern classNamePattern, @Nonnull final Pattern memberNamePattern )
  {
    final List<SymbolEntry> matches = findSymbolsByPatterns( classNamePattern, memberNamePattern );
    if ( matches.isEmpty() )
    {
      fail( "Expected that the SymbolMap would have at least one member that matched: classNamePattern '" +
            classNamePattern + "', memberPattern '" + memberNamePattern + "' but no entries matched." );
    }
  }

  /**
   * Verify that either a member that matches patterns is either present or not present based on present parameter.
   *
   * @param classNamePattern  the pattern to match className.
   * @param memberNamePattern the pattern to match member name.
   * @param present           true if member is expected to be present, false otherwise.
   */
  public void assertSymbol( @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String classNamePattern,
                            @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String memberNamePattern,
                            final boolean present )
  {
    assertSymbol( Pattern.compile( "^" + classNamePattern + "$" ),
                  Pattern.compile( "^" + memberNamePattern + "$" ),
                  present );
  }

  /**
   * Verify that either a member that matches patterns is either present or not present based on present parameter.
   *
   * @param classNamePattern  the pattern to match className.
   * @param memberNamePattern the pattern to match member name.
   * @param present           true if member is expected to be present, false otherwise.
   */
  public void assertSymbol( @Nonnull final Pattern classNamePattern,
                            @Nonnull final Pattern memberNamePattern,
                            final boolean present )
  {
    if ( present )
    {
      assertMemberMatches( classNamePattern, memberNamePattern );
    }
    else
    {
      assertNoMemberMatches( classNamePattern, memberNamePattern );
    }
  }

  /**
   * Find symbols by classname pattern and member pattern.
   *
   * @param classNamePattern the pattern to match className.
   * @return the SymbolEntry instances that match.
   */
  @Nonnull
  public List<SymbolEntry> findSymbolsByClassName( @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String classNamePattern )
  {
    return findSymbolsByClassName( Pattern.compile( "^" + classNamePattern + "$" ) );
  }

  /**
   * Find symbols by classname pattern and member pattern.
   *
   * @param classNamePattern the pattern to match className.
   * @return the SymbolEntry instances that match.
   */
  @Nonnull
  public List<SymbolEntry> findSymbolsByClassName( @Nonnull final Pattern classNamePattern )
  {
    return _classNameToEntry.entrySet()
      .stream()
      .filter( n -> classNamePattern.matcher( n.getKey() ).matches() )
      .flatMap( n -> n.getValue().stream() )
      .collect( Collectors.toList() );
  }

  /**
   * Find symbols by classname pattern and member pattern.
   *
   * @param classNamePattern  the pattern to match className.
   * @param memberNamePattern the pattern to match member name.
   * @return the SymbolEntry instances that match.
   */
  @Nonnull
  public List<SymbolEntry> findSymbolsByPatterns( @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String classNamePattern,
                                                  @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String memberNamePattern )
  {
    return findSymbolsByPatterns( Pattern.compile( "^" + classNamePattern + "$" ),
                                  Pattern.compile( "^" + memberNamePattern + "$" ) );
  }

  /**
   * Find symbols by classname pattern and member pattern.
   *
   * @param classNamePattern  the pattern to match className.
   * @param memberNamePattern the pattern to match member name.
   * @return the SymbolEntry instances that match.
   */
  @Nonnull
  public List<SymbolEntry> findSymbolsByPatterns( @Nonnull final Pattern classNamePattern,
                                                  @Nonnull final Pattern memberNamePattern )
  {
    return _classNameToEntry
      .entrySet()
      .stream()
      .filter( e -> classNamePattern.matcher( e.getKey() ).matches() )
      .flatMap( e -> e.getValue().stream() )
      .filter( s -> memberNamePattern.matcher( s.getMemberName() ).matches() )
      .collect( Collectors.toList() );
  }

  /**
   * Find symbol by jsni identifier.
   *
   * @param jsniIdentifier the JSNI identifier.
   * @return the SymbolEntry instance that matches if any.
   */
  @Nullable
  public SymbolEntry findByJsniIdentifier( @Nonnull final String jsniIdentifier )
  {
    return _jsniToEntry.get( jsniIdentifier );
  }

  /**
   * Find symbol by javascript names.
   *
   * @param jsName the javascript names.
   * @return the SymbolEntry instance that matches if any.
   */
  @Nullable
  public SymbolEntry findByJsName( @Nonnull final String jsName )
  {
    return _jsToEntry.get( jsName );
  }
}
