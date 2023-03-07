package ca.mcgill.splendorclient.view.gameboard;

import java.awt.Dimension;
import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class CoatofarmsView extends Rectangle{

  private final Dimension screenSize;
  private final static String rootPath = new File("").getAbsolutePath();
  
  /**
   * Creates a CoatofarmsView.
   *
   * @param screenSize the size of the screen
   * @param color a string representing the color of the shield
   */
  public CoatofarmsView(Dimension screenSize, String color) {
    super(screenSize.getWidth() / 65f, screenSize.getHeight() / 40.f);
    Image newImage = new Image("file:///"+rootPath+"/resources/shield_"+color.toLowerCase()+".png");
    this.setFill(new ImagePattern(newImage));
    this.screenSize = screenSize;
  }
}
  
