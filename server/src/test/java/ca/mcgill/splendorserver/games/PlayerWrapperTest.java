package ca.mcgill.splendorserver.games;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerWrapperTest {

  @Test
  void newPlayerWrapper() {
  }

  @Test
  void getaUserName() {
    PlayerWrapper.newPlayerWrapper("Zack");
    PlayerWrapper.newPlayerWrapper("Ojas").getName();
    assertEquals
      ("Ojas",
       PlayerWrapper.newPlayerWrapper("Ojas").getName(), "");
  }
}
