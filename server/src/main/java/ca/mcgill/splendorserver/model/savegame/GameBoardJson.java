package ca.mcgill.splendorserver.model.savegame;

import ca.mcgill.splendorserver.model.InventoryJson;
import ca.mcgill.splendorserver.model.TradingPostJson;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The JSON translation of the gameboard for savegames.
 *
 * @author lawrenceberardelli
 *
 */
public class GameBoardJson {
  
  public String whoseTurn;
  public List<InventoryJson> inventories;
  public List<DeckJson> decks = new ArrayList<>();
  public List<Integer> nobles = new ArrayList<>();
  public List<Integer> cardField = new ArrayList<>();
  public EnumMap<TokenType, Integer> tokenField = new EnumMap<TokenType, Integer>(TokenType.class);
  public List<TradingPostJson> tradingPosts = new ArrayList<>();
  public List<Integer> cities = new ArrayList<>();
  
  /**
   * Creates a gameboard json object.
   *
   * @param whoseTurn current turn at save time
   * @param inventories of players
   * @param decks on playing field
   * @param nobles on playing field
   * @param cardField on playing field
   * @param tokenField on playing field
   * @param tradingPosts on playing field
   * @param cities on playing field
   */
  public GameBoardJson(String whoseTurn, List<InventoryJson> inventories, List<DeckJson> decks, 
      List<Integer> nobles, List<Integer> cardField, EnumMap<TokenType, TokenPile> tokenField,
      List<TradingPostJson> tradingPosts, List<Integer> cities) {
    this.inventories = inventories;
    this.decks = decks;
    this.nobles = nobles;
    this.cardField = cardField;
    for (Entry<TokenType, TokenPile> entry : tokenField.entrySet()) {
      this.tokenField.put(entry.getKey(), entry.getValue().getSize());
    }
    this.tradingPosts = tradingPosts;
    this.cities = cities;
  }

}
