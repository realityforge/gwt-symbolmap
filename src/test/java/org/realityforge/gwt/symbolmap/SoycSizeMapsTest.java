package org.realityforge.gwt.symbolmap;

import java.nio.file.Path;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SoycSizeMapsTest
  extends AbstractSymbolMapTest
{
  @Test
  public void parseGzipFile()
    throws Exception
  {
    final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                         "<sizemaps>\n" +
                         "  <sizemap fragment=\"0\" size=\"74\">\n" +
                         "    <size type=\"var\" ref=\"c\" size=\"1\"/>\n" +
                         "    <size type=\"string\" ref=\"number\" size=\"11\"/>\n" +
                         "    <size type=\"type\" ref=\"arez.ArezContext\" size=\"35\"/>\n" +
                         "    <size type=\"method\" ref=\"arez.ArezContextHolder::$clinit()V\" size=\"26\"/>\n" +
                         "    <size type=\"field\" ref=\"arez.ArezContextHolder::c_context\" size=\"1\"/>\n" +
                         "  </sizemap>\n" +
                         "</sizemaps>\n";
    final Path file = createGzipFileFromContent( input );
    SoycSizeMaps.readFromGzFile( file );
    final SoycSizeMaps sizeMaps = readFromInput( input );
    assertEquals( sizeMaps.getSizeMaps().size(), 1 );
    final SoycSizeMap soycSizeMap = sizeMaps.getSizeMaps().get( 0 );
    assertEquals( soycSizeMap.getFragment(), 0 );
    assertEquals( soycSizeMap.getSize(), 74 );
    assertEquals( soycSizeMap.getSizes().size(), 5 );
    assertNotNull( soycSizeMap.findSizeByRef( "c" ) );
    assertNull( soycSizeMap.findSizeByRef( "somethingelse" ) );
    assertSoycSize( soycSizeMap.getSizes().get( 0 ), SoycSize.Type.var, "c", 1 );
    assertSoycSize( soycSizeMap.getSizes().get( 1 ), SoycSize.Type.string, "number", 11 );
    assertSoycSize( soycSizeMap.getSizes().get( 2 ), SoycSize.Type.type, "arez.ArezContext", 35 );
    assertSoycSize( soycSizeMap.getSizes().get( 3 ), SoycSize.Type.method, "arez.ArezContextHolder::$clinit()V", 26 );
    assertSoycSize( soycSizeMap.getSizes().get( 4 ), SoycSize.Type.field, "arez.ArezContextHolder::c_context", 1 );

    assertEquals( sizeMaps.toString(), "<sizemaps>\n" +
                                       "<sizemap fragment=\"0\" size=\"74\">\n" +
                                       "<size type=\"var\" ref=\"c\" size=\"1\"/>\n" +
                                       "<size type=\"string\" ref=\"number\" size=\"11\"/>\n" +
                                       "<size type=\"type\" ref=\"arez.ArezContext\" size=\"35\"/>\n" +
                                       "<size type=\"method\" ref=\"arez.ArezContextHolder::$clinit()V\" size=\"26\"/>\n" +
                                       "<size type=\"field\" ref=\"arez.ArezContextHolder::c_context\" size=\"1\"/>\n" +
                                       "</sizemap>\n" +
                                       "\n" +
                                       "</sizemaps>\n" );
  }

  @Test
  public void basic()
    throws Exception
  {
    final String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                         "<sizemaps>\n" +
                         "  <sizemap fragment=\"0\" size=\"74\">\n" +
                         "    <size type=\"var\" ref=\"c\" size=\"1\"/>\n" +
                         "    <size type=\"string\" ref=\"number\" size=\"11\"/>\n" +
                         "    <size type=\"type\" ref=\"arez.ArezContext\" size=\"35\"/>\n" +
                         "    <size type=\"method\" ref=\"arez.ArezContextHolder::$clinit()V\" size=\"26\"/>\n" +
                         "    <size type=\"field\" ref=\"arez.ArezContextHolder::c_context\" size=\"1\"/>\n" +
                         "  </sizemap>\n" +
                         "</sizemaps>\n";
    final SoycSizeMaps sizeMaps = readFromInput( input );
    assertEquals( sizeMaps.getSizeMaps().size(), 1 );
    final SoycSizeMap soycSizeMap = sizeMaps.getSizeMaps().get( 0 );
    assertEquals( soycSizeMap.getFragment(), 0 );
    assertEquals( soycSizeMap.getSize(), 74 );
    assertEquals( soycSizeMap.getSizes().size(), 5 );
    assertNotNull( soycSizeMap.findSizeByRef( "c" ) );
    assertNull( soycSizeMap.findSizeByRef( "somethingelse" ) );
    assertSoycSize( soycSizeMap.getSizes().get( 0 ), SoycSize.Type.var, "c", 1 );
    assertSoycSize( soycSizeMap.getSizes().get( 1 ), SoycSize.Type.string, "number", 11 );
    assertSoycSize( soycSizeMap.getSizes().get( 2 ), SoycSize.Type.type, "arez.ArezContext", 35 );
    assertSoycSize( soycSizeMap.getSizes().get( 3 ), SoycSize.Type.method, "arez.ArezContextHolder::$clinit()V", 26 );
    assertSoycSize( soycSizeMap.getSizes().get( 4 ), SoycSize.Type.field, "arez.ArezContextHolder::c_context", 1 );

    assertEquals( sizeMaps.toString(), "<sizemaps>\n" +
                                       "<sizemap fragment=\"0\" size=\"74\">\n" +
                                       "<size type=\"var\" ref=\"c\" size=\"1\"/>\n" +
                                       "<size type=\"string\" ref=\"number\" size=\"11\"/>\n" +
                                       "<size type=\"type\" ref=\"arez.ArezContext\" size=\"35\"/>\n" +
                                       "<size type=\"method\" ref=\"arez.ArezContextHolder::$clinit()V\" size=\"26\"/>\n" +
                                       "<size type=\"field\" ref=\"arez.ArezContextHolder::c_context\" size=\"1\"/>\n" +
                                       "</sizemap>\n" +
                                       "\n" +
                                       "</sizemaps>\n" );
  }

  private void assertSoycSize( @Nonnull final SoycSize soycSize,
                               @Nonnull final SoycSize.Type type,
                               @Nonnull final String ref,
                               final int size )
  {
    assertEquals( soycSize.getType(), type );
    assertEquals( soycSize.getRef(), ref );
    assertEquals( soycSize.getSize(), size );
  }

  @SuppressWarnings( "SameParameterValue" )
  @Nonnull
  private SoycSizeMaps readFromInput( @Nonnull final String input )
    throws Exception
  {
    return SoycSizeMaps.readFromFile( createFileFromContent( input ) );
  }
}
