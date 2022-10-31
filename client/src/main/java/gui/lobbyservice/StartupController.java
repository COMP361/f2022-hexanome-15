/**
 * Oct 30, 2022
 * TODO
 */
package gui.lobbyservice;

import static backend.lobbyservice.LobbyServiceHandler.createLSEventHandler;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONException;
import org.json.JSONObject;

import backend.lobbyservice.LobbyServiceExecutor;
import backend.lobbyservice.ParseJSON;
import backend.lobbyservice.ScriptExecutor;
import backend.users.Role;
import backend.users.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @author zacharyhayden
 *
 */
public class StartupController implements Initializable {
	private final LobbyServiceExecutor ls = LobbyServiceExecutor.getLobbyServiceExecutor(null, null);

	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button loginButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loginButton.setOnAction(createLSEventHandler(new ScriptExecutor<JSONObject>() {

			@Override
			public JSONObject execute() {
				return ls.auth_token(usernameField.getText(), passwordField.getText());
			}

			@Override
			public void respond(Object auth) {
				try {
					User.newUser((String) ParseJSON.PARSE_JSON.getFromKey((JSONObject) auth, "access_token"),
							(String) ParseJSON.PARSE_JSON.getFromKey((JSONObject) auth, "refresh_token"), Role.PLAYER);
				} catch (JSONException e) {
					// TODO: show an error alert to user that the entered credentials dont match any
					// accounts
				}

			}
		}));

	}

}
