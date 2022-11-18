package gui.gameboard;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.cards.Deck;
import model.cards.DeckObserver;

/**
 * Represents the view of a Splendor Deck.
 */
public class DeckView extends Rectangle implements DeckObserver {

  private final Counter numCardsDisplay;
  private final Deck deck;

  /**
   * Creates a DeckView.
   *
   * @param deck The deck that is represented by this DeckView
   * @param height The height of the DeckView
   * @param width The width of the DeckView
   */
  public DeckView(Deck deck, double height, double width) {
    super(height, width);
    this.setArcHeight(height / 5);
    this.setArcWidth(height / 5);
    numCardsDisplay = new Counter(deck.getSize());
    this.deck = deck;
    this.setFill(deck.getColor());
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
  public void onAction() {
    deck.replaceCard();
    numCardsDisplay.setText(Integer.toString(deck.getSize()));
    if (deck.getSize() == 0) {
      this.setFill(Color.WHITE);
    }
  }

  /**
   * Sets up the DeckView upon starting the game.
   */
  public void setUp() {
    deck.deal();
    numCardsDisplay.setText(Integer.toString(deck.getSize()));
  }
}
