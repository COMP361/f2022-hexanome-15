package ca.mcgill.splendorserver.gameio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerWrapperTest {
  PlayerWrapper pw = new PlayerWrapper("Jeff");
  PlayerWrapper pw2 = new PlayerWrapper("Jeff");
  PlayerWrapper pw3 = new PlayerWrapper("Ojas");

  @Test
  void testPlayerWrapper() {
    assertEquals(true,pw instanceof PlayerWrapper, "Test Passed");
  }


  @Test
  void testnewPlayerWrapper() {
    pw.newPlayerWrapper("Jeff");
    assertEquals(pw, pw.newPlayerWrapper("Jeff"), "Test Passed");
  }

  @Test
  void testgetName() {
    assertEquals
      ("Jeff",
        pw.getName(), "Test Passed");
  }

  @Test
  void testToString() {
    assertEquals("Player{Jeff}",pw.toString(),"");
  }

  @Test
  void testEquals() {
    assertTrue(pw.equals(pw2));
    assertTrue(pw!=pw3);
  }

  @Test
  void testHashCode() {
    assertEquals(2304890,pw.hashCode());
  }

}