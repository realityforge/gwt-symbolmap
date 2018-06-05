package org.realityforge.gwt.symbolmap;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SymbolEntryIndexDiffTest
  extends AbstractSymbolMapTest
{
  private static final String HEADERS =
    "# jsName, jsniIdent, className, memberName, sourceUri, sourceLine, fragmentNumber\n";
  private static final String BEFORE = "# { 0 }\n" +
                                       "# { 'user.agent' : 'gecko1_8' }\n" +
                                       "# { 'user.agent' : 'ie10' }\n" +
                                       "# { 'user.agent' : 'ie8' }\n" +
                                       "# { 'user.agent' : 'ie9' }\n" +
                                       "# { 'user.agent' : 'safari' }\n" +
                                       HEADERS +
                                       "ArezContext,,arez.ArezContext,,arez/ArezContext.java,29,-1\n" +
                                       "r,arez.ArezContext::$action(Larez/ArezContext;Ljava/lang/String;Larez/TransactionMode;Larez/Procedure;ZLarez/Observer;[Ljava/lang/Object;)V,arez.ArezContext,$action,arez/ArezContext.java,1533,0\n" +
                                       "K,arez.CircularBuffer::$add(Larez/CircularBuffer;Ljava/lang/Object;)V,arez.CircularBuffer,$add,arez/CircularBuffer.java,63,0\n" +
                                       "Ic,com.google.gwt.core.client.JsDate::now()D,com.google.gwt.core.client.JsDate,now,com/google/gwt/core/client/JsDate.java,95,0\n" +
                                       "Scheduler,,com.google.gwt.core.client.Scheduler,,com/google/gwt/core/client/Scheduler.java,33,-1\n";
  private static final String AFTER = "# { 0 }\n" +
                                      "# { 'user.agent' : 'gecko1_8' }\n" +
                                      "# { 'user.agent' : 'ie10' }\n" +
                                      "# { 'user.agent' : 'ie8' }\n" +
                                      "# { 'user.agent' : 'ie9' }\n" +
                                      "# { 'user.agent' : 'safari' }\n" +
                                      HEADERS +
                                      "ArezContext,,arez.ArezContext,,arez/ArezContext.java,29,-1\n" +
                                      "CircularBuffer,,arez.CircularBuffer,,arez/CircularBuffer.java,13,-1\n" +
                                      "K,arez.CircularBuffer::$add(Larez/CircularBuffer;Ljava/lang/Object;)V,arez.CircularBuffer,$add,arez/CircularBuffer.java,63,0\n" +
                                      "Ic,com.google.gwt.core.client.JsDate::now()D,com.google.gwt.core.client.JsDate,now,com/google/gwt/core/client/JsDate.java,95,0\n" +
                                      "Scheduler,,com.google.gwt.core.client.Scheduler,,com/google/gwt/core/client/Scheduler.java,33,-1\n";

  @Test
  public void diff()
    throws Exception
  {
    final SymbolEntryIndex before = readIndexFromInput( BEFORE );
    final SymbolEntryIndex after = readIndexFromInput( AFTER );
    final SymbolEntryIndexDiff diff = SymbolEntryIndexDiff.diff( before, after );
    assertEquals( diff.getBefore(), before );
    assertEquals( diff.getAfter(), after );

    assertEquals( diff.getMissing().size(), 1 );
    assertEquals( diff.getMissing().get( 0 ).getJsName(), "r" );
    assertEquals( diff.getAdditional().size(), 1 );
    assertEquals( diff.getAdditional().get( 0 ).getJsName(), "CircularBuffer" );
    assertEquals( diff.getSame().size(), before.getSymbolEntries().size() - 1 );
    assertEquals( diff.getSame().size(), after.getSymbolEntries().size() - 1 );

    assertEquals( diff.printToString(),
                  "\n" +
                  "Symbols Removed:\n" +
                  "\n" +
                  "Symbol: arez.ArezContext::$action(Larez/ArezContext;Ljava/lang/String;Larez/TransactionMode;Larez/Procedure;ZLarez/Observer;[Ljava/lang/Object;)V\n" +
                  "Location: arez/ArezContext.java:1533\n" +
                  "\n" +
                  "\n" +
                  "Symbols Added:\n" +
                  "\n" +
                  "Symbol: arez.CircularBuffer\n" +
                  "Location: arez/CircularBuffer.java:13\n" +
                  "\n" );
  }

  @Test
  public void diff_TypeMissing()
    throws Exception
  {
    final SymbolEntryIndex before =
      readIndexFromInput( HEADERS + "ArezContext,,arez.ArezContext,,arez/ArezContext.java,29,-1\n" );
    final SymbolEntryIndex after = readIndexFromInput( HEADERS );
    final SymbolEntryIndexDiff diff = SymbolEntryIndexDiff.diff( before, after );
    assertEquals( diff.getBefore(), before );
    assertEquals( diff.getAfter(), after );

    assertEquals( diff.getMissing().size(), 1 );
    assertEquals( diff.getMissing().get( 0 ).getJsName(), "ArezContext" );
    assertEquals( diff.getAdditional().size(), 0 );
    assertEquals( diff.getSame().size(), 0 );
  }

  @Test
  public void diff_TypeAdded()
    throws Exception
  {
    final SymbolEntryIndex before =
      readIndexFromInput( HEADERS );
    final SymbolEntryIndex after =
      readIndexFromInput( HEADERS + "ArezContext,,arez.ArezContext,,arez/ArezContext.java,29,-1\n" );
    final SymbolEntryIndexDiff diff = SymbolEntryIndexDiff.diff( before, after );
    assertEquals( diff.getBefore(), before );
    assertEquals( diff.getAfter(), after );

    assertEquals( diff.getAdditional().size(), 1 );
    assertEquals( diff.getAdditional().get( 0 ).getJsName(), "ArezContext" );
    assertEquals( diff.getMissing().size(), 0 );
    assertEquals( diff.getSame().size(), 0 );
  }

  @Test
  public void diff_MemberMissing()
    throws Exception
  {
    final SymbolEntryIndex before =
      readIndexFromInput( HEADERS +
                          "r,arez.ArezContext::$action(Larez/ArezContext;Ljava/lang/String;Larez/TransactionMode;Larez/Procedure;ZLarez/Observer;[Ljava/lang/Object;)V,arez.ArezContext,$action,arez/ArezContext.java,1533,0\n" );
    final SymbolEntryIndex after = readIndexFromInput( HEADERS );
    final SymbolEntryIndexDiff diff = SymbolEntryIndexDiff.diff( before, after );
    assertEquals( diff.getBefore(), before );
    assertEquals( diff.getAfter(), after );

    assertEquals( diff.getMissing().size(), 1 );
    assertEquals( diff.getMissing().get( 0 ).getJsName(), "r" );
    assertEquals( diff.getAdditional().size(), 0 );
    assertEquals( diff.getSame().size(), 0 );
  }

  @Test
  public void diff_MemberAdded()
    throws Exception
  {
    final SymbolEntryIndex before = readIndexFromInput( HEADERS );
    final SymbolEntryIndex after =
      readIndexFromInput( HEADERS +
                          "r,arez.ArezContext::$action(Larez/ArezContext;Ljava/lang/String;Larez/TransactionMode;Larez/Procedure;ZLarez/Observer;[Ljava/lang/Object;)V,arez.ArezContext,$action,arez/ArezContext.java,1533,0\n" );
    final SymbolEntryIndexDiff diff = SymbolEntryIndexDiff.diff( before, after );
    assertEquals( diff.getBefore(), before );
    assertEquals( diff.getAfter(), after );

    assertEquals( diff.getAdditional().size(), 1 );
    assertEquals( diff.getAdditional().get( 0 ).getJsName(), "r" );
    assertEquals( diff.getMissing().size(), 0 );
    assertEquals( diff.getSame().size(), 0 );
  }

  @Test
  public void diff_same()
    throws Exception
  {
    final SymbolEntryIndex before = readIndexFromInput( BEFORE );
    final SymbolEntryIndex after = readIndexFromInput( BEFORE );
    final SymbolEntryIndexDiff diff = SymbolEntryIndexDiff.diff( before, after );
    assertEquals( diff.getBefore(), before );
    assertEquals( diff.getAfter(), after );
    assertEquals( diff.getMissing().size(), 0 );
    assertEquals( diff.getAdditional().size(), 0 );
    assertEquals( diff.getSame().size(), before.getSymbolEntries().size() );
  }
}
