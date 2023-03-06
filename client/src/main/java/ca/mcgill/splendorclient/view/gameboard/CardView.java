package ca.mcgill.splendorclient.view.gameboard;

import ca.mcgill.splendorclient.control.ActionManager;
import ca.mcgill.splendorclient.control.ColorManager;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
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
  private int localID;

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
    this.setOnMouseClicked(arg0 -> {});
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
        try {
          ActionManager.forwardCardRequest(locationCode + "R");
        } catch (JSONException e) {
          //TODO: add a turn field to the response from the .../board call. 
        }
      } else {
        try {
          ActionManager.forwardCardRequest(locationCode + "P");
        } catch (JSONException e) {
          //TODO: add something in catch block
        }
      }
    });
  }

  //TODO: figure out what kind of card model we need on this side for display purposes.
//  /**
//   * Fills the CardView with the appropriate colors.
//   *
//   * @param card The card that is represented by this CardView
//   */
//  public void forceCard(Card card) {
//    this.card = Optional.of(card);
//    inner.setFill(ColorManager.getColor(card.getTokenType()));
//    outer.setFill(ColorManager.getColor(card.getCardType()));
//  }
  public void updateView(int num) {
	//yes hardcoded for testing
	Image newImage = new Image("file:///L:/f2022-hexanome-15/client/resources/card_"+num+".jpg");
	outer.setFill(ColorManager.getColor(num));
	inner.setFill(new ImagePattern(newImage));
	localID = num;
  }
}
