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
  
  /**
   * Name of current turn holder.
   */
  public String whoseTurn;
  
  /**
   * Inventories on the board.
   */
  public List<InventoryJson> inventories;
  
  /**
   * Decks on the board.
   */
  public List<DeckJson> decks = new ArrayList<>();
  
  /**
   * Nobles on the board.
   */
  public List<Integer> nobles = new ArrayList<>();
  
  /**
   * Cardfield on the board.
   */
  public List<Integer> cardField = new ArrayList<>();
  
  /**
   * Tokens on the board.
   */
  public EnumMap<TokenType, Integer> tokenField = new EnumMap<TokenType, Integer>(TokenType.class);
  
  /**
   * Trading posts on the board, optional.
   */
  public List<TradingPostJson> tradingPosts = new ArrayList<>();
  
  /**
   * Cities on the board, optional.
   */
  public List<Integer> cities = new ArrayList<>();

  /**
   * Winning players.
   */
  public List<String> winningPlayers = new ArrayList<>();

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
   * @param winningPlayers players that won the game
   */
  public GameBoardJson(String whoseTurn, List<InventoryJson> inventories, List<DeckJson> decks, 
      List<Integer> nobles, List<Integer> cardField, EnumMap<TokenType, TokenPile> tokenField,
      List<TradingPostJson> tradingPosts, List<Integer> cities, List<String> winningPlayers) {
    this.whoseTurn = whoseTurn;
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
