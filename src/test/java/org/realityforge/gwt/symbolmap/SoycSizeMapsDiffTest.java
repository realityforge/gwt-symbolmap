package org.realityforge.gwt.symbolmap;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SoycSizeMapsDiffTest
  extends AbstractSymbolMapTest
{
  @Test
  public void basicOperation()
    throws Exception
  {
    final String before = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                          "<sizemaps>\n" +
                          "  <sizemap fragment=\"0\" size=\"74\">\n" +
                          "    <size type=\"string\" ref=\"number\" size=\"11\"/>\n" +
                          "    <size type=\"type\" ref=\"arez.ArezContext\" size=\"35\"/>\n" +
                          "    <size type=\"method\" ref=\"arez.ArezContextHolder::$clinit()V\" size=\"26\"/>\n" +
                          "    <size type=\"field\" ref=\"arez.ArezContextHolder::c_context\" size=\"1\"/>\n" +
                          "  </sizemap>\n" +
                          "</sizemaps>\n";
    final String after = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                         "<sizemaps>\n" +
                         "  <sizemap fragment=\"0\" size=\"74\">\n" +
                         "    <size type=\"var\" ref=\"c\" size=\"1\"/>\n" +
                         "    <size type=\"string\" ref=\"number\" size=\"11\"/>\n" +
                         "    <size type=\"method\" ref=\"arez.ArezContextHolder::$clinit()V\" size=\"22\"/>\n" +
                         "    <size type=\"field\" ref=\"arez.ArezContextHolder::c_context\" size=\"1\"/>\n" +
                         "  </sizemap>\n" +
                         "</sizemaps>\n";
    final SoycSizeMaps beforeSizeMaps = readFromInput( before );
    final SoycSizeMaps afterSizeMaps = readFromInput( after );
    final SoycSizeMapsDiff diff = SoycSizeMapsDiff.diff( beforeSizeMaps, afterSizeMaps );
    assertEquals( diff.getBefore(), beforeSizeMaps );
    assertEquals( diff.getAfter(), afterSizeMaps );
    assertEquals( diff.hasDifferences(), true );
    final List<SoycSizeMapsDiff.Entry> entries = diff.getEntries();
    assertEquals( entries.size(), 3 );
    assertEntry( entries.get( 0 ),
                 SoycSizeMapsDiff.DiffType.REMOVED,
                 0,
                 beforeSizeMaps.getSizeMaps().get( 0 ).getSizes().get( 1 ),
                 null );
    assertEntry( entries.get( 1 ),
                 SoycSizeMapsDiff.DiffType.CHANGED,
                 0,
                 beforeSizeMaps.getSizeMaps().get( 0 ).getSizes().get( 2 ),
                 afterSizeMaps.getSizeMaps().get( 0 ).getSizes().get( 2 ) );
    assertEntry( entries.get( 2 ),
                 SoycSizeMapsDiff.DiffType.ADDED,
                 0,
                 null,
                 afterSizeMaps.getSizeMaps().get( 0 ).getSizes().get( 0 ) );

    assertEquals( diff.printToString(),
                  "REMOVED type=type ref=arez.ArezContext size=35\n" +
                  "CHANGED type=method ref=arez.ArezContextHolder::$clinit()V size=26->22 Size Delta=-4\n" +
                  "ADDED type=var ref=c size=1\n" );
  }

  @SuppressWarnings( "SameParameterValue" )
  private void assertEntry( @Nonnull final SoycSizeMapsDiff.Entry entry,
                            @Nonnull final SoycSizeMapsDiff.DiffType type,
                            final int fragment,
                            @Nullable final SoycSize lhs,
                            @Nullable final SoycSize rhs )
  {
    assertEquals( entry.getType(), type );
    assertEquals( entry.getFragment(), fragment );
    assertEquals( entry.getLhs(), lhs );
    assertEquals( entry.getRhs(), rhs );
  }

  @Nonnull
  private SoycSizeMaps readFromInput( @Nonnull final String input )
    throws Exception
  {
    return SoycSizeMaps.readFromFile( createFileFromContent( input ) );
  }
}
