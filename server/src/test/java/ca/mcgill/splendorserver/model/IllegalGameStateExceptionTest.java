package ca.mcgill.splendorserver.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IllegalGameStateExceptionTest {

  @Test
  void createIllegalGameStateException() {
    IllegalGameStateException gameStateException = new IllegalGameStateException("Illegal Game State");
    assertNotEquals(null, gameStateException);
  }
}