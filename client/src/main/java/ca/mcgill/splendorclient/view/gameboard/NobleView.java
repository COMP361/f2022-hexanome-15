package ca.mcgill.splendorclient.view.gameboard;

import ca.mcgill.splendorclient.control.ActionManager;
import ca.mcgill.splendorclient.control.ColorManager;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import kong.unirest.json.JSONException;

/**
 * Represents a noble in Splendor.
 */
public class NobleView extends StackPane {

  private final Rectangle outer;
  private final Rectangle inner;
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
  }

  /**
   * Updates the noble view.
   *
   * @param num the noble id
   */
  public void updateView(int num) {
    Image newImage = new Image("file:///" + rootPath + "/resources/noble_" + num + ".jpg");
    if (newImage != null) {
      outer.setFill(ColorManager.getColor(num));
      inner.setFill(new ImagePattern(newImage));
    }
    localid = num;
  }
}
