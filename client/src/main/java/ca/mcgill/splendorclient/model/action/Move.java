package ca.mcgill.splendorclient.model.action;

import ca.mcgill.splendorclient.model.cards.Card;

/**
 * Represents a move with the action a player made,
 * the card that was purchased/reserved and the username of the player.
 */
public class Move {
  
  private Action action;
  private Card card;
  private String playername;

  /**
   * Creates a Move.
   *
   * @param action The action that a player did
   * @param card The card that was purchased/reserved
   * @param playername The player's username
   */
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
