package org.realityforge.gwt.symbolmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.zip.GZIPOutputStream;
import javax.annotation.Nonnull;

abstract class AbstractTest
{
  @Nonnull
  final SymbolEntryIndex readIndexFromInput( @Nonnull final String input )
    throws IOException, ParseException
  {
    final Path file = createFileFromContent( input );
    return SymbolEntryIndex.readSymbolMapIntoIndex( file );
  }

  @SuppressWarnings( "SameParameterValue" )
  @Nonnull
  final Path createGzipFileFromContent( @Nonnull final String input )
    throws IOException
  {
    return createFileFromContent( gzipData( input.getBytes() ) );
  }

  @Nonnull
  final Path createFileFromContent( @Nonnull final String input )
    throws IOException
  {
    return createFileFromContent( input.getBytes() );
  }

  @Nonnull
  private Path createFileFromContent( @Nonnull final byte[] bytes )
    throws IOException
  {
    final Path file = Files.createTempFile( "gwt-symbolmap", ".symbolMap" );
    Files.write( file, bytes );
    return file;
  }

  @Nonnull
  private byte[] gzipData( @Nonnull final byte[] bytes )
    throws IOException
  {
    final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    try
    {
      try ( final GZIPOutputStream zipStream = new GZIPOutputStream( byteStream ) )
      {
        zipStream.write( bytes );
      }
    }
    finally
    {
      byteStream.close();
    }

    return byteStream.toByteArray();
  }
}
