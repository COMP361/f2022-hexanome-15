package comp361.f2022hexanome15.splendorclient.model.cards;

import comp361.f2022hexanome15.splendorclient.gui.gameboard.CardView;
import comp361.f2022hexanome15.splendorclient.model.tokens.Token;

/**
 * Observer interface.
 */
public interface Observer {

  default void onAction(Card card) {
	  
  }

  default void onAction(Token token) {
	  
  }
  
  default void onAction(boolean bIncrement) {
	  
  }
  
  default void onAction(CardView cardView) {
    
  }
}
