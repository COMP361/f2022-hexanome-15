package ca.mcgill.splendorserver.games;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerWrapperTest {

  @Test
  void newPlayerWrapper() {
  }

  @Test
  void getaUserName() {
    PlayerWrapper.newPlayerWrapper("Zack", 20);
    PlayerWrapper.newPlayerWrapper("Ojas", 21).getName();
    assertEquals
      ("Ojas",
       PlayerWrapper.newPlayerWrapper("Ojas", null).getName(), "");
  }
}
