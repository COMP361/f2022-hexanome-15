package ca.mcgill.splendorclient.gui.lobbyservice;

import ca.mcgill.splendorclient.gui.scenemanager.SceneManager;
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

  /**
   * Creates a LobbyController.
   */
  public LobbyController() {

  }

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    createSessionButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent arg0) {
        // TODO: fix this so that it adds the session object to available sessions list
        availableSessionList.getItems().add(null);

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
