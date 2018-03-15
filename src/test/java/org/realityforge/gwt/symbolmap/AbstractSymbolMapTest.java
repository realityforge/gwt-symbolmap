package org.realityforge.gwt.symbolmap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nonnull;

public abstract class AbstractSymbolMapTest
{
  @Nonnull
  final Path createFileFromContent( @Nonnull final String input )
    throws IOException
  {
    final Path file = Files.createTempFile( "gwt-symbolmap", ".symbolMap" );
    Files.write( file, input.getBytes() );
    return file;
  }
}
