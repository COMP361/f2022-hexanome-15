package ca.mcgill.splendorserver.gameio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerWrapperTest {

  private PlayerWrapper pw = PlayerWrapper.newPlayerWrapper("Jeff");

  @Test
  void testGetName() {
    assertEquals("Jeff", pw.getName());
  }

  @Test
  void testToString() {
    assertEquals("Player{Jeff}", pw.toString());
  }

  @Test
  void testNotEqualsNull() {
    assertFalse(pw.equals(null));
  }

  @Test
  void testNotEquals() {
    PlayerWrapper pw2 = PlayerWrapper.newPlayerWrapper("Sofia");
    assertNotEquals(pw, pw2);
  }

  @Test
  void testEqualsSamePlayer() {
    assertEquals(pw, pw);
  }

  @Test
  void testHashCode() {
    assertEquals(2304890,pw.hashCode());
  }

}
