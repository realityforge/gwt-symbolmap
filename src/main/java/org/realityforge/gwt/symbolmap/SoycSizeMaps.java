package org.realityforge.gwt.symbolmap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class SoycSizeMaps
{
  private final List<SoycSizeMap> _sizeMaps = new ArrayList<>();

  private SoycSizeMaps()
  {
  }

  /**
   * Return a sizemap per fragment.
   *
   * @return a sizemap per fragment.
   */
  @Nonnull
  public List<SoycSizeMap> getSizeMaps()
  {
    return Collections.unmodifiableList( _sizeMaps );
  }

  @Override
  public String toString()
  {
    return "<sizemaps>\n" +
           _sizeMaps.stream().map( SoycSizeMap::toString ).collect( Collectors.joining( "\n" ) ) + "\n" +
           "</sizemaps>\n";
  }

  /**
   * Read soyc size maps from file.
   *
   * @param path the path to load size maps from.
   * @return the new SoycSizeMaps.
   * @throws Exception if there is an error reading or parsing file.
   */
  @Nonnull
  public static SoycSizeMaps readFromFile( @Nonnull final Path path )
    throws Exception
  {
    return readFromInputStream( new FileInputStream( path.toFile() ) );
  }

  /**
   * Read soyc size maps from file.
   *
   * @param path the path to load size maps from.
   * @return the new SoycSizeMaps.
   * @throws Exception if there is an error reading or parsing file.
   */
  @Nonnull
  public static SoycSizeMaps readFromGzFile( @Nonnull final Path path )
    throws Exception
  {
    return readFromInputStream( new GZIPInputStream( new FileInputStream( path.toFile() ) ) );
  }

  @Nonnull
  private static SoycSizeMaps readFromInputStream( @Nonnull final InputStream inputStream )
    throws ParserConfigurationException, SAXException, IOException
  {
    final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    final Document doc = builder.parse( inputStream );
    doc.getDocumentElement().normalize();

    return parse( doc );
  }

  @Nonnull
  private static SoycSizeMaps parse( @Nonnull final Document doc )
  {
    final SoycSizeMaps maps = new SoycSizeMaps();
    final NodeList sizemapList = doc.getElementsByTagName( "sizemap" );
    for ( int i = 0; i < sizemapList.getLength(); i++ )
    {
      final SoycSizeMap soycSizeMap = SoycSizeMap.parse( (Element) sizemapList.item( i ) );
      maps._sizeMaps.add( soycSizeMap );
    }

    return maps;
  }
}
