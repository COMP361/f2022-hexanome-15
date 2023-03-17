package ca.mcgill.splendorserver.model.action;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.DeckType;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import java.util.Optional;

/**
 * Represents a move with the action a player made,
 * the card that was purchased/reserved (if any) and the username of the player.
 */
public class Move {

  private final PlayerWrapper         player;
  private final Action                action;
  private final Card        card;
  private final TokenType selectedTokenTypes; // which token type if move involves token
  private final DeckType    deckType;
  private final TradingPostSlot  tradingPostSlot;
  private final Noble     noble;

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
   * @param noble              a noble tile that represents the noble
   *                           which will visit as a result of selecting this move
   * @param tradingPostSlot    The trading post slot that the player is acquiring a power from
   * @param selectedTokenTypes The type(s) of token if any, can be null.
   */
  public Move(Action action, Card card, PlayerWrapper player,
              DeckType deckType, Noble noble,
              TradingPostSlot tradingPostSlot,
              TokenType selectedTokenTypes
  ) {
    this.action             = action;
    this.card               = card;
    this.player             = player;
    this.selectedTokenTypes = selectedTokenTypes;
    this.deckType           = deckType;
    this.tradingPostSlot    = tradingPostSlot;
    this.noble              = noble;
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
   * Returns the deck type of the deck the player is taking a card from.
   *
   * @return the deck type of the deck the player is taking a card from
   */
  public DeckType getDeckType() {
    return deckType;
  }

  /**
   * Returns the token types of token piles the player is taking from.
   *
   * @return the token types of token piles the player is taking from
   */
  public TokenType getSelectedTokenTypes() {
    return selectedTokenTypes;
  }

  /**
   * Returns the noble that is visiting the player.
   *
   * @return the noble that is visiting the player
   */
  public Noble getNoble() {
    return noble;
  }

  /**
   * Returns the trading post slot that a player is unlocking.
   *
   * @return the trading post slot that a player is unlocking
   */
  public TradingPostSlot getTradingPostSlot() {
    return tradingPostSlot;
  }

  /**
   * Returns the current player's action.
   *
   * @return the current player's action
   */
  public Action getAction() {

    return action;
  }

  /**
   * Returns the name of the player who performed this action.
   *
   * @return the name of the player who performed this action
   */
  public String getPlayerName() {
    return player.getName();
  }

  @Override
  public String toString() {
    return "Move{"
             + "player=" + player
             + ", action=" + action
             + ", card=" + card
             + ", tokenTypes=" + selectedTokenTypes
             + ", deckLevel=" + deckType
             + ", tradingPostSlot=" + tradingPostSlot
             + '}';
  }
}
