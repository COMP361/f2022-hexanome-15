package ca.mcgill.splendorclient.view.lobbyservice;

import ca.mcgill.splendorclient.control.LobbyController;
import ca.mcgill.splendorclient.control.Splendor;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Represents the screen that shows sessions.
 */
public class LobbyScreen {
  private Scene scene;
  
  @Autowired
  private LobbyController controller;

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
   * Grabs the instance of the lobby controller.
   *
   * @return the lobby controller instance.
   */
  public LobbyController getLobbyController() {
    return controller;
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
      controller = fxmlLoader.getController();
      this.scene = scene;
    }
    return scene;
  }
}
