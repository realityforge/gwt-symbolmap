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
      if ( entry.isMember() && null == after.findByJsniIdentifier( entry.getJsniIdent() ) )
      {
        diff._missing.add( entry );
      }
      else if ( entry.isType() && null == after.findByJsName( entry.getJsName() ) )
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
      if ( entry.isMember() && null == before.findByJsniIdentifier( entry.getJsniIdent() ) )
      {
        diff._additional.add( entry );
      }
      else if ( entry.isType() && null == before.findByJsName( entry.getJsName() ) )
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

  public boolean hasDifferences()
  {
    return !( getAdditional().isEmpty() && getMissing().isEmpty() );
  }

  @Nonnull
  public String printToString()
  {
    final StringBuilder sb = new StringBuilder();
    final List<SymbolEntry> missing = getMissing();
    if ( !missing.isEmpty() )
    {
      sb.append( "\nSymbols Removed:\n\n" );
      missing.forEach( entry -> emitEntry( sb, entry ) );
    }
    final List<SymbolEntry> additional = getAdditional();
    if ( !additional.isEmpty() )
    {
      sb.append( "\nSymbols Added:\n\n" );
      additional.forEach( entry -> emitEntry( sb, entry ) );
    }
    return sb.toString();
  }

  private void emitEntry( @Nonnull final StringBuilder sb, @Nonnull final SymbolEntry entry )
  {
    sb.append( "Symbol: " );
    if ( entry.isType() )
    {
      sb.append( entry.getClassName() );
    }
    else
    {
      sb.append( entry.getJsniIdent() );
    }
    sb.append( "\nLocation: " );
    sb.append( entry.getSourceUri() );
    sb.append( ":" );
    sb.append( entry.getSourceLine() );
    sb.append( "\n\n" );
  }
}
