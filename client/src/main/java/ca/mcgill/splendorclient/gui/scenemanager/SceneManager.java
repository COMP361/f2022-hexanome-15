package ca.mcgill.splendorclient.gui.scenemanager;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Scene;


/**
 * Manages scenes in the application.
 */
public class SceneManager {

  private static Map<String, Scene> scenes = new HashMap<String, Scene>();

  private static String[] sceneNames = {"GAMESCREEN", "LOGIN", "LOBBY", "SETTINGS", "ADMIN"};

  /**
   * Creates a SceneManager.
   */
  public SceneManager() {

  }

  /**
   * Sets the Game Screen.
   *
   * @param scene the scene to be set
   */
  public static void setGameScreen(Scene scene) {
    scenes.put(sceneNames[0], scene);
  }

  /**
   * Returns the Game Screen.
   *
   * @return the game screen
   */
  public static Scene getGameScreen() {
    return scenes.get(sceneNames[0]);
  }

  /**
   * Sets the Login Screen.
   *
   * @param scene the scene to be set
   */
  public static void setLoginScreen(Scene scene) {
    scenes.put(sceneNames[1], scene);
  }

  /**
   * Returns the Login Screen.
   *
   * @return the login screen
   */
  public static Scene getLoginScreen() {
    return scenes.get(sceneNames[1]);
  }

  /**
   * Sets the Lobby Screen.
   *
   * @param scene the scene to be set
   */

  public static void setLobbyScreen(Scene scene) {
    scenes.put(sceneNames[2], scene);
  }

  /**
   * Returns the Lobby Screen.
   *
   * @return the lobby screen
   */
  public static Scene getLobbyScreen() {
    return scenes.get(sceneNames[2]);
  }

  /**
   * Sets the Settings Screen.
   *
   * @param scene the scene to be set
   */
  public static void setSettingsScreen(Scene scene) {
    scenes.put(sceneNames[3], scene);
  }

  /**
   * Returns the Settings Screen.
   *
   * @return the settings screen
   */
  public static Scene getSettingsScreen() {
    return scenes.get(sceneNames[3]);
  }

  /**
   * Sets the Admin Screen.
   *
   * @param scene the scene to be set
   */
  public static void setAdminScreen(Scene scene) {
    scenes.put(sceneNames[4], scene);
  }

  /**
   * Returns the Admin Screen.
   *
   * @return the admin screen
   */
  public static Scene getAdminScreen() {
    return scenes.get(sceneNames[4]);
  }

}