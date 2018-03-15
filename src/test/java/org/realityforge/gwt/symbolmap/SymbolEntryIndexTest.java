package org.realityforge.gwt.symbolmap;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SymbolEntryIndexTest
  extends AbstractSymbolMapTest
{
  private static final String DEFAULT_INPUT =
    "# { 0 }\n" +
    "# { 'user.agent' : 'gecko1_8' }\n" +
    "# { 'user.agent' : 'ie10' }\n" +
    "# { 'user.agent' : 'ie8' }\n" +
    "# { 'user.agent' : 'ie9' }\n" +
    "# { 'user.agent' : 'safari' }\n" +
    "# jsName, jsniIdent, className, memberName, sourceUri, sourceLine, fragmentNumber\n" +
    "ArezContext,,arez.ArezContext,,arez/ArezContext.java,29,-1\n" +
    "r,arez.ArezContext::$action(Larez/ArezContext;Ljava/lang/String;Larez/TransactionMode;Larez/Procedure;ZLarez/Observer;[Ljava/lang/Object;)V,arez.ArezContext,$action,arez/ArezContext.java,1533,0\n" +
    "CircularBuffer,,arez.CircularBuffer,,arez/CircularBuffer.java,13,-1\n" +
    "K,arez.CircularBuffer::$add(Larez/CircularBuffer;Ljava/lang/Object;)V,arez.CircularBuffer,$add,arez/CircularBuffer.java,63,0\n" +
    "Ic,com.google.gwt.core.client.JsDate::now()D,com.google.gwt.core.client.JsDate,now,com/google/gwt/core/client/JsDate.java,95,0\n" +
    "Scheduler,,com.google.gwt.core.client.Scheduler,,com/google/gwt/core/client/Scheduler.java,33,-1\n";

  @Test
  public void findSymbolsByClassName()
    throws Exception
  {
    final SymbolEntryIndex index = readDefaultIndex();

    {
      final List<SymbolEntry> symbols = index.findSymbolsByClassName( "arez\\..*" );
      assertEquals( symbols.size(), 4 );
      assertEquals( symbols.get( 0 ).getJsName(), "ArezContext" );
      assertEquals( symbols.get( 1 ).getJsName(), "r" );
      assertEquals( symbols.get( 2 ).getJsName(), "CircularBuffer" );
      assertEquals( symbols.get( 3 ).getJsName(), "K" );
    }

    {
      final List<SymbolEntry> symbols = index.findSymbolsByClassName( "arez\\.ArezContext" );
      assertEquals( symbols.size(), 2 );
      assertEquals( symbols.get( 0 ).getJsName(), "ArezContext" );
      assertEquals( symbols.get( 1 ).getJsName(), "r" );
    }
  }

  @Test
  public void findSymbolsByPatterns()
    throws Exception
  {
    final SymbolEntryIndex index = readDefaultIndex();

    {
      final List<SymbolEntry> symbols = index.findSymbolsByPatterns( "arez\\..*", "\\$action" );
      assertEquals( symbols.size(), 1 );
      assertEquals( symbols.get( 0 ).getJsName(), "r" );
    }

    {
      final List<SymbolEntry> symbols = index.findSymbolsByPatterns( "arez\\..*", "\\$a.*" );
      assertEquals( symbols.size(), 2 );
      assertEquals( symbols.get( 0 ).getJsName(), "r" );
      assertEquals( symbols.get( 1 ).getJsName(), "K" );
    }

    {
      final List<SymbolEntry> symbols = index.findSymbolsByPatterns( "arez\\.ArezContext", "\\$a.*" );
      assertEquals( symbols.size(), 1 );
      assertEquals( symbols.get( 0 ).getJsName(), "r" );
    }
  }

  @Test
  public void findByJsName()
    throws Exception
  {
    final SymbolEntryIndex index = readDefaultIndex();

    final SymbolEntry symbol1 = index.findByJsName( "r" );
    assertNotNull( symbol1 );
    assertEquals( symbol1.getJsName(), "r" );

    final SymbolEntry symbol2 = index.findByJsName( "K" );
    assertNotNull( symbol2 );
    assertEquals( symbol2.getJsName(), "K" );

    final SymbolEntry symbol3 = index.findByJsName( "NotFound" );
    assertNull( symbol3 );
  }

  @Test
  public void findByJsniIdentifier()
    throws Exception
  {
    final SymbolEntryIndex index = readDefaultIndex();

    final SymbolEntry symbol1 = index.findByJsniIdentifier( "arez.ArezContext::$action(Larez/ArezContext;Ljava/lang/String;Larez/TransactionMode;Larez/Procedure;ZLarez/Observer;[Ljava/lang/Object;)V" );
    assertNotNull( symbol1 );
    assertEquals( symbol1.getJsName(), "r" );

    final SymbolEntry symbol2 = index.findByJsniIdentifier( "arez.CircularBuffer::$add(Larez/CircularBuffer;Ljava/lang/Object;)V" );
    assertNotNull( symbol2 );
    assertEquals( symbol2.getJsName(), "K" );

    final SymbolEntry symbol3 = index.findByJsniIdentifier( "NotFound" );
    assertNull( symbol3 );
  }

  @Nonnull
  private SymbolEntryIndex readDefaultIndex()
    throws IOException, ParseException
  {
    return readIndexFromInput( DEFAULT_INPUT );
  }

  @Nonnull
  private SymbolEntryIndex readIndexFromInput( @Nonnull final String input )
    throws IOException, ParseException
  {
    final Path file = createFileFromContent( input );
    return SymbolEntryIndex.readSymbolMapIntoIndex( file );
  }
}
