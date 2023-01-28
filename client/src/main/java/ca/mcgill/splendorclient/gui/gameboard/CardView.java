package ca.mcgill.splendorclient.gui.gameboard;

import java.util.ArrayList;
import java.util.Optional;

import ca.mcgill.splendorclient.control.ActionManager;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
   * Creates a CardView.
   *
   * @param height The height of the CardView
   * @param width  The width of the CardView
   */
  public CardView(float height, float width, int columnCount, int rowCount) {
    this.outer = new Rectangle(height, width);
    outer.setArcHeight(height / 5);
    outer.setArcWidth(height / 5);
    this.inner = new Rectangle(height - 20, width - 20);
    inner.setArcHeight((height - 20) / 5);
    inner.setArcWidth((height - 20) / 5);
    this.getChildren().addAll(outer, inner);
    this.setOnMouseClicked(arg0 -> {
      if (arg0.getButton() == MouseButton.SECONDARY) {
        ActionManager.forwardReserveRequest(columnCount, rowCount);
      }
      else {
        ActionManager.forwardPurchaseRequest(columnCount, rowCount);
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
