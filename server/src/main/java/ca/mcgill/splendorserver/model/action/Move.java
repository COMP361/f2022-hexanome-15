package ca.mcgill.splendorserver.model.action;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.DeckType;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArms;
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
  private final Optional<CoatOfArms>  coatOfArms;
  private final Optional<Noble>       noble;

  /**
   * Creates a Move.
   *
   * @param action             The action that a player did.
   * @param card               The card that was purchased/reserved,
   *                           can be null if none or not available to
   *                           see by the client.
   * @param player             The player's username.
   * @param deckType           The level of the deck from which
   *                           they can take from if they want, can be null.
   * @param returnedTokenTypes tokens that are to be returned as a result of having > 10 tokens as
   *                           a result of the tokens taken in this move.
   * @param noble              a noble tile that represents the noble
   *                           which will visit as a result of selecting this move
   * @param coatOfArms         The coat of  arms that is being placed on a trading route slot
   * @param selectedTokenTypes The type(s) of token if any, can be null.
   */
  public Move(Action action, Card card, PlayerWrapper player,
              DeckType deckType, TokenType[] returnedTokenTypes, Noble noble, CoatOfArms coatOfArms,
              TokenType... selectedTokenTypes
  ) {
    this.action             = action;
    this.card               = Optional.ofNullable(card);
    this.player             = player;
    this.selectedTokenTypes = Optional.ofNullable(selectedTokenTypes);
    this.returnedTokenTypes = Optional.ofNullable(returnedTokenTypes);
    this.deckType           = Optional.ofNullable(deckType);
    this.coatOfArms         = Optional.ofNullable(coatOfArms);
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

  /**
   * Returns the deck type of the deck the player is taking a card from.
   *
   * @return the deck type of the deck the player is taking a card from
   */
  public Optional<DeckType> getDeckType() {
    return deckType;
  }

  /**
   * Returns the token types of token piles the player is taking from.
   *
   * @return the token types of token piles the player is taking from
   */
  public Optional<TokenType[]> getSelectedTokenTypes() {
    return selectedTokenTypes;
  }

  /**
   * Returns the token types of tokens the player is returning to the board.
   *
   * @return the token types of tokens the player is returning to the board
   */
  public Optional<TokenType[]> getReturnedTokenTypes() {
    return returnedTokenTypes;
  }

  /**
   * Returns the noble that is visiting the player.
   *
   * @return the noble that is visiting the player
   */
  public Optional<Noble> getNoble() {
    return noble;
  }

  /**
   * Returns the coat of arms that a player is being placed on a trading route slot.
   *
   * @return the coat of arms that a player is being placed on a trading route slot
   */
  public Optional<CoatOfArms> getCoatOfArms() {
    return coatOfArms;
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
             + ", coatOfArms=" + coatOfArms
             + '}';
  }
}
