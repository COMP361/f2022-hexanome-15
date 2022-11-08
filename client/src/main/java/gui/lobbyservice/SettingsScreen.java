package gui.lobbyservice;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class SettingsScreen {
private Scene scene;
	
	public static SettingsScreen instance = new SettingsScreen();
	
	private SettingsScreen() {
		
	}
	
	public static SettingsScreen getInstance() {
		return instance;
	}
	
	public Scene getSettingsScene() throws IOException {
		if (scene == null) {
	        FXMLLoader fxmlLoader = new FXMLLoader(Splendor.class.getResource("/settings-view.fxml"));
	        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
	        this.scene = scene;
		}
		return scene;
	}
}
