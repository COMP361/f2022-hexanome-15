package gui.lobbyservice;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import gui.gameboard.GameBoard;
import gui.scenemanager.SceneManager;

public class Splendor extends Application {
	
	private static Stage stage;
	
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.setLoginScreen(LoginScreen.getInstance().getLoginScene());
        SceneManager.setGameScreen(GameBoard.setupGameBoard());
        Splendor.stage = stage;
        stage.setTitle("Login or Signup!");
        stage.setScene(SceneManager.getLoginScreen());
        stage.show();
    }
    
    public static void transitionTo(Scene scene, Optional<String> title) {
    	stage.setScene(scene);
    	if (title.isPresent()) {
    		stage.setTitle(title.get());
    	}
    	else {
    		stage.setTitle("");
    	}
    }

    public static void main(String[] args) {
        launch();
    }
}