package org.realityforge.gwt.symbolmap;

import java.nio.file.Path;
import java.util.ArrayList;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SymbolEntryTest
  extends AbstractSymbolMapTest
{
  @Test
  public void basicOperation()
    throws Exception
  {
    final String input =
      "# { 0 }\n" +
      "# { 'user.agent' : 'gecko1_8' }\n" +
      "# { 'user.agent' : 'ie10' }\n" +
      "# { 'user.agent' : 'ie8' }\n" +
      "# { 'user.agent' : 'ie9' }\n" +
      "# { 'user.agent' : 'safari' }\n" +
      "# jsName, jsniIdent, className, memberName, sourceUri, sourceLine, fragmentNumber\n" +
      "ArezContext,,arez.ArezContext,,arez/ArezContext.java,29,-1\n" +
      "r,arez.ArezContext::$action(Larez/ArezContext;Ljava/lang/String;Larez/TransactionMode;Larez/Procedure;ZLarez/Observer;[Ljava/lang/Object;)V,arez.ArezContext,$action,arez/ArezContext.java,1533,0\n";

    final Path file = createFileFromContent( input );
    final ArrayList<SymbolEntry> entries = new ArrayList<>();

    SymbolEntry.readSymbolMap( file, entries::add );

    assertEquals( entries.size(), 2 );
    final SymbolEntry symbolEntry1 = entries.get( 0 );
    final SymbolEntry symbolEntry2 = entries.get( 1 );

    assertEquals( symbolEntry1.getJsName(), "ArezContext" );
    assertEquals( symbolEntry1.getJsniIdent(), "" );
    assertEquals( symbolEntry1.getClassName(), "arez.ArezContext" );
    assertEquals( symbolEntry1.getMemberName(), "" );
    assertEquals( symbolEntry1.getSourceUri(), "arez/ArezContext.java" );
    assertEquals( symbolEntry1.getSourceLine(), "29" );
    assertEquals( symbolEntry1.getFragmentNumber(), "-1" );

    assertEquals( symbolEntry2.getJsName(), "r" );
    assertEquals( symbolEntry2.getJsniIdent(),
                  "arez.ArezContext::$action(Larez/ArezContext;Ljava/lang/String;Larez/TransactionMode;Larez/Procedure;ZLarez/Observer;[Ljava/lang/Object;)V" );
    assertEquals( symbolEntry2.getClassName(), "arez.ArezContext" );
    assertEquals( symbolEntry2.getMemberName(), "$action" );
    assertEquals( symbolEntry2.getSourceUri(), "arez/ArezContext.java" );
    assertEquals( symbolEntry2.getSourceLine(), "1533" );
    assertEquals( symbolEntry2.getFragmentNumber(), "0" );
  }

  @Test
  public void differentPermutationCount()
    throws Exception
  {
    final String input =
      "# { 0 }\n" +
      "# { 'user.agent' : 'gecko1_8' }\n" +
      "# { 'user.agent' : 'ie10' }\n" +
      "# jsName, jsniIdent, className, memberName, sourceUri, sourceLine, fragmentNumber\n" +
      "ArezContext,,arez.ArezContext,,arez/ArezContext.java,29,-1\n";

    final Path file = createFileFromContent( input );
    final ArrayList<SymbolEntry> entries = new ArrayList<>();

    SymbolEntry.readSymbolMap( file, entries::add );

    assertEquals( entries.size(), 1 );
    final SymbolEntry symbolEntry1 = entries.get( 0 );

    assertEquals( symbolEntry1.getJsName(), "ArezContext" );
    assertEquals( symbolEntry1.getJsniIdent(), "" );
    assertEquals( symbolEntry1.getClassName(), "arez.ArezContext" );
    assertEquals( symbolEntry1.getMemberName(), "" );
    assertEquals( symbolEntry1.getSourceUri(), "arez/ArezContext.java" );
    assertEquals( symbolEntry1.getSourceLine(), "29" );
    assertEquals( symbolEntry1.getFragmentNumber(), "-1" );
  }

  @Test
  public void badHeaders()
    throws Exception
  {
    final String input =
      "# { 0 }\n" +
      "# { 'user.agent' : 'gecko1_8' }\n" +
      "# { 'user.agent' : 'ie10' }\n" +
      "ArezContext,,arez.ArezContext,,arez/ArezContext.java,29,-1\n";

    final Path file = createFileFromContent( input );
    final ArrayList<SymbolEntry> entries = new ArrayList<>();

    assertThrows( () -> SymbolEntry.readSymbolMap( file, entries::add ) );

    assertEquals( entries.size(), 0 );
  }
}
