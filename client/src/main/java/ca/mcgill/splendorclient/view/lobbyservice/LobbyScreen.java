package ca.mcgill.splendorclient.view.lobbyservice;

import java.io.IOException;

import ca.mcgill.splendorclient.control.Splendor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Represents the screen that shows sessions.
 */
public class LobbyScreen {
  private Scene scene;

  /**
   * Lobby screen instance.
   */
  public static LobbyScreen instance = new LobbyScreen();

  private LobbyScreen() {

  }

  /**
   * Returns this instance of the lobby screen.
   *
   * @return this instance of the lobby screen
   */
  public static LobbyScreen getInstance() {
    return instance;
  }

  /**
   * Returns the current scene of the lobby service.
   *
   * @return the current scene of the lobby service
   * @throws IOException an io exception can be thrown
   */
  public Scene getLobbyScene() throws IOException {
    if (scene == null) {
      FXMLLoader fxmlLoader = new FXMLLoader(Splendor.class.getResource("/lobby-view.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
      this.scene = scene;
    }
    return scene;
  }
}
