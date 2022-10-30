/**
 * Oct 30, 2022
 * TODO
 */
package gui.lobbyservice;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	@FXML private Button loginButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("Tried to login " + usernameField.getText() + " with password " + passwordField.getText());
				
			}
		});
		
	}
	
}
