package ca.mcgill.splendorclient.model.cards;

import ca.mcgill.splendorclient.gui.gameboard.CardView;
import ca.mcgill.splendorclient.model.action.Move;
import ca.mcgill.splendorclient.model.tokens.Token;

/**
 * Observer interface. Getting pretty crowded. Might have to revisit this in the future. 
 */
public interface Observer {

  default void onAction(Card card) {

  }

  default void onAction(Token token) {

  }

  default void onAction(boolean increment) {

  }

  default void onAction(CardView cardView) {

  }
  
  default void onAction() {
    
  }
  
  default void onAction(Move move) {
    
  }
}
