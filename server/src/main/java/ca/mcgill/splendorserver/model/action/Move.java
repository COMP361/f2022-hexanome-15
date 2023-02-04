package ca.mcgill.splendorserver.model.action;

import ca.mcgill.splendorserver.games.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.DeckLevel;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import java.util.Optional;

/**
 * Represents a move with the action a player made,
 * the card that was purchased/reserved (if any) and the username of the player.
 */
public class Move {

  private final PlayerWrapper player;
  private final Action action;
  private final Optional<Card> card;
  private final Optional<TokenType[]> tokenTypes; // which token type if move involves token
  private final Optional<DeckLevel> deckLevel;

  /**
   * Creates a Move.
   *  @param action    The action that a player did.
   * @param card      The card that was purchased/reserved, can be null if none or not available to
   *                  see by the client.
   * @param player    The player's username.
   * @param deckLevel The level of the deck from which they can take from if they want, can be null.
   * @param tokenType the type(s) of token if any, can be null.
   */
  public Move(Action action, Card card, PlayerWrapper player,
              DeckLevel deckLevel, TokenType... tokenType
  ) {
    this.action = action;
    this.card = Optional.ofNullable(card);
    this.player = player;
    this.tokenTypes = Optional.ofNullable(tokenType);
    this.deckLevel = Optional.ofNullable(deckLevel);
  }

  /**
   * Returns the card purchased during this move.
   *
   * @return the card purchased during this move
   */
  public Optional<Card> getCard() {
    return card;
  }

  /**
   * Returns the current player's username.
   *
   * @return the current player's username
   */
  public PlayerWrapper getName() {
    return player;
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
