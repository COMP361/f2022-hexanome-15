package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArms;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
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
  private String gameServer;
  private List<InventoryJson> inventories;
  private List<DeckJson> decks = new ArrayList<>();
  private List<Integer> nobles = new ArrayList<>();
  private List<Integer> cardField = new ArrayList<>();
  private Map<TokenType, Integer> tokenField = new HashMap<TokenType, Integer>();
  private List<TradingPostJson> tradingPosts = new ArrayList<>();
  private List<Integer> cities = new ArrayList<>();

  /**
   * Creates a gameboardjson object. Should be based on the actual gameboard. 
   *
   * @param gameServer the game service of this gameboard
   * @param whoseTurn the name of the player whose turn it is
   * @param inventories the player inventories
   * @param decks the decks on the field
   * @param nobles nobles on the field
   * @param cardField cards on the card field
   * @param tokenField tokens on the playing field
   * @param tradingPostSlots trading post slots on the field
   * @param cities cities on the field
   */
  public GameBoardJson(String gameServer, String whoseTurn,
                       List<InventoryJson> inventories, List<Deck> decks,
                       List<Noble> nobles, List<Card> cardField,
                       EnumMap<TokenType, TokenPile> tokenField,
                       List<TradingPostSlot> tradingPostSlots, List<City> cities) {
    this.whoseTurn = whoseTurn;
    this.gameServer = gameServer;
    this.inventories = inventories;
    for (Deck deck : decks) {
      this.decks.add(new DeckJson(deck.getSize(), deck.getType()));
    }
    for (Noble noble : nobles) {
      this.nobles.add(noble.getId());
    }
    for (Card card : cardField) {
      this.cardField.add(card.getId());
    }
    for (TokenType type : tokenField.keySet()) {
      this.tokenField.put(type, tokenField.get(type).getSize());
    }
    for (TradingPostSlot tradingPostSlot : tradingPostSlots) {
      List<CoatOfArmsType> coatOfArmsTypes = new ArrayList<>();
      for (CoatOfArms coatOfArms : tradingPostSlot.getAcquiredCoatOfArmsList()) {
        coatOfArmsTypes.add(coatOfArms.getType());
      }
      this.tradingPosts.add(new TradingPostJson(tradingPostSlot.getId(), coatOfArmsTypes));
    }
    for (City city : cities) {
      this.cities.add(city.getId());
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
