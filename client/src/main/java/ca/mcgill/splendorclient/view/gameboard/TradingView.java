package ca.mcgill.splendorclient.view.gameboard;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import ca.mcgill.splendorclient.control.ColorManager;
import ca.mcgill.splendorclient.model.TokenType;
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
  }
}
