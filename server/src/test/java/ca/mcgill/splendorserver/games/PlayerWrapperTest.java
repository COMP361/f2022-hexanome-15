package ca.mcgill.splendorserver.games;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerWrapperTest {

  @Test
  void newPlayerWrapper() {
  }

  @Test
  void getaUserName() {
    PlayerWrapper.newPlayerWrapper("Zack");
    PlayerWrapper.newPlayerWrapper("Ojas").getaUserName();
    assertEquals
      ("Ojas",
        PlayerWrapper.newPlayerWrapper("Ojas").getaUserName(), "");
  }
}
