package comp361.f2022hexanome15.splendorclient.model.cards;

import comp361.f2022hexanome15.splendorclient.gui.gameboard.CardView;
import comp361.f2022hexanome15.splendorclient.model.action.Move;
import comp361.f2022hexanome15.splendorclient.model.tokens.Token;

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
