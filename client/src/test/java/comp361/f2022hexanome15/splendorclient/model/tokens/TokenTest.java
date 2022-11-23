package comp361.f2022hexanome15.splendorclient.model.tokens;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

  Token token;

  @BeforeEach
  void setUp() {
    token = new Token(TokenType.SAPPHIRE);
  }

  @Test
  void getColor() {
    assertEquals(Color.BLUE, token.getColor());
  }

  @Test
  void getType() {
    assertEquals(TokenType.SAPPHIRE, token.getType());
  }
}