package ca.mcgill.splendorserver.model.tokens;

import org.junit.jupiter.api.Test;

import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

  private Token token = new Token(ONYX);
  @Test
  void getType() {
    assertEquals(ONYX, token.getType());
  }
}