package ca.mcgill.splendorserver.control;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class AuthTokenAuthenticatorTest {

  /*@Test
  void getRole() {
    assertEquals(Optional.empty(),AuthTokenAuthenticator.getRole("12345"),"");

  }

  @Test
  void getUserName() {
    assertEquals(Optional.empty(),AuthTokenAuthenticator.getUserName("12345"),"");

  }

  @Test
  void isSameName() {
    assertEquals(false,AuthTokenAuthenticator.isSameName("slava","12345"),"");
  }

  @Test
  void isPlayer() {
    assertEquals(false,AuthTokenAuthenticator.isPlayer("12345"),"");
  }

  @Test()
  void authenticate() {;
    TokenAuthenticationException thrown = assertThrows(
      TokenAuthenticationException.class,
      () -> AuthTokenAuthenticator.authenticate("slava","12345"),
      "Username slava invalid against access token 12345");
  }*/
}