package org.realityforge.gwt.symbolmap;

import com.csvreader.CsvReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import static org.testng.Assert.*;

/**
 * Represents a symbol in the GWT output.
 * These symbols are read from the symbol maps output by the GWT compiler. These symbol maps are typically placed
 * in extras directory at <code>WEB-INF/deploy/[my-module]/symbolMaps/[content-hash].symbolMap</code>.
 */
public final class SymbolEntry
{
  @Nonnull
  private final String _jsName;
  @Nonnull
  private final String _jsniIdent;
  @Nonnull
  private final String _className;
  @Nonnull
  private final String _memberName;
  @Nonnull
  private final String _sourceUri;
  private final int _sourceLine;
  private final int _fragmentNumber;

  private SymbolEntry( @Nonnull final String jsName,
                       @Nonnull final String jsniIdent,
                       @Nonnull final String className,
                       @Nonnull final String memberName,
                       @Nonnull final String sourceUri,
                       final int sourceLine,
                       final int fragmentNumber )
  {
    _jsName = Objects.requireNonNull( jsName );
    _jsniIdent = Objects.requireNonNull( jsniIdent );
    _className = Objects.requireNonNull( className );
    _memberName = Objects.requireNonNull( memberName );
    _sourceUri = Objects.requireNonNull( sourceUri );
    _sourceLine = sourceLine;
    _fragmentNumber = fragmentNumber;
  }

  /**
   * Read the symbol map from specified file and pass each symbol to supplied action.
   *
   * @param path   the path to the symbolMap file.
   * @param action the callback for each entry.
   * @throws IOException    if there is an error reading file.
   * @throws ParseException if there is an error parsing file.
   */
  static void readSymbolMap( @Nonnull final Path path, @Nonnull final Consumer<SymbolEntry> action )
    throws IOException, ParseException
  {
    final BufferedReader br = new BufferedReader( new FileReader( path.toFile() ) );

    int skippedLines = 0;
    boolean foundHeaders = false;
    while ( !foundHeaders )
    {
      // Skip the comment start of line
      assertEquals( br.read(), '#' );
      assertEquals( br.read(), ' ' );
      br.mark( 1 );
      final int ch = br.read();
      if ( '{' == ch )
      {
        br.readLine();
        skippedLines++;
      }
      else
      {
        br.reset();
        foundHeaders = true;
      }
    }

    final CsvReader reader = new CsvReader( br );
    if ( !reader.readHeaders() )
    {
      fail( "Failed to find headers in symbolFile " + path + " Skipped " + skippedLines + " permutation lines" );
    }
    checkHeader( reader, 0, "jsName" );
    checkHeader( reader, 1, "jsniIdent" );
    checkHeader( reader, 2, "className" );
    checkHeader( reader, 3, "memberName" );
    checkHeader( reader, 4, "sourceUri" );
    checkHeader( reader, 5, "sourceLine" );
    checkHeader( reader, 6, "fragmentNumber" );
    while ( reader.readRecord() )
    {
      final SymbolEntry entry =
        new SymbolEntry( reader.get( "jsName" ),
                         reader.get( "jsniIdent" ),
                         reader.get( "className" ),
                         reader.get( "memberName" ),
                         reader.get( "sourceUri" ),
                         Integer.parseInt( reader.get( "sourceLine" ) ),
                         Integer.parseInt( reader.get( "fragmentNumber" ) ) );
      action.accept( entry );
    }
    reader.close();
  }

  private static void checkHeader( @Nonnull final CsvReader reader, final int number, @Nonnull final String expected )
    throws ParseException, IOException
  {
    final String header = reader.getHeader( number );
    assertEquals( header,
                  expected,
                  String.format( "%nExpected Header :- [%50s],%nResolved Header :- [%50s]", expected, header ) );
  }

  /**
   * Return true if symbol represents a type rather than a member (such as a field or a method).
   *
   * @return true if symbol represents a type rather than a member (such as a field or a method).
   */
  public boolean isType()
  {
    return "".equals( _jsniIdent );
  }

  /**
   * Return true if symbol represents a method or field.
   *
   * @return true if symbol represents a method or field.
   */
  public boolean isMember()
  {
    return !isType();
  }

  /**
   * Return the javascript name of the symbol.
   *
   * @return the javascript name of the symbol.
   */
  @Nonnull
  public String getJsName()
  {
    return _jsName;
  }

  /**
   * Return the jsni identifier of the symbol.
   *
   * @return the jsni identifier of the symbol.
   */
  @Nonnull
  public String getJsniIdent()
  {
    return _jsniIdent;
  }

  /**
   * Return the name of the java class where the symbol is defined.
   *
   * @return the name of the java class where the symbol is defined.
   */
  @Nonnull
  public String getClassName()
  {
    return _className;
  }

  /**
   * Return the name of the java member that the symbol represents.
   *
   * @return the name of the java member that the symbol represents.
   */
  @Nonnull
  public String getMemberName()
  {
    return _memberName;
  }

  /**
   * Return the uri of source file in which symbol is defined.
   *
   * @return the uri of source file in which symbol is defined.
   */
  @Nonnull
  public String getSourceUri()
  {
    return _sourceUri;
  }

  /**
   * Return the line in the source file that defines symbol.
   *
   * @return the line in the source file that defines symbol.
   */
  public int getSourceLine()
  {
    return _sourceLine;
  }

  /**
   * Return the fragment number in which the symbol is output.
   *
   * @return the fragment number in which the symbol is output.
   */
  public int getFragmentNumber()
  {
    return _fragmentNumber;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return getJsName() + "," +
           getJsniIdent() + "," +
           getClassName() + "," +
           getMemberName() + "," +
           getSourceUri() + "," +
           getSourceLine() + "," +
           getFragmentNumber();
  }
}
