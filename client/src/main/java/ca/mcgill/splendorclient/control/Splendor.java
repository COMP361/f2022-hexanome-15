package ca.mcgill.splendorclient.control;

import ca.mcgill.splendorclient.view.gameboard.GameBoardView;
import ca.mcgill.splendorclient.view.lobbyservice.LobbyScreen;
import ca.mcgill.splendorclient.view.lobbyservice.LoginScreen;
import ca.mcgill.splendorclient.view.lobbyservice.SettingsScreen;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kong.unirest.json.JSONObject;

/**
 * Represents the view of the entire game.
 * From login screen to lobby screen to game board screen.
 */
public class Splendor extends Application {

  private static Stage stage;
  private static UpdateGetter updateGetter;

  /**
   * Creates a Splendor object.
   */
  public Splendor() {

  }

  @Override
  public void start(Stage stage) throws IOException {
    // scene initialization
    SceneManager.setLoginScreen(LoginScreen.getInstance().getLoginScene());
    SceneManager.setLobbyScreen(LobbyScreen.getInstance().getLobbyScene());
    SceneManager.setSettingsScreen(SettingsScreen.getInstance().getSettingsScene());

    stage.setResizable(false);
    Splendor.stage = stage;
    stage.setTitle("Login!");
    stage.setScene(SceneManager.getLoginScreen());
    stage.show();
  }

  /**
   * Transitions to the next scene.
   * TODO: Make this the state machine pattern. Each scene is a state with a transition to function.
   *
   * @param scene the scene to transition to
   * @param title the title of the screen
   */
  public static void transitionTo(Scene scene, Optional<String> title) {
    stage.setScene(scene);
    if (title.get() == "Game Screen") {
      //get the game info from the server
      
    }
    if (title.isPresent()) {
      stage.setTitle(title.get());
      System.out.println("Transitioning to " + title.get());
    } else {
      stage.setTitle("");
    }
  }
  
  public static void transitionToGameScreen(long gameId, JSONObject sessionInfo) {
    SceneManager.setGameScreen(GameBoardView.setupGameBoard(sessionInfo.getJSONArray("players")));
    stage.setScene(SceneManager.getGameScreen());
    //get game info from server such as player names creator etc.
    
    //launch update getter TODO: somehow get the session id over from the lobby service.
    updateGetter = new UpdateGetter(gameId);
    new Thread(updateGetter).start();
  }

  /**
   * Exits the game.
   */
  public static void exitGame() {
    assert updateGetter != null;
    updateGetter.exit();
  }

  /**
   * Launches the Splendor application.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    launch();
  }
}
