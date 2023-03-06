package ca.mcgill.splendorserver.gameio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoadDatabaseTest {

  @Test
  void LoadDatabaseTest() {
    LoadDatabase ldb = new LoadDatabase();
    assertTrue(ldb != null,"");
  }
}
