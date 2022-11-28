package ca.mcgill.splendorclient.gui.gameboard;

import ca.mcgill.splendorclient.model.cards.Card;
import ca.mcgill.splendorclient.model.cards.Observer;
import ca.mcgill.splendorclient.model.tokens.TokenType;
import java.awt.Dimension;
import javafx.scene.layout.Pane;

/*
 * TODO: A big design phase is gonna be adding a hand model to the user inventory model
 * Much like other choices, communication between the HandColumnView
 * and the Hand itself can be done with the listener pattern
 * When this class receives a ping (executes onAction)
 * just notify the hand model and pass the card through that way.
 */

/**
 * Represents the view of the user's inventory.
 */
public class HandColumnView extends Pane implements Observer {

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
  public HandColumnView(TokenType type, Dimension screenSize) {
    this.typeOfColumn = type;
    this.screenSize = screenSize;
    this.numCardsDisplay = new Counter(ncardsInColumn);
  }

  @Override
  public void onAction(Card card) {
    // again in this case we'll just match the type of the bonus token
    if (card.getTokenType() == typeOfColumn) {
      CardView cardView = new CardView(screenSize.height / 20f, screenSize.width / 20f);
      cardView.forceCard(card);
      // view.setFill(ColorManager.getColor(card.getTokenType()));
      cardView.setLayoutY(5 * ncardsInColumn);
      this.getChildren().add(cardView);
      ++ncardsInColumn;
    }
    numCardsDisplay.setText(Integer.toString(ncardsInColumn));
  }

  public Counter getNumCardsDisplay() {
    return numCardsDisplay;
  }
}
