package ca.mcgill.splendorclient.view.gameboard;

import ca.mcgill.splendorclient.control.ActionManager;
import ca.mcgill.splendorclient.control.ColorManager;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import kong.unirest.HttpResponse;
import kong.unirest.json.JSONException;

/**
 * Represents a noble in Splendor.
 */
public class NobleView extends StackPane {

  private final Rectangle outer;
  private final Rectangle inner;
  private Text textToDisplay;
  private int localid;
  private static final String rootPath = new File("").getAbsolutePath();

  /**
   * Creates a NobleView.
   *
   * @param height the height of the noble view
   * @param width the width of the noble view
   */
  public NobleView(float height, float width) {
    this.outer = new Rectangle(height, width);
    outer.setArcHeight(height / 5);
    outer.setArcWidth(height / 5);
    this.inner = new Rectangle(height - 10, width - 10);
    inner.setArcHeight((height - 10) / 5);
    inner.setArcWidth((height - 10) / 5);
    this.getChildren().addAll(outer, inner);
    this.setOnMouseClicked(arg0 -> {
      HttpResponse<String> result = ActionManager.findAndSendReceiveNobleMove(localid);
      if (result != null) {
        if (result.getStatus() == 206) {
          ActionManager.handleCompoundMoves(result.getBody());
        } else if (result.getStatus() == 200) {
            //inform end of turn
        }
      } else {
        result = ActionManager.findAndSendReserveNobleMove(localid);
        if (result != null) {
          if (result.getStatus() == 206) {
            ActionManager.handleCompoundMoves(result.getBody());
          } else if (result.getStatus() == 200) {
              //inform end of turn
          }
        }
      }
    });
  }

  /**
   * Updates the noble view.
   *
   * @param num the noble id
   */
  public void updateView(int num) {
    if (num >= 0 && num < 19) {
      Image newImage = new Image("file:///" + rootPath + "/resources/noble_" + num + ".jpg");
      outer.setFill(ColorManager.getColor(num));
      inner.setFill(new ImagePattern(newImage));
    }
    localid = num;
  }

  /**
   * Adds display text to a noble view after reserving or visiting.
   *
   * @param text the text to display
   */
  public void displayText(String text) {
    this.textToDisplay = new Text();
    this.textToDisplay.setText(text);
    this.textToDisplay.setFont(Font.font("Comic Sans MS",
            FontWeight.BOLD,
            FontPosture.REGULAR,
            GameBoardView.getFontSize() / 2));
    this.textToDisplay.setFill(Color.WHITE);
    this.textToDisplay.setStrokeWidth(1.0);
    this.textToDisplay.setStroke(Color.BLACK);
    this.getChildren().add(textToDisplay);
  }

  /**
   * Returns the id of this city view.
   *
   * @return the id of this city view
   */
  public int getLocalid() {
    return localid;
  }
}
