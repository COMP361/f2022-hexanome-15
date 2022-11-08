package gui.lobbyservice;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class LoginScreen {
	
	private Scene scene;
	
	public static LoginScreen instance = new LoginScreen();
	
	private LoginScreen() {
		
	}
	
	public static LoginScreen getInstance() {
		return instance;
	}
	
	public Scene getLoginScene() throws IOException {
		if (scene == null) {
	        FXMLLoader fxmlLoader = new FXMLLoader(Splendor.class.getResource("/hello-view.fxml"));
	        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
	        this.scene = scene;
		}
		return scene;
	}

}