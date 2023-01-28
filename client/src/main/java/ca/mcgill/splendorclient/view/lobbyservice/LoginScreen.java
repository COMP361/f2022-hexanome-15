package ca.mcgill.splendorclient.view.lobbyservice;

import java.io.IOException;

import ca.mcgill.splendorclient.control.Splendor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Represents the login screen.
 */
public class LoginScreen {

  private Scene scene;

  /**
   * Login screen instance.
   */
  public static LoginScreen instance = new LoginScreen();

  private LoginScreen() {

  }

  /**
   * Returns this instance of login screen.
   *
   * @return this instance of login screen
   */
  public static LoginScreen getInstance() {
    return instance;
  }

  /**
   * Returns the current login scene.
   *
   * @return the current login scene
   * @throws IOException may throw an io exception
   */
  public Scene getLoginScene() throws IOException {
    if (scene == null) {
      FXMLLoader fxmlLoader = new FXMLLoader(Splendor.class.getResource("/hello-view.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
      this.scene = scene;
    }
    return scene;
  }

}