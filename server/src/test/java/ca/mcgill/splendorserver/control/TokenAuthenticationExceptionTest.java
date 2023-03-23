package ca.mcgill.splendorserver.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenAuthenticationExceptionTest {

  @Test
  void createTokenAuthenticationException() {
    TokenAuthenticationException authenticationException = new TokenAuthenticationException("Token Authentication Exception");
    assertNotEquals(null, authenticationException);
  }

}