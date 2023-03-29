package ca.mcgill.splendorserver.model.action;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveInfoTest {

  @Test
  void createMoveInfo() {
    MoveInfo moveInfo = new MoveInfo("Sofia", "PURCHASE_DEV", "0", null, null, null);
    assertNotEquals(null, moveInfo);
  }

}