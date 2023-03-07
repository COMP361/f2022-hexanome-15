package ca.mcgill.splendorclient.view.gameboard;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class TradingView extends Pane {

  private final Dimension screenSize;
  private final static String rootPath = new File("").getAbsolutePath();

  //order of shields is blue red yellow black
  private final ArrayList<Rectangle>[] shields = new ArrayList[5];
  
  /**
   * Creates a TradingView.
   *
   * @param screenSize the size of the screen
   */
  public TradingView(Dimension screenSize) {
    this.screenSize = screenSize;
    Image newImage = new Image("file:///"+rootPath+"/resources/tradingBoard.jpg");
    this.setBackground(new Background(new BackgroundFill(new ImagePattern(newImage), CornerRadii.EMPTY, Insets.EMPTY)));
    this.setPrefSize(screenSize.getWidth() / 4.0f, screenSize.getHeight() / 10.0f);
    
    for (int i = 0; i < 5; i++) {
      shields[i] = new ArrayList<Rectangle>();
    }
  }
  
  /**
   * Draws the correct coats of arms under the powers
   * as owned by the players.
   * 
   * @param 
   */
  public void updatePowers(String[] firstShields, 
      String[] secondShields, 
      String[] thirdShields, 
      String[] fourthShields, 
      String[] fifthShields) {
    this.getChildren().clear();
    //this.getChildren().add(new CoatofarmsView(screenSize, "RED"));
    float xOffset = (float) (screenSize.getWidth() / 65f);
    float yOffset = (float) (screenSize.getHeight() / 40f);
    float xOrigin = 0.0f;
    updatePower(0, firstShields, xOffset, yOffset, xOrigin);
    xOrigin += screenSize.getWidth() / 20.0f;
    updatePower(1, firstShields, xOffset, yOffset, xOrigin);
    xOrigin += screenSize.getWidth() / 20.0f;
    updatePower(2, firstShields, xOffset, yOffset, xOrigin);
    xOrigin += screenSize.getWidth() / 20.0f;
    updatePower(3, firstShields, xOffset, yOffset, xOrigin);
    xOrigin += screenSize.getWidth() / 20.0f;
    updatePower(4, firstShields, xOffset, yOffset, xOrigin);
    
  }
  private void updatePower(int powerIndex, String[] colors, float xOffset, float yOffset, float xOrigin) {
    if (colors.length < 1) return;
    
    CoatofarmsView toAdd = new CoatofarmsView(screenSize, colors[0]);
    this.getChildren().add(toAdd);
    toAdd.setTranslateX(xOffset + xOrigin);
    toAdd.setTranslateY(yOffset);

    if (colors.length < 2) return;
    toAdd = new CoatofarmsView(screenSize, colors[1]);
    this.getChildren().add(toAdd);
    toAdd.setTranslateX(2 * xOffset + xOrigin);
    toAdd.setTranslateY(yOffset);

    
    if (colors.length < 3) return;
    toAdd = new CoatofarmsView(screenSize, colors[2]);
    this.getChildren().add(toAdd);
    toAdd.setTranslateX(xOffset + xOrigin);
    toAdd.setTranslateY(2 * yOffset);

    
    if (colors.length < 4) return;
    toAdd = new CoatofarmsView(screenSize, colors[3]);
    this.getChildren().add(toAdd);
    toAdd.setTranslateX(2 * xOffset + xOrigin);
    toAdd.setTranslateY(2 * yOffset);
    

  }
}
