package ca.mcgill.splendorserver.gameio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceJsonTest {
  @Test
  void GameServiceJson() {
    GameServiceJson gsJ = new GameServiceJson();
    assertNotNull(gsJ);
  }

  @Test
  void createGameServiceJson() {
    GameServiceJson gameServiceJson = new GameServiceJson("", "", "", "2",
      "4", "true");
    assertNotNull(gameServiceJson);
  }

}