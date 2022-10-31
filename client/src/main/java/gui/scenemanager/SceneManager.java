package gui.scenemanager;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Scene;

public class SceneManager {
	
	private static Map<String, Scene> scenes = new HashMap<String, Scene>();
	
	private static String[] sceneNames = {"GAMESCREEN", "LOGIN", "LOBBY", "SETTINGS", "ADMIN"};
	
	public static void setGameScreen(Scene scene) {
		scenes.put(sceneNames[0], scene);
	}
	
	public static Scene getGameScreen() {
		return scenes.get(sceneNames[0]);
	}
	
	public static void setLoginScreen(Scene scene) {
		scenes.put(sceneNames[1], scene);
	}
	
	public static Scene getLoginScreen() {
		return scenes.get(sceneNames[1]);
	}
	
	public static void setLobbyScreen(Scene scene) {
		scenes.put(sceneNames[2], scene);
	}
	
	public static Scene getLobbyScreen() {
		return scenes.get(sceneNames[2]);
	}
	
	public static void setSettingsScreen(Scene scene) {
		scenes.put(sceneNames[3], scene);
	}
	
	public static Scene getSettingsScreen() {
		return scenes.get(sceneNames[3]);
	}
	
	public static void setAdminScreen(Scene scene) {
		scenes.put(sceneNames[4], scene);
	}
	
	public static Scene getAdminScreen() {
		return scenes.get(sceneNames[4]);
	}

}