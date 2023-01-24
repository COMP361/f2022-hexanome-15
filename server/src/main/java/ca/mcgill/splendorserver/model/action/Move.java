package ca.mcgill.splendorserver.model.action;

import ca.mcgill.splendorserver.model.cards.Card;

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

  /**
   * Returns the card purchased during this move.
   *
   * @return the card purchased during this move
   */
  public Card getCard() {
    return card;
  }

  /**
   * Returns the current player's username.
   *
   * @return the current player's username
   */
  public String getName() {
    return playername;
  }

  /**
   * Returns the current player's action.
   *
   * @return the current player's action
   */
  public Action getAction() {
    return action;
  }

}
