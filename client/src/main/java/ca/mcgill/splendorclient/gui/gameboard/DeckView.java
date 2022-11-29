package ca.mcgill.splendorclient.gui.gameboard;

import ca.mcgill.splendorclient.model.cards.Observer;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents the view of a Splendor Deck.
 * Observes Deck to be notified of when to change the count.
 */
public class DeckView extends Rectangle implements Observer {

  private final Counter numCardsDisplay;

  /**
   * Creates a DeckView.
   *
   * @param height The height of the DeckView
   * @param width  The width of the DeckView
   * @param size The number of cards in the deck
   * @param color The color of the DeckView
   */
  public DeckView(double height, double width, int size, Color color) {
    super(height, width);
    this.setArcHeight(height / 5);
    this.setArcWidth(height / 5);
    numCardsDisplay = new Counter(size);
    this.setFill(color);
  }

  /**
   * Returns the counter that displays the number of cards in the deck.
   *
   * @return the counter that displays the number of cards in the deck
   */
  public Label getNumCardsDisplay() {
    return numCardsDisplay;
  }

  @Override
  public void onAction(boolean increment) {
    if (increment) {
      numCardsDisplay.setText(String.valueOf(numCardsDisplay.getCount() + 1));
    } else {
      numCardsDisplay.setText(String.valueOf(numCardsDisplay.getCount() - 1));
    }
  }
}
