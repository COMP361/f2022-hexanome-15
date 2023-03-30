package ca.mcgill.splendorserver.model.savegame;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.mcgill.splendorserver.model.InventoryJson;
import ca.mcgill.splendorserver.model.TradingPostJson;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;

/**
 * The JSON translation of the gameboard for savegames
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
  public Map<TokenType, Integer> tokenField = new HashMap<TokenType, Integer>();
  public List<TradingPostJson> tradingPosts = new ArrayList<>();
  public List<Integer> cities = new ArrayList<>();
  
  public GameBoardJson(String whoseTurn, List<InventoryJson> inventories, List<DeckJson> decks, 
      List<Integer> nobles, List<Integer> cardField, EnumMap<TokenType, TokenPile> tokenField,
      List<TradingPostJson> tradingPosts, List<Integer> cities) {
    
  }

}
