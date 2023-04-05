package ca.mcgill.splendorclient.view.gameboard;

import java.awt.Dimension;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Represents a CoatOfArms in Splendor.
 */
public class CoatofarmsView extends Rectangle {

  private final Dimension screenSize;
  private static final String rootPath = new File("").getAbsolutePath();
  
  /**
   * Creates a CoatofarmsView.
   *
   * @param screenSize the size of the screen
   * @param x the x coordinate
   * @param y the y coordinate
   * @param color a string representing the color of the shield
   */
  public CoatofarmsView(Dimension screenSize, float x, float y, String color) {
    super(x, y);
    Image newImage = new Image("file:///" + rootPath + "/resources/shield_"
                                   + color.toLowerCase() + ".png");
    if (newImage != null) {
      this.setFill(new ImagePattern(newImage));
    }
    this.screenSize = screenSize;
  }
}
  
