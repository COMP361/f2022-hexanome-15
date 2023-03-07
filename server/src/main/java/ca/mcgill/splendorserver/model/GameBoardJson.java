package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Facilitates the creation of a stripped down gameboard for transmission to client.
 *
 * @author lawrenceberardelli
 *
 */
public class GameBoardJson {
  
  private String whoseTurn;
  private List<InventoryJson> inventories;
  private List<DeckJson> decks = new ArrayList<DeckJson>();
  private List<Noble> nobles;
  private List<Integer> cardField = new ArrayList<Integer>(); //implicit flattened 2d array
  private Map<TokenType, Integer> tokenField = new HashMap<TokenType, Integer>();
  private List<TradingPostJson> tradingPosts = new ArrayList<>();

  /**
   * Creates a gameboardjson object. Should be based on the actual gameboard. 
   *
   * @param whoseTurn the name of the player whose turn it is
   * @param inventories the player inventories
   * @param decks the decks on the field
   * @param nobles nobles on the field
   * @param cardField cards on the card field
   * @param tokenField tokens on the playing field
   * @param tradingPostSlots trading post slots on the field
   */
  public GameBoardJson(String whoseTurn, List<InventoryJson> inventories, List<Deck> decks,
                       List<Noble> nobles, List<Card> cardField,
                       EnumMap<TokenType, TokenPile> tokenField,
                       List<TradingPostSlot> tradingPostSlots) {
    this.whoseTurn = whoseTurn;
    this.inventories = inventories;
    for (Deck deck : decks) {
      this.decks.add(new DeckJson(deck.getSize(), deck.getType()));
    }
    this.nobles = nobles;
    for (Card card : cardField) {
      this.cardField.add(card.getId());
    }
    for (TokenType type : tokenField.keySet()) {
      this.tokenField.put(type, tokenField.get(type).getSize());
    }
    for (TradingPostSlot tradingPostSlot : tradingPostSlots) {
      this.tradingPosts.add(new TradingPostJson(tradingPostSlot.getId(),
          tradingPostSlot.getAcquiredCoatOfArmsList()));
    }
  }

  /**
   * Returns the player whose turn it is.
   *
   * @return the player whose turn it is
   */
  public String getWhoseTurn() {
    return whoseTurn;
  }

}
