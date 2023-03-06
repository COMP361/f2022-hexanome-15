package ca.mcgill.splendorserver.model.tokens;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

class TokenPileTest {
  Token t1 = new Token(ONYX);
  Token t2 = new Token(ONYX);
  TokenPile tokenP = new TokenPile(ONYX);


  @Test
  void removeTokens() {
    tokenP.setUp(2);
    tokenP.removeTokens(tokenP.getSize());
    assertTrue(tokenP.getSize() == 0);

  }

  @Test
  void setUp() {
    tokenP.setUp(4);
    assertTrue(tokenP.getSize()<=35,"");
  }

  @Test
  void addToken() {
    tokenP.setUp(4);
    int fsize = tokenP.getSize();
    tokenP.addToken(t2);
    assertTrue(tokenP.getSize() > fsize);

  }

  @Test
  void removeToken() {
    tokenP.addToken(t1);
    tokenP.removeToken();
    assertTrue(tokenP.getSize() == 0);
  }

  @Test
  void getType() {
    assertEquals(ONYX,tokenP.getType(),"");
  }

  @Test
  void getSize() {
    assertEquals(0,tokenP.getSize(),"");
  }

  @Test
  void iterator() {
    tokenP.addToken(t1);
    tokenP.addToken(t2);
    Iterator<Token> i1 = tokenP.iterator();
    assertTrue(i1.hasNext(),"");
  }
}