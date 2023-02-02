package ca.mcgill.splendorclient.view.gameboard;

import java.util.Optional;

import ca.mcgill.splendorclient.control.ActionManager;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * Represents the view of a Splendor Card.
 * Observed by UserInventory, for purpose of adding purchased card to User Inventory
 * Observed by MoveManager to forward the finished move to the server
 * Observes Deck in order to populate card from top of deck.
 */
public class CardView extends StackPane {

  private final Rectangle outer;
  private final Rectangle inner;

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
    this.inner = new Rectangle(height - 20, width - 20);
    inner.setArcHeight((height - 20) / 5);
    inner.setArcWidth((height - 20) / 5);
    this.getChildren().addAll(outer, inner);
    this.setOnMouseClicked(arg0 -> {});
  }
  
  /**
   * Creates a CardView in the field of play
   * 
   * @param height
   * @param width
   * @param locationCode
   */
  public CardView(float height, float width, String locationCode) {
    this.outer = new Rectangle(height, width);
    outer.setArcHeight(height / 5);
    outer.setArcWidth(height / 5);
    this.inner = new Rectangle(height - 20, width - 20);
    inner.setArcHeight((height - 20) / 5);
    inner.setArcWidth((height - 20) / 5);
    this.getChildren().addAll(outer, inner);
    this.setOnMouseClicked(arg0 -> {
      if (arg0.getButton() == MouseButton.SECONDARY) {
        ActionManager.forwardCardRequest(locationCode + "R");
      }
      else {
        ActionManager.forwardCardRequest(locationCode + "P");
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

}
