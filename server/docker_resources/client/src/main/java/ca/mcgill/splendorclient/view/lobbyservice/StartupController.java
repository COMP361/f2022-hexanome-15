package ca.mcgill.splendorclient.view.lobbyservice;

import ca.mcgill.splendorclient.control.SceneManager;
import ca.mcgill.splendorclient.control.Splendor;
import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceHandler;
import ca.mcgill.splendorclient.lobbyserviceio.Parsejson;
import ca.mcgill.splendorclient.lobbyserviceio.ScriptExecutor;
import ca.mcgill.splendorclient.model.users.Role;
import ca.mcgill.splendorclient.model.users.User;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONObject;

/**
 * Controls the start up for the lobby service.
 */
public class StartupController implements Initializable {
  private final LobbyServiceExecutor ls = LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR;

  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button loginButton;

  /**
   * Creates a StartUpController.
   */
  public StartupController() {

  }

  @Override
  // this method gets called once when the contents of the associated fxml files
  // are loaded
  public void initialize(URL arg0, ResourceBundle arg1) {

    loginButton.setOnAction(LobbyServiceHandler.createlsEventHandler(
      new ScriptExecutor<JSONObject>() {

        @Override
        public JSONObject execute() {
          // must use the utf-8 encoding otherwise (+) in the token will cause it to
          // seperate and insert a space
          return ls.auth_token(usernameField.getText(), passwordField.getText());
        }

        @Override
        public void respond(Object userInfo) {
          JSONObject reply = (JSONObject) userInfo;
          if (reply.has("error")) {
            Alert wrongCredentialsAlert = new Alert(AlertType.ERROR);
            wrongCredentialsAlert.setHeaderText("Invalid Credentials");
            wrongCredentialsAlert.setContentText(
                "Given credentials don't match our records, please try again.");
            wrongCredentialsAlert.showAndWait();
            System.out.println(
                "Incorrect credentials attempted: (" + usernameField.getText() + ")");
          } else {
            System.out.println("Successfully logged in " + usernameField.getText());
            Splendor.transitionTo(SceneManager.getLobbyScreen(), Optional.of("Splendor Lobby"));
            //TODO: actually get the role
            String accessToken = (String) Parsejson.PARSE_JSON
                                            .getFromKey((JSONObject) userInfo, "access_token");
            String refreshToken = (String) Parsejson.PARSE_JSON
                                             .getFromKey((JSONObject) userInfo, "refresh_token");
            User.newUser(usernameField.getText(), accessToken, refreshToken, Role.PLAYER, true);
          }
        }
      }));
  }

}
