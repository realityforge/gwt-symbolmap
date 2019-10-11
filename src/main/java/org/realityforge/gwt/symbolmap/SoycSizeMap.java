package org.realityforge.gwt.symbolmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class SoycSizeMap
{
  @Nonnull
  private final List<SoycSize> _sizes = new ArrayList<>();
  @Nonnull
  private final Map<String, SoycSize> _sizeMap = new HashMap<>();
  private final int _fragment;
  private final int _size;

  SoycSizeMap( final int fragment, final int size )
  {
    _fragment = fragment;
    _size = size;
  }

  public int getFragment()
  {
    return _fragment;
  }

  public int getSize()
  {
    return _size;
  }

  @Nonnull
  public List<SoycSize> getSizes()
  {
    return Collections.unmodifiableList( _sizes );
  }

  @Nullable
  public SoycSize findSizeByRef( @Nonnull final String ref )
  {
    return _sizeMap.get( ref );
  }

  @Override
  public String toString()
  {
    return "<sizemap fragment=\"" + _fragment + "\" size=\"" + _size + "\">\n" +
           _sizes.stream().map( SoycSize::toString ).collect( Collectors.joining( "\n" ) ) + "\n" +
           "</sizemap>\n";
  }

  @Nonnull
  static SoycSizeMap parse( @Nonnull final Element element )
  {
    final int fragment = Integer.parseInt( element.getAttribute( "fragment" ) );
    final int size = Integer.parseInt( element.getAttribute( "size" ) );
    final SoycSizeMap soycSizeMap = new SoycSizeMap( fragment, size );

    final NodeList sizeElements = element.getElementsByTagName( "size" );
    for ( int i = 0; i < sizeElements.getLength(); i++ )
    {
      final SoycSize soycSize = SoycSize.parse( (Element) sizeElements.item( i ) );
      soycSizeMap._sizes.add( soycSize );
      soycSizeMap._sizeMap.put( soycSize.getRef(), soycSize );
    }
    return soycSizeMap;
  }
}
