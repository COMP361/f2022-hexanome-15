package gui.lobbyservice;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Represents the screen that shows sessions.
 */
public class LobbyScreen {
  private Scene scene;

  public static LobbyScreen instance = new LobbyScreen();

  private LobbyScreen() {

  }

  public static LobbyScreen getInstance() {
    return instance;
  }

  /**
   * Returns the current scene of the lobby service.
   *
   * @return the current scene of the lobby service
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
