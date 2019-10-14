package org.realityforge.gwt.symbolmap;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SoycSizeMapsDiffTest
  extends AbstractTest
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
                         "    <size type=\"field\" ref=\"arez.ArezContextHolder::c_zone\" size=\"1\"/>\n" +
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
    assertTrue( diff.hasDifferences() );
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
                  "ADDED type=field ref=arez.ArezContextHolder::c_zone size=1\n" );
  }

  @Test
  public void basicOperation_includeVar()
    throws Exception
  {
    final String before = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                          "<sizemaps>\n" +
                          "  <sizemap fragment=\"0\" size=\"74\">\n" +
                          "    <size type=\"var\" ref=\"c\" size=\"1\"/>\n" +
                          "    <size type=\"var\" ref=\"d\" size=\"2\"/>\n" +
                          "    <size type=\"var\" ref=\"e\" size=\"3\"/>\n" +
                          "  </sizemap>\n" +
                          "</sizemaps>\n";
    final String after = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                         "<sizemaps>\n" +
                         "  <sizemap fragment=\"0\" size=\"74\">\n" +
                         "    <size type=\"var\" ref=\"c\" size=\"1\"/>\n" +
                         "    <size type=\"var\" ref=\"e\" size=\"2\"/>\n" +
                         "    <size type=\"var\" ref=\"f\" size=\"2\"/>\n" +
                         "  </sizemap>\n" +
                         "</sizemaps>\n";
    final SoycSizeMaps beforeSizeMaps = readFromInput( before );
    final SoycSizeMaps afterSizeMaps = readFromInput( after );
    final SoycSizeMapsDiff diff = SoycSizeMapsDiff.diff( beforeSizeMaps, afterSizeMaps, true );
    assertEquals( diff.getBefore(), beforeSizeMaps );
    assertEquals( diff.getAfter(), afterSizeMaps );
    assertTrue( diff.hasDifferences() );
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
                 afterSizeMaps.getSizeMaps().get( 0 ).getSizes().get( 1 ) );
    assertEntry( entries.get( 2 ),
                 SoycSizeMapsDiff.DiffType.ADDED,
                 0,
                 null,
                 afterSizeMaps.getSizeMaps().get( 0 ).getSizes().get( 2 ) );

    assertEquals( diff.printToString(),
                  "REMOVED type=var ref=d size=2\n" +
                  "CHANGED type=var ref=e size=3->2 Size Delta=-1\n" +
                  "ADDED type=var ref=f size=2\n" );
  }

  @Test
  public void sortedDiff()
    throws Exception
  {
    final String before = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                          "<sizemaps>\n" +
                          "  <sizemap fragment=\"0\" size=\"74\">\n" +
                          "    <size type=\"type\" ref=\"arez.ArezContext\" size=\"35\"/>\n" +
                          "    <size type=\"field\" ref=\"arez.Observable::myField\" size=\"1\"/>\n" +
                          "    <size type=\"field\" ref=\"arez.ArezContextHolder::c_zone\" size=\"1\"/>\n" +
                          "    <size type=\"method\" ref=\"arez.ArezContextHolder::$clinit()V\" size=\"26\"/>\n" +
                          "    <size type=\"field\" ref=\"arez.ArezContextHolder::c_context\" size=\"1\"/>\n" +
                          "  </sizemap>\n" +
                          "</sizemaps>\n";
    final String after = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                         "<sizemaps>\n" +
                         "  <sizemap fragment=\"0\" size=\"74\">\n" +
                         "    <size type=\"type\" ref=\"arez.ArezContext\" size=\"35\"/>\n" +
                         "    <size type=\"field\" ref=\"arez.ArezContextHolder::c_context\" size=\"4\"/>\n" +
                         "    <size type=\"field\" ref=\"arez.Observable::myField\" size=\"3\"/>\n" +
                         "    <size type=\"field\" ref=\"arez.Observer::_observable\" size=\"324\"/>\n" +
                         "    <size type=\"field\" ref=\"arez.ArezContextHolder::c_zone\" size=\"1\"/>\n" +
                         "    <size type=\"method\" ref=\"arez.ArezContextHolder::$clinit()V\" size=\"26\"/>\n" +
                         "  </sizemap>\n" +
                         "</sizemaps>\n";
    final SoycSizeMaps beforeSizeMaps = readFromInput( before );
    final SoycSizeMaps afterSizeMaps = readFromInput( after );
    final SoycSizeMapsDiff diff = SoycSizeMapsDiff.diff( beforeSizeMaps, afterSizeMaps );
    assertEquals( diff.getBefore(), beforeSizeMaps );
    assertEquals( diff.getAfter(), afterSizeMaps );
    assertTrue( diff.hasDifferences() );
    final List<SoycSizeMapsDiff.Entry> entries = diff.getEntries();
    assertEquals( entries.size(), 3 );

    assertEquals( diff.printToString(),
                  "CHANGED type=field ref=arez.ArezContextHolder::c_context size=1->4 Size Delta=3\n" +
                  "CHANGED type=field ref=arez.Observable::myField size=1->3 Size Delta=2\n" +
                  "ADDED type=field ref=arez.Observer::_observable size=324\n" );
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
