package ca.mcgill.splendorserver.model.tokens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

class TokenPileTest {

  private TokenPile tokenP;

  @BeforeEach
  void setUp() {
    tokenP = new TokenPile(ONYX);
  }

  @Test
  void removeTokens() {
    Token t1 = new Token(ONYX);
    Token t2 = new Token(ONYX);
    tokenP.addToken(t1);
    tokenP.addToken(t2);
    tokenP.removeTokens(tokenP.getSize());
    assertEquals(0, tokenP.getSize());

  }

  @Test
  void setUpTwoPlayers() {
    tokenP.setUp(2);
    assertEquals(4, tokenP.getSize());
  }

  @Test
  void setUpThreePlayers() {
    tokenP.setUp(3);
    assertEquals(5, tokenP.getSize());
  }

  @Test
  void setUpFourPlayers() {
    tokenP.setUp(4);
    assertEquals(7, tokenP.getSize());
  }

  @Test
  void setUpGoldPile() {
    TokenPile gold = new TokenPile(GOLD);
    gold.setUp(2);
    assertEquals(5, gold.getSize());
  }

  @Test
  void addToken() {
    Token t1 = new Token(ONYX);
    tokenP.addToken(t1);
    assertEquals(1, tokenP.getSize());

  }

  @Test
  void removeToken() {
    Token t1 = new Token(ONYX);
    tokenP.addToken(t1);
    tokenP.removeToken();
    assertEquals(0, tokenP.getSize());
  }

  @Test
  void getType() {
    assertEquals(ONYX,tokenP.getType());
  }

  @Test
  void getSize() {
    assertEquals(0,tokenP.getSize());
  }

  @Test
  void iterator() {
    Token t1 = new Token(ONYX);
    Token t2 = new Token(ONYX);
    tokenP.addToken(t1);
    tokenP.addToken(t2);
    Iterator<Token> i1 = tokenP.iterator();
    assertTrue(i1.hasNext(),"");
  }

}