package ca.mcgill.splendorclient.view.gameboard;

import ca.mcgill.splendorclient.control.ActionManager;
import ca.mcgill.splendorclient.control.ColorManager;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import kong.unirest.HttpResponse;
import kong.unirest.json.JSONException;

/**
 * Represents the view of a Splendor Card.
 * Observed by UserInventory, for purpose of adding purchased card to User Inventory
 * Observed by MoveManager to forward the finished move to the server
 * Observes Deck in order to populate card from top of deck.
 */
public class CardView extends StackPane {

  private final Rectangle outer;
  private final Rectangle inner;
  private int localid;
  private static final String rootPath = new File("").getAbsolutePath();

  /**
   * Creates a CardView. These represent CardViews in a user inventory.
   *
   * @param height The height of the CardView
   * @param width  The width of the CardView
   */

  public CardView(float height, float width) {
    this.outer = new Rectangle(height, width);
    outer.setArcHeight(height / 5);
    outer.setArcWidth(height / 5);
    this.inner = new Rectangle(height - 10, width - 10);
    inner.setArcHeight((height - 10) / 5);
    inner.setArcWidth((height - 10) / 5);
    this.getChildren().addAll(outer, inner);
    this.setOnMouseClicked(arg0 -> {
      HttpResponse<String> result =
          ActionManager.findAndSendPairSpiceCardMove(localid);
      if (result  != null) {
        if (result.getStatus() == 206) {
          ActionManager.handleCompoundMoves(result.getBody());
        } else if (result.getStatus() == 200) {
          //board updater informs end of turn
        }
      } else {
        result = ActionManager.findAndSendPurchaseCardMove(localid);
        if (result  != null) {
          if (result.getStatus() == 206) {
            ActionManager.handleCompoundMoves(result.getBody());
          } else if (result.getStatus() == 200) {
            //board updater informs end of turn
          }
        } else {
          result = ActionManager.findAndSendDiscardCardMove(localid);
          if (result  != null) {
            if (result.getStatus() == 206) {
              ActionManager.handleCompoundMoves(result.getBody());
            } else if (result.getStatus() == 200) {
              //board updater informs end of turn
            }
          }
        }
      }
    });
  }

  /**
   * Creates a CardView in the field of play.
   *
   * @param height The height of the CardView
   * @param width The width of the CardView
   * @param locationCode The locationCode of the CardView
   */
  public CardView(float height, float width, String locationCode) {
    this.outer = new Rectangle(height, width);
    outer.setArcHeight(height / 5);
    outer.setArcWidth(height / 5);
    this.inner = new Rectangle(height - 10, width - 10);
    inner.setArcHeight((height - 10) / 5);
    inner.setArcWidth((height - 10) / 5);
    this.getChildren().addAll(outer, inner);
    this.setOnMouseClicked(arg0 -> {
      if (arg0.getButton() == MouseButton.SECONDARY) {
        HttpResponse<String> result = ActionManager.findAndSendReserveCardMove(localid);
        if (result != null) {
          if (result.getStatus() == 206) {
            ActionManager.handleCompoundMoves(result.getBody());
          } else if (result.getStatus() == 200) {
            //board updater informs end of turn
          }
        }
      } else {
        HttpResponse<String> result = ActionManager.findAndSendPurchaseCardMove(localid);
        if (result != null) {
          if (result.getStatus() == 206) {
            ActionManager.handleCompoundMoves(result.getBody());
          } else if (result.getStatus() == 200) {
            //inform end of turn
          }
        } else {
          result = ActionManager.findAndSendCascadeLevel2Move(localid);
          if (result != null) {
            if (result.getStatus() == 206) {
              ActionManager.handleCompoundMoves(result.getBody());
            } else if (result.getStatus() == 200) {
              //inform end of turn
            }
          } else {
            result =
              ActionManager.findAndSendCascadeLevel1Move(localid);
            if (result != null) {
              if (result.getStatus() == 206) {
                ActionManager.handleCompoundMoves(result.getBody());
              } else if (result.getStatus() == 200) {
                //inform end of turn
              }
            }
          }
        }
      }
    });
  }

  /**
   * Updates the card view with the card image.
   *
   * @param num the card id
   */
  public void updateView(int num) {
    if (num >= 0 && num < 120) {
      Image newImage = new Image("file:///" + rootPath + "/resources/card_" + num + ".jpg");
      outer.setFill(ColorManager.getColor(num));
      inner.setFill(new ImagePattern(newImage));
    }
    localid = num;
  }

  /**
   * Updates the card view with the card image and marks it as reserved.
   *
   * @param num the card id
   */
  public void updateViewReserved(int num) {
    if (num >= 0 && num < 120) {
      Image newImage = new Image("file:///" + rootPath + "/resources/card_" + num + ".jpg");
      outer.setFill(Color.BLACK);
      inner.setFill(new ImagePattern(newImage));
    }
    localid = num;
  }
}
