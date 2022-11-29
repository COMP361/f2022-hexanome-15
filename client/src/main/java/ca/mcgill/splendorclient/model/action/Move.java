package ca.mcgill.splendorclient.model.action;

import ca.mcgill.splendorclient.model.cards.Card;

public class Move {
  
  private Action action;
  private Card card;
  private String playername;
  
  
  public Move(Action action, Card card, String playername) {
    this.action = action;
    this.card = card;
    this.playername = playername;
  }
  
  public Card getCard() {
    return card;
  }
  
  public String getName() {
    return playername;
  }
  
  public Action getAction() {
    return action;
  }

}
