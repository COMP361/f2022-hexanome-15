/**
 * Oct 23, 2022
 * TODO
 */
package gui.startup;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @author zacharyhayden
 *
 */
public class LoginApplication extends Application {

	@Override
	public void start(Stage pStage) throws Exception {
		pStage.setTitle("Login");

		// create login pane
		GridPane gridPane = createLoginPane();
		// create scene with login pane as root
		Scene scene = new Scene(gridPane, 800, 500); // width 800, height 500
		// put scene in primary stage
		pStage.setScene(scene);

		pStage.show();

	}

	private GridPane createLoginPane() {
		GridPane gridPane = new GridPane();
		// position at center of screen both vertically and horizontally
		gridPane.setAlignment(Pos.CENTER);
		// set padding 20px on each side
		gridPane.setPadding(new Insets(40, 40, 40, 40));
		// set horizontal gap between columns
		gridPane.setHgap(10);
		// set vertical gap between rows
		gridPane.setVgap(10);

		// Column constraints
		// columnOneConstraints will be applied to all the nodes placed in column one.
		ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
		columnOneConstraints.setHalignment(HPos.RIGHT);

		// columnTwoConstraints will be applied to all the nodes placed in column two.
		ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
		columnTwoConstrains.setHgrow(Priority.ALWAYS); // if screen resizes then column two should grow to fill

		gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

		return gridPane;
	}

	private void addControls(GridPane pGridPane) {
		// header
		Label headerLabel = new Label("Login");
		headerLabel.setFont(Font.font("Ariel", FontWeight.BOLD, 24)); // font, special, size
		pGridPane.add(headerLabel, 0, 0, 2, 1);
		GridPane.setHalignment(headerLabel, HPos.CENTER);
		GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));
		
		// add username label
		Label nameLabel = new Label("Username : ");
		pGridPane.add(nameLabel, 0, 1);
		
		// add receiving text field
		TextField nameField = new TextField();
		nameField.setPrefHeight(40);
		pGridPane.add(nameField, 1, 1);
		
		// add password label
		Label passwordLabel = new Label("Password : ");
		pGridPane.add(passwordLabel, 0, 3);
		
		// add password field
		PasswordField passwordField = new PasswordField();
		passwordField.setPrefHeight(40);
		pGridPane.add(passwordField, 1, 3);
		
		// add a enter button
		Button enterButton = new Button("Enter");
		enterButton.prefHeight(40);
		enterButton.setDefaultButton(true);
		enterButton.setPrefWidth(100);
		pGridPane.add(enterButton, 0, 4, 2, 1);
		GridPane.setHalignment(enterButton, HPos.CENTER);
		GridPane.setMargin(enterButton, new Insets(20,0,20,0));
	}

	public static void main(String[] args) {
		launch(args);
	}

}
