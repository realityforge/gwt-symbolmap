package org.realityforge.gwt.symbolmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Results of performing a difference between to symbol indexes.
 */
public final class SymbolEntryIndexDiff
{
  private final List<SymbolEntry> _missing = new ArrayList<>();
  private final List<SymbolEntry> _additional = new ArrayList<>();
  private final List<SymbolEntry> _same = new ArrayList<>();
  @Nonnull
  private final SymbolEntryIndex _before;
  @Nonnull
  private final SymbolEntryIndex _after;

  private SymbolEntryIndexDiff( @Nonnull final SymbolEntryIndex before,
                                @Nonnull final SymbolEntryIndex after )
  {
    _before = Objects.requireNonNull( before );
    _after = Objects.requireNonNull( after );
  }

  @Nonnull
  public static SymbolEntryIndexDiff diff( @Nonnull final SymbolEntryIndex before,
                                           @Nonnull final SymbolEntryIndex after )
  {
    final SymbolEntryIndexDiff diff = new SymbolEntryIndexDiff( before, after );
    for ( final SymbolEntry entry : before.getSymbolEntries() )
    {
      final String jsniIdent = entry.getJsniIdent();
      final boolean isJsniValid = "".equals( jsniIdent );
      if ( !isJsniValid && null == after.findByJsniIdentifier( jsniIdent ) )
      {
        diff._missing.add( entry );
      }
      else if ( isJsniValid && null == after.findByJsName( entry.getJsName() ) )
      {
        diff._missing.add( entry );
      }
      else
      {
        diff._same.add( entry );
      }
    }
    for ( final SymbolEntry entry : after.getSymbolEntries() )
    {
      final String jsniIdent = entry.getJsniIdent();
      final boolean isJsniValid = "".equals( jsniIdent );
      if ( !isJsniValid && null == before.findByJsniIdentifier( jsniIdent ) )
      {
        diff._additional.add( entry );
      }
      else if ( isJsniValid && null == before.findByJsName( entry.getJsName() ) )
      {
        diff._additional.add( entry );
      }
    }
    return diff;
  }

  @Nonnull
  public SymbolEntryIndex getBefore()
  {
    return _before;
  }

  @Nonnull
  public SymbolEntryIndex getAfter()
  {
    return _after;
  }

  @Nonnull
  public List<SymbolEntry> getMissing()
  {
    return Collections.unmodifiableList( _missing );
  }

  @Nonnull
  public List<SymbolEntry> getAdditional()
  {
    return Collections.unmodifiableList( _additional );
  }

  @Nonnull
  public List<SymbolEntry> getSame()
  {
    return Collections.unmodifiableList( _same );
  }
}
