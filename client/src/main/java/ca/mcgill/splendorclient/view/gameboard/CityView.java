package ca.mcgill.splendorclient.view.gameboard;

import java.io.File;

import ca.mcgill.splendorclient.control.ActionManager;
import ca.mcgill.splendorclient.control.ColorManager;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import kong.unirest.HttpResponse;

/**
 * Represents a City in Splendor.
 * 
 * @author Svyatoslav Sklokin
 */
public class CityView extends StackPane {

  private final Rectangle outer;
  private final Rectangle inner;
  private static final String rootPath = new File("").getAbsolutePath();

  /**
   * Creates a NobleView.
   *
   * @param height the height of the noble view
   * @param width the width of the noble view
   */
  public CityView(float width, float height) {
    this.outer = new Rectangle(width, height);
    outer.setArcHeight(height / 5);
    outer.setArcWidth(width / 5);
    this.inner = new Rectangle(width - 10, height - 10);
    inner.setArcHeight((height - 10) / 5);
    inner.setArcWidth((width - 10) / 5);
    this.getChildren().addAll(outer, inner);
  }

  /**
   * Updates the noble view.
   *
   * @param num the noble id
   */
  public void updateView(int num) {
    if (num >= 0 && num < 19) {
      Image newImage = new Image("file:///" + rootPath + "/resources/city_" + num + ".jpg");
      outer.setFill(Color.FLORALWHITE);
      inner.setFill(new ImagePattern(newImage));
    }
  }
}
