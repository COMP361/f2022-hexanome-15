package ca.mcgill.splendorserver.gameio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceAccountJsonTest {

  @Test
  void GameServiceAccountJsonTest() {
    GameServiceAccountJson gs =
      new GameServiceAccountJson("Jeff","12345","Blue");
    assertNotEquals(null, gs);

  }

  @Test
  void GameServiceAccountJsonTest2() {
    GameServiceAccountJson gs = new GameServiceAccountJson();
    assertNotEquals(null, gs);

  }

}