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

/**
 * Represents a Trading Post in Splendor.
 */
public class TradingView extends Pane {

  private final Dimension screenSize;
  private static final String rootPath = new File("").getAbsolutePath();
  private final float sizeX;
  private final float sizeY;
  private final float shieldSizeX;
  private final float shieldSizeY;

  //order of shields is blue red yellow black
  private final ArrayList[] shields = new ArrayList[5];

  /**
   * Creates a TradingView.
   *
   * @param screenSize the size of the screen
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public TradingView(Dimension screenSize, float x, float y) {
    this.screenSize = screenSize;
    Image newImage = new Image("file:///" + rootPath + "/resources/tradingBoard.jpg");
    if (newImage != null) {
      this.setBackground(new Background(
          new BackgroundFill(new ImagePattern(newImage), CornerRadii.EMPTY, Insets.EMPTY)));
    }
    this.setPrefSize(x, y);
    this.sizeX = x;
    this.sizeY = y;
    this.shieldSizeX = sizeX / 18f;
    this.shieldSizeY = sizeY / 4f;

    for (int i = 0; i < 5; i++) {
      shields[i] = new ArrayList<>();
    }
  }

  /**
   * Draws the correct coats of arms under the powers
   * as owned by the players.
   *
   * @param firstShields shields
   * @param secondShields shields
   * @param thirdShields shields
   * @param fourthShields shields
   * @param fifthShields shields
   */
  public void updatePowers(String[] firstShields,
                           String[] secondShields,
                           String[] thirdShields,
                           String[] fourthShields,
                           String[] fifthShields) {
    this.getChildren().clear();
    float xoffset = sizeX / 18f;
    float yoffset = sizeY / 8f;
    float xorigin = 0.0f;
    updatePower(0, firstShields, xoffset, yoffset, xorigin);
    xorigin += sizeX / 5f;
    updatePower(1, secondShields, xoffset, yoffset, xorigin);
    xorigin += sizeX / 5f;
    updatePower(2, thirdShields, xoffset, yoffset, xorigin);
    xorigin += sizeX / 5f;
    updatePower(3, fourthShields, xoffset, yoffset, xorigin);
    xorigin += sizeX / 5f;
    updatePower(4, fifthShields, xoffset, yoffset, xorigin);

  }

  private void updatePower(int powerIndex, String[] colors,
                           float xoffset, float yoffset, float xorigin) {
    if (colors.length < 1) {
      return;
    }

    CoatofarmsView toAdd = new CoatofarmsView(screenSize, shieldSizeX, shieldSizeY, colors[0]);
    this.getChildren().add(toAdd);
    toAdd.setTranslateX(xoffset + xorigin);
    toAdd.setTranslateY(yoffset);

    if (colors.length < 2) {
      return;
    }
    toAdd = new CoatofarmsView(screenSize, shieldSizeX, shieldSizeY, colors[1]);
    this.getChildren().add(toAdd);
    toAdd.setTranslateX(2 * xoffset + xorigin);
    toAdd.setTranslateY(yoffset);


    if (colors.length < 3) {
      return;
    }
    toAdd = new CoatofarmsView(screenSize, shieldSizeX, shieldSizeY, colors[2]);
    this.getChildren().add(toAdd);
    toAdd.setTranslateX(xoffset + xorigin);
    toAdd.setTranslateY(3.5 * yoffset);


    if (colors.length < 4) {
      return;
    }
    toAdd = new CoatofarmsView(screenSize, shieldSizeX, shieldSizeY, colors[3]);
    this.getChildren().add(toAdd);
    toAdd.setTranslateX(2 * xoffset + xorigin);
    toAdd.setTranslateY(3.5 * yoffset);


  }
}
