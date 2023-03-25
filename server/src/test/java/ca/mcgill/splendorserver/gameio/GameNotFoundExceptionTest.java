package ca.mcgill.splendorserver.gameio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameNotFoundExceptionTest {

  @Test
  void testGameNotFoundException() {
    Long i = 0L;
    GameNotFoundException e = new GameNotFoundException(i);
    assertEquals("Could not find game ID: " + i, e.getMessage());
  }
}