package ca.mcgill.splendorclient.view.gameboard;

import ca.mcgill.splendorclient.model.TokenType;
import java.awt.Dimension;
import javafx.scene.layout.Pane;


/**
 * Represents the view of the user's inventory.
 * Observes UserInventory for notification of valid purchase and associated card
 * Observed by Deck for notification of dealing new card
 */
public class CardColumnView extends Pane {

  // This will be the token type of the bonus associated to the card
  private final TokenType typeOfColumn;
  private final Dimension screenSize;
  private int ncardsInColumn = 0;
  private final Counter numCardsDisplay;

  /**
   * Creates a HandColumnView.
   *
   * @param type       the type of token bonus of the card
   * @param screenSize the size of the screen
   */
  public CardColumnView(TokenType type, Dimension screenSize) {
    this.typeOfColumn = type;
    this.screenSize = screenSize;
    this.numCardsDisplay = new Counter(ncardsInColumn);
  }

  /**
   * Returns the label with the number of cards in that column.
   *
   * @return the card counter label
   */
  public Counter getNumCardsDisplay() {
    return numCardsDisplay;
  }
  
  public TokenType getTokenType() {
    return typeOfColumn;
  }

}
