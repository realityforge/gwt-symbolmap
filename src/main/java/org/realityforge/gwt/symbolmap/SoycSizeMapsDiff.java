package org.realityforge.gwt.symbolmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class SoycSizeMapsDiff
{
  enum DiffType
  {
    ADDED,
    REMOVED,
    CHANGED
  }

  public static class Entry
    implements Comparable<Entry>
  {
    private final int _fragment;
    @Nonnull
    private final DiffType _type;
    @Nullable
    private final SoycSize _lhs;
    @Nullable
    private final SoycSize _rhs;

    Entry( final int fragment,
           @Nonnull final DiffType type,
           @Nullable final SoycSize lhs,
           @Nullable final SoycSize rhs )
    {
      assert DiffType.ADDED != type || ( null == lhs && null != rhs );
      assert DiffType.REMOVED != type || ( null != lhs && null == rhs );
      assert DiffType.CHANGED != type || ( null != lhs && null != rhs );
      _fragment = fragment;
      _type = Objects.requireNonNull( type );
      _lhs = lhs;
      _rhs = rhs;
    }

    public int getFragment()
    {
      return _fragment;
    }

    @Nonnull
    public DiffType getType()
    {
      return _type;
    }

    @Nonnull
    public String getRef()
    {
      return null != _lhs ? _lhs.getRef() : Objects.requireNonNull( _rhs ).getRef();
    }

    @Nullable
    public SoycSize getLhs()
    {
      return _lhs;
    }

    @Nullable
    public SoycSize getRhs()
    {
      return _rhs;
    }

    @Override
    public int compareTo( @Nonnull final SoycSizeMapsDiff.Entry o )
    {
      final String ref = getRef();
      final String otherRef = o.getRef();
      final int i = ref.compareTo( otherRef );
      if ( 0 != i )
      {
        return i;
      }
      else
      {
        return getType().compareTo( o.getType() );
      }
    }
  }

  @Nonnull
  private final SoycSizeMaps _before;
  @Nonnull
  private final SoycSizeMaps _after;
  private final List<Entry> _entries = new ArrayList<>();

  private SoycSizeMapsDiff( @Nonnull final SoycSizeMaps before, @Nonnull final SoycSizeMaps after )
  {
    _before = Objects.requireNonNull( before );
    _after = Objects.requireNonNull( after );
  }

  @Nonnull
  public static SoycSizeMapsDiff diff( @Nonnull final SoycSizeMaps before, @Nonnull final SoycSizeMaps after )
  {
    /*
     * By default vars are ignored as GWT produces a var per-type and there is no easy way to trace var to
     * type and thus they always come through as different.
     */
    return diff( before, after, false );
  }

  @Nonnull
  public static SoycSizeMapsDiff diff( @Nonnull final SoycSizeMaps before,
                                       @Nonnull final SoycSizeMaps after,
                                       final boolean includeVar )
  {
    final SoycSizeMapsDiff diff = new SoycSizeMapsDiff( before, after );
    for ( final SoycSizeMap map : before.getSizeMaps() )
    {
      final int fragment = map.getFragment();
      final SoycSizeMap other =
        after.getSizeMaps()
          .stream()
          .filter( m -> m.getFragment() == fragment )
          .findFirst()
          .orElse( null );
      final List<SoycSize> sizes =
        includeVar ?
        map.getSizes() :
        map.getSizes().stream().filter( s -> SoycSize.Type.var != s.getType() ).collect( Collectors.toList() );
      for ( final SoycSize soycSize : sizes )
      {
        final SoycSize otherSoycSize = null != other ? other.findSizeByRef( soycSize.getRef() ) : null;
        if ( null == otherSoycSize )
        {
          diff._entries.add( new Entry( fragment, DiffType.REMOVED, soycSize, null ) );
        }
        else if ( otherSoycSize.getSize() != soycSize.getSize() )
        {
          diff._entries.add( new Entry( fragment, DiffType.CHANGED, soycSize, otherSoycSize ) );
        }
      }
    }
    for ( final SoycSizeMap map : after.getSizeMaps() )
    {
      final int fragment = map.getFragment();
      final SoycSizeMap other =
        before.getSizeMaps().stream().filter( m -> m.getFragment() == fragment ).findFirst().orElse( null );
      final List<SoycSize> sizes =
        includeVar ?
        map.getSizes() :
        map.getSizes().stream().filter( s -> SoycSize.Type.var != s.getType() ).collect( Collectors.toList() );
      for ( final SoycSize soycSize : sizes )
      {
        final SoycSize otherSoycSize = null != other ? other.findSizeByRef( soycSize.getRef() ) : null;
        if ( null == otherSoycSize )
        {
          diff._entries.add( new Entry( fragment, DiffType.ADDED, null, soycSize ) );
        }
      }
    }
    return diff;
  }

  @Nonnull
  public SoycSizeMaps getBefore()
  {
    return _before;
  }

  @Nonnull
  public SoycSizeMaps getAfter()
  {
    return _after;
  }

  @Nonnull
  public List<Entry> getEntries()
  {
    return Collections.unmodifiableList( _entries );
  }

  public boolean hasDifferences()
  {
    return !getEntries().isEmpty();
  }

  @Nonnull
  public String printToString()
  {
    final StringBuilder sb = new StringBuilder();
    final List<Entry> entries = getEntries();
    if ( !entries.isEmpty() )
    {
      entries.stream().sorted().forEach( entry -> emitEntry( sb, entry ) );
    }
    return sb.toString();
  }

  private void emitEntry( @Nonnull final StringBuilder sb, @Nonnull final Entry entry )
  {
    final DiffType type = entry.getType();
    sb.append( type );
    sb.append( " " );
    if ( DiffType.ADDED == type )
    {
      final SoycSize soycSize = Objects.requireNonNull( entry.getRhs() );
      sb.append( "type=" );
      sb.append( soycSize.getType() );
      sb.append( " ref=" );
      sb.append( soycSize.getRef() );
      sb.append( " size=" );
      sb.append( soycSize.getSize() );
    }
    else if ( DiffType.REMOVED == type )
    {
      final SoycSize soycSize = Objects.requireNonNull( entry.getLhs() );
      sb.append( "type=" );
      sb.append( soycSize.getType() );
      sb.append( " ref=" );
      sb.append( soycSize.getRef() );
      sb.append( " size=" );
      sb.append( soycSize.getSize() );
    }
    else
    {
      final SoycSize soycSize = Objects.requireNonNull( entry.getLhs() );
      sb.append( "type=" );
      sb.append( soycSize.getType() );
      sb.append( " ref=" );
      sb.append( soycSize.getRef() );
      sb.append( " size=" );
      final int lhsSize = soycSize.getSize();
      sb.append( lhsSize );
      sb.append( "->" );
      final int rhsSize = Objects.requireNonNull( entry.getRhs() ).getSize();
      sb.append( rhsSize );
      sb.append( " Size Delta=" );
      sb.append( rhsSize - lhsSize );
    }
    sb.append( "\n" );
  }
}
