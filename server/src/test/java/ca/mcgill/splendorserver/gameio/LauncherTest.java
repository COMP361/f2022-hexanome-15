package ca.mcgill.splendorserver.gameio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LauncherTest {

  private Launcher launcher = new Launcher();

  @Test
  void main() {
    String[] args = {"",""};
    launcher.main(args);
    assertEquals("", args[0]);
  }

}