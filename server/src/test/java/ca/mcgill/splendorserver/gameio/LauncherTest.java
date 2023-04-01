package ca.mcgill.splendorserver.gameio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LauncherTest {

  @Test
  void createLauncher() {
    Launcher launcher = new Launcher();
    assertNotNull(launcher);
  }

}