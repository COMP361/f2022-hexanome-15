package gui.lobbyservice;

import backend.lobbyservice.Session;
import gui.scenemanager.SceneManager;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * Controls Lobby Service functions.
 */
public class LobbyController implements Initializable {
  @FXML
  private Button createSessionButton;
  @FXML
  private ListView<String> availableSessionList;
  @FXML
  private Button launchSessionButton;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    createSessionButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent arg0) {
        // TODO: figure out how to pass the currently logged in user to use for this
        // field
        availableSessionList.getItems().add(new Session("maex", null, "demo").toString());

      }
    });

    // launches the selected session
    launchSessionButton.setOnAction(new EventHandler<ActionEvent>() {
      // TODO: setup with LS

      @Override
      public void handle(ActionEvent arg0) {
        Splendor.transitionTo(SceneManager.getGameScreen(), Optional.of("Game Screen"));
      }
    });
  }
}
