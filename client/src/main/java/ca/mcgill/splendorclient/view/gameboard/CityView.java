package ca.mcgill.splendorclient.view.gameboard;

import ca.mcgill.splendorclient.control.ActionManager;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import kong.unirest.HttpResponse;

/**
 * Represents a City in Splendor.
 *
 * @author Svyatoslav Sklokin
 */
public class CityView extends StackPane {

  private final Rectangle outer;
  private final Rectangle inner;
  private Text textToDisplay;
  private int cityid;
  private static final String rootPath = new File("").getAbsolutePath();

  /**
   * Creates a CityView.
   *
   * @param height the height of the city view
   * @param width the width of the city view
   */
  public CityView(float width, float height) {
    this.outer = new Rectangle(width, height);
    outer.setArcHeight(height / 5);
    outer.setArcWidth(width / 5);
    this.inner = new Rectangle(width - 10, height - 10);
    inner.setArcHeight((height - 10) / 5);
    inner.setArcWidth((width - 10) / 5);
    this.getChildren().addAll(outer, inner);
    this.setOnMouseClicked(arg0 -> {
      HttpResponse<String> result = ActionManager.findAndSendReceiveCityMove(cityid);
      if (result != null) {
        if (result.getStatus() == 206) {
          ActionManager.handleCompoundMoves(result.getBody());
        } else if (result.getStatus() == 200) {
          //inform end of turn
        }
      }
    });
  }

  /**
   * Updates the city view.
   *
   * @param num the city id
   */
  public void updateView(int num) {
    if (num >= 0 && num < 19) {
      Image newImage = new Image("file:///" + rootPath + "/resources/city_" + num + ".jpg");
      outer.setFill(Color.FLORALWHITE);
      inner.setFill(new ImagePattern(newImage));
    }
    cityid = num;
  }

  /**
   * Returns the city id.
   *
   * @return the city id
   */
  public int getCityid() {
    return cityid;
  }

  /**
   * Adds display text to city view when unlocked.
   *
   * @param text the text to be displayed
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
}
