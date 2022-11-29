package ca.mcgill.splendorclient.gui.lobbyservice;

import java.io.IOException;
import java.util.Optional;

import ca.mcgill.splendorclient.gui.gameboard.GameBoardView;
import ca.mcgill.splendorclient.gui.scenemanager.SceneManager;
import ca.mcgill.splendorclient.model.action.UpdateGetter;
import ca.mcgill.splendorclient.model.cards.GameBoard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Represents the view of the entire game.
 * From login screen to lobby screen to game board screen.
 */
public class Splendor extends Application {

  private static Stage stage;
  private static UpdateGetter updateGetter;

  @Override
  public void start(Stage stage) throws IOException {
    // scene initialization
    SceneManager.setLoginScreen(LoginScreen.getInstance().getLoginScene());
    SceneManager.setLobbyScreen(LobbyScreen.getInstance().getLobbyScene());
    SceneManager.setSettingsScreen(SettingsScreen.getInstance().getSettingsScene());
    //TODO: Only do this once we have knowledge of the session.
    SceneManager.setGameScreen(GameBoardView.setupGameBoard());

    stage.setResizable(false);
    Splendor.stage = stage;
    stage.setTitle("Login!");
    stage.setScene(SceneManager.getLoginScreen());
    stage.show();
  }

  /**
   * Transitions to the next scene. TODO: Make this the state machine pattern. Each scene is a state with a transition to function.
   */
  public static void transitionTo(Scene scene, Optional<String> title) {
    stage.setScene(scene);
    if (title.get() == "Game Screen") {
      //update server with game info
      GameBoard gameBoard = GameBoardView.getGameBoard();
      //TODO: make this an actual gameid
      gameBoard.sendToServer(1);
      
      //launch update getter TODO: somehow get the session id over from the lobby service.
      updateGetter = new UpdateGetter(1);
      new Thread(updateGetter).start();
    }
    if (title.isPresent()) {
      stage.setTitle(title.get());
      System.out.println("Transitioning to " + title.get());
    } else {
      stage.setTitle("");
    }
  }
  
  public static void exitGame() {
    assert updateGetter != null;
    updateGetter.exit();
  }

  public static void main(String[] args) {
    launch();
  }
}
