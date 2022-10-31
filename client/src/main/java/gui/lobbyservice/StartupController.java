/**
 * Oct 30, 2022
 * TODO
 */
package gui.lobbyservice;

import static backend.lobbyservice.LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR;
import static backend.lobbyservice.LobbyServiceHandler.createLSEventHandler;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.ResourceBundle;

import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import backend.lobbyservice.LobbyServiceExecutor;
import backend.lobbyservice.ParseJSON;
import backend.lobbyservice.ScriptExecutor;
import backend.users.Role;
import backend.users.User;
import gui.scenemanager.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @author zacharyhayden
 *
 */
public class StartupController implements Initializable {
	private final LobbyServiceExecutor ls = LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR;

	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button loginButton;

	@Override
	// this method gets called once when the contents of the associated fxml files
	// are loaded
	public void initialize(URL arg0, ResourceBundle arg1) {
		// getting LS default admin info
		JSONObject adminAuth = LOBBY_SERVICE_EXECUTOR.auth_token("maex", "abc123_ABC123");
		String accessToken = (String) ParseJSON.PARSE_JSON.getFromKey(adminAuth, "access_token");
		String refreshToken = (String) ParseJSON.PARSE_JSON.getFromKey(adminAuth, "refresh_token");
		// flyweight User object representing the default admin of the LS
		User.newUser("maex", accessToken, refreshToken, Role.ADMIN);

		loginButton.setOnAction(createLSEventHandler(new ScriptExecutor<JSONObject>() {

			@Override
			public JSONObject execute() {
				try {
					// must use the utf-8 encoding otherwise (+) in the token will cause it to
					// seperate and insert a space
					return ls.get_user(usernameField.getText(), URLEncoder.encode(accessToken, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void respond(Object userInfo) {
				if (userInfo == null) { // means that the entered username doesn't exist within the LS
					Alert noUser = new Alert(AlertType.ERROR);
					noUser.setHeaderText("Invalid Credentials");
					noUser.setContentText("Given credentials don't match our records, please try again.");
					noUser.showAndWait();
					System.out.println("Login failed for user: (" + usernameField.getText() + ") with password: ("
							+ passwordField.getText() + ")");
				} else {
					PasswordEncoder bcrypt = new BCryptPasswordEncoder();
					// username is valid must validate the password
					String actualPassword = (String) ParseJSON.PARSE_JSON.getFromKey((JSONObject) userInfo, "password");
					if (bcrypt.matches(passwordField.getText(), actualPassword)) {
						System.out.println("Successfully logged in " + usernameField.getText());
						Splendor.transitionTo(SceneManager.getLobbyScreen(), Optional.of("Splendor Lobby"));
					} else {
						Alert wrongPasswordAlert = new Alert(AlertType.ERROR);
						wrongPasswordAlert.setHeaderText("Incorrect Password");
						wrongPasswordAlert
								.setContentText("Entered password does not match the given username, please try again");
						wrongPasswordAlert.showAndWait();
						System.out.println("Incorrect password attempt for user: (" + usernameField.getText() + ")");

					}

					// TODO: initialize player object with this logged in player and fetch their
					// auth tokens
				}
			}
		}));

	}

}
