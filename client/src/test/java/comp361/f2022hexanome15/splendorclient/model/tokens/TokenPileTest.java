package comp361.f2022hexanome15.splendorclient.model.tokens;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenPileTest {

  TokenPile tokenPile;

  @BeforeEach
  void setUp() {
    tokenPile = new TokenPile(TokenType.SAPPHIRE);
  }
  @Test
  void addToken() {
  }

  @Test
  void removeToken() {
  }

  @Test
  void getType() {
    assertEquals(TokenType.SAPPHIRE, tokenPile.getType());
  }

  @Test
  void getSize() {
    assertEquals(0, tokenPile.getSize());
  }

  @Test
  void getColor() {
    assertEquals(Color.BLUE, tokenPile.getColor());
  }
}