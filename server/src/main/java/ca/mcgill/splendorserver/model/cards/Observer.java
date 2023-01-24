package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorclient.gui.gameboard.CardView;
import ca.mcgill.splendorclient.model.action.Move;
import ca.mcgill.splendorclient.model.tokens.Token;

/**
 * Observer interface. Getting pretty crowded. Might have to revisit this in the future. 
 */
public interface Observer {

  /**
   * This is used to send the card that was purchased to observers.
   *
   * @param card The card that was purchased
   */
  default void onAction(Card card) {

  }

  /**
   * Sends a token to observers.
   *
   * @param token The token to be sent to observers
   */
  default void onAction(Token token) {

  }

  /**
   * This is used when adding tokens or removing tokens.
   *
   * @param increment boolean to determine if tokens are being added or removed
   */
  default void onAction(boolean increment) {

  }

  /**
   * This is used to send a card that was purchased.
   *
   * @param cardView the cardView of the purchased card
   */
  default void onAction(CardView cardView) {

  }

  /**
   * This is used when forwarding moves to the server.
   */
  default void onAction() {
    
  }

  /**
   * This is used to send the current player's move.
   *
   * @param move the move to be sent
   */
  default void onAction(Move move) {
    
  }
}
