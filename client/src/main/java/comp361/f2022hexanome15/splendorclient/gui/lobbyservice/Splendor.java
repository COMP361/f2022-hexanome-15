package comp361.f2022hexanome15.splendorclient.gui.lobbyservice;


import java.io.IOException;
import java.util.Optional;

import comp361.f2022hexanome15.splendorclient.gui.gameboard.GameBoardView;
import comp361.f2022hexanome15.splendorclient.gui.scenemanager.SceneManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Represents the view of the entire game.
 * From login screen to lobby screen to game board screen.
 */
public class Splendor extends Application {

  private static Stage stage;

  @Override
  public void start(Stage stage) throws IOException {
    // scene initialization
    SceneManager.setLoginScreen(LoginScreen.getInstance().getLoginScene());
    SceneManager.setLobbyScreen(LobbyScreen.getInstance().getLobbyScene());
    SceneManager.setSettingsScreen(SettingsScreen.getInstance().getSettingsScene());
    SceneManager.setGameScreen(GameBoardView.setupGameBoard());

    stage.setResizable(false);
    Splendor.stage = stage;
    stage.setTitle("Login!");
    stage.setScene(SceneManager.getLoginScreen());
    stage.show();
  }

  /**
   * Transitions to the next scene.
   */
  public static void transitionTo(Scene scene, Optional<String> title) {
    stage.setScene(scene);
    if (title.isPresent()) {
      stage.setTitle(title.get());
      System.out.println("Transitioning to " + title.get());
    } else {
      stage.setTitle("");
    }
  }

  public static void main(String[] args) {
    launch();
  }
}