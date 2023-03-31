package ca.mcgill.splendorclient.view.lobbyservice;

import ca.mcgill.splendorclient.control.Splendor;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Represents the settings screen.
 */
public class SettingsScreen {
  private Scene scene;

  /**
   * Instance of the settings screen.
   */
  public static SettingsScreen instance = new SettingsScreen();

  private SettingsScreen() {

  }

  /**
   * Returns this instance of the settings screen.
   *
   * @return this instance of the settings screen.
   */
  public static SettingsScreen getInstance() {
    return instance;
  }

  /**
   * Returns the current settings scene.
   *
   * @return the current settings scene
   * @throws IOException may throw an io exception
   */
  public Scene getSettingsScene() throws IOException {
    if (scene == null) {
      FXMLLoader fxmlLoader = new FXMLLoader(Splendor.class.getResource("/settings-view.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
      this.scene = scene;
    }
    return scene;
  }
}
