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
   *
   * @param scene the scene to transition to
   * @param title the title of the screen
   */
  public static void transitionTo(Scene scene, Optional<String> title) {
    stage.setScene(scene);
    if (title.get().equals("Splendor Lobby")) {
      //absolutely terrible, but works for now. 
      LobbyScreen.getInstance().getLobbyController().setExitThread(false);
    }
    if (title.isPresent()) {
      stage.setTitle(title.get());
      System.out.println("Transitioning to " + title.get());
    } else {
      stage.setTitle("");
    }
  }

  /**
   * Transitions to the game screen.
   *
   * @param gameId the id of the game
   * @param sessionInfo the session info of the game
   */
  public static void transitionToGameScreen(long gameId, JSONObject sessionInfo) {
    JSONObject parameters = sessionInfo.getJSONObject("gameParameters");
    SceneManager.setGameScreen(GameBoardView.setupGameBoard(sessionInfo.getJSONArray("players"),
        parameters.getString("name")));
    stage.setScene(SceneManager.getGameScreen());  
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
