package ca.mcgill.splendorclient.view.gameboard;

import ca.mcgill.splendorclient.control.ActionManager;
import ca.mcgill.splendorclient.control.ColorManager;
import ca.mcgill.splendorclient.model.DeckType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import kong.unirest.HttpResponse;

/**
 * Represents the view of a Splendor Deck.
 * Observes Deck to be notified of when to change the count.
 */
public class DeckView extends Rectangle {

  private final Counter numCardsDisplay;
  private final DeckType deckType;

  /**
   * Creates a DeckView.
   *
   * @param height The height of the DeckView
   * @param width  The width of the DeckView
   * @param size The number of cards in the deck
   * @param deckType The type of deck represented by this DeckView
   */
  public DeckView(double height, double width, int size, DeckType deckType) {
    super(height, width);
    this.setArcHeight(height / 5);
    this.setArcWidth(height / 5);
    numCardsDisplay = new Counter(size);
    Color color = ColorManager.getColor(deckType);
    this.setFill(color);
    this.deckType = deckType;

    this.setOnMouseClicked(arg0 -> {
      if (arg0.getButton() == MouseButton.SECONDARY) {
        HttpResponse<String> result = ActionManager.findAndSendReserveFromDeckMove(deckType);
        if (result != null) {
          if (result.getStatus() == 206) {
            ActionManager.handleCompoundMoves(result.getBody());
          } else if (result.getStatus() == 200) {
            //board updater informs end of turn
          }
        }
      }
    });
  }

  /**
   * Returns the counter that displays the number of cards in the deck.
   *
   * @return the counter that displays the number of cards in the deck
   */
  public Text getNumCardsDisplay() {
    return numCardsDisplay;
  }
  
  /**
   * Sets the number of cards display for a deck view.
   *
   * @param count to display
   */
  public void setNumCardsDisplay(int count) {
    numCardsDisplay.setCount(count);
  }
}
