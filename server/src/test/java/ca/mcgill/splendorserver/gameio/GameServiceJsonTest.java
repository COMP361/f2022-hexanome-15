package ca.mcgill.splendorserver.gameio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceJsonTest {
  @Test
  void GameServiceJson() {
    GameServiceJson gsJ = new GameServiceJson();
    assertNotEquals(null, gsJ);
  }

}