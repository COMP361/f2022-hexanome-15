package ca.mcgill.splendorserver.model.action;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.DeckType;
import ca.mcgill.splendorserver.model.cards.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import java.util.Optional;

/**
 * Represents a move with the action a player made,
 * the card that was purchased/reserved (if any) and the username of the player.
 */
public class Move {

  private final PlayerWrapper         player;
  private final Action                action;
  private final Optional<Card>        card;
  private final Optional<TokenType[]> selectedTokenTypes; // which token type if move involves token
  private final Optional<TokenType[]> returnedTokenTypes; // token types selected to be returned
  private final Optional<DeckType>    deckType;
  private final Optional<Noble>       noble;

  /**
   * Creates a Move.
   *
   * @param action             The action that a player did.
   * @param card               The card that was purchased/reserved, can be null if none or not available to
   *                           see by the client.
   * @param player             The player's username.
   * @param deckType           The level of the deck from which they can take from if they want, can be null.
   * @param returnedTokenTypes tokens that are to be returned as a result of having > 10 tokens as
   *                           a result of the tokens taken in this move.
   * @param noble              a noble tile that represents the noble which will visit as a result of selecting this move
   * @param selectedTokenTypes the type(s) of token if any, can be null.
   */
  public Move(Action action, Card card, PlayerWrapper player,
              DeckType deckType, TokenType[] returnedTokenTypes, Noble noble,
              TokenType... selectedTokenTypes
  ) {
    this.action             = action;
    this.card               = Optional.ofNullable(card);
    this.player             = player;
    this.selectedTokenTypes = Optional.ofNullable(selectedTokenTypes);
    this.returnedTokenTypes = Optional.ofNullable(returnedTokenTypes);
    this.deckType           = Optional.ofNullable(deckType);
    this.noble              = Optional.ofNullable(noble);
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

  public Optional<DeckType> getDeckType() {
    return deckType;
  }

  public Optional<TokenType[]> getSelectedTokenTypes() {
    return selectedTokenTypes;
  }

  public Optional<TokenType[]> getReturnedTokenTypes() {
    return returnedTokenTypes;
  }

  public Optional<Noble> getNoble() {
    return noble;
  }

  /**
   * Returns the current player's action.
   *
   * @return the current player's action
   */
  public Action getAction() {
    return action;
  }

  @Override
  public String toString() {
    return "Move{"
        + "player=" + player
        + ", action=" + action
        + ", card=" + card
        + ", tokenTypes=" + selectedTokenTypes
        + ", deckLevel=" + deckType
        + '}';
  }
}
