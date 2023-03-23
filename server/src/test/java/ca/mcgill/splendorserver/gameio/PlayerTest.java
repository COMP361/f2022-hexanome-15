package ca.mcgill.splendorserver.gameio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

  private Player player = new Player("Sofia", "Purple");

  @Test
  public void getNameTest() {
    assertEquals("Sofia", player.getName());
  }
}