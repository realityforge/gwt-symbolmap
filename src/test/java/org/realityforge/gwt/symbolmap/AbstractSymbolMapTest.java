package org.realityforge.gwt.symbolmap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import javax.annotation.Nonnull;

public abstract class AbstractSymbolMapTest
{
  @Nonnull
  final SymbolEntryIndex readIndexFromInput( @Nonnull final String input )
    throws IOException, ParseException
  {
    final Path file = createFileFromContent( input );
    return SymbolEntryIndex.readSymbolMapIntoIndex( file );
  }

  @Nonnull
  final Path createFileFromContent( @Nonnull final String input )
    throws IOException
  {
    final Path file = Files.createTempFile( "gwt-symbolmap", ".symbolMap" );
    Files.write( file, input.getBytes() );
    return file;
  }
}
