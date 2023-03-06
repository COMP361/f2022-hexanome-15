package ca.mcgill.splendorserver.gameio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceAccountJsonTest {

  @Test
  void GameServiceAccountJsonTest() {
    GameServiceAccountJson gs =
      new GameServiceAccountJson("Jeff","12345","Blue");

    GameServiceAccountJson gs2 =
      new GameServiceAccountJson("Jeff","12345","Blue");
    assertTrue(gs != gs2,"");

  }

  @Test
  void GameServiceAccountJsonTest2() {
    GameServiceAccountJson gs3 = new GameServiceAccountJson();
    assertTrue(gs3 != null,"");

  }

}