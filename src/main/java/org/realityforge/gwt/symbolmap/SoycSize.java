package org.realityforge.gwt.symbolmap;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.w3c.dom.Element;

/**
 * Represents a symbol in SoyC story.xml
 */
public final class SoycSize
{
  enum Type
  {
    var,
    string,
    type,
    method,
    field
  }

  @Nonnull
  private final Type _type;
  @Nonnull
  private final String _ref;
  private final int _size;

  private SoycSize( @Nonnull final Type type, @Nonnull final String ref, final int size )
  {
    _type = Objects.requireNonNull( type );
    _ref = Objects.requireNonNull( ref );
    _size = size;
  }

  @Nonnull
  public Type getType()
  {
    return _type;
  }

  @Nonnull
  public String getRef()
  {
    return _ref;
  }

  public int getSize()
  {
    return _size;
  }

  @Override
  public String toString()
  {
    return "<size type=\"" + _type + "\" ref=\"" + _ref + "\" size=\"" + _size + "\"/>";
  }

  @Nonnull
  static SoycSize parse( @Nonnull final Element element )
  {
    final String type = element.getAttribute( "type" );
    final String ref = element.getAttribute( "ref" );
    final int size = Integer.parseInt( element.getAttribute( "size" ) );

    return new SoycSize( Type.valueOf( type ), ref, size );
  }
}
