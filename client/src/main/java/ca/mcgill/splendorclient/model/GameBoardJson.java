package ca.mcgill.splendorclient.model;

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
  private List<Integer> nobles;
  private List<Integer> cardField = new ArrayList<Integer>();
  private Map<TokenType, Integer> tokenField = new HashMap<TokenType, Integer>();
  private List<TradingPostJson> tradingPosts = new ArrayList<>();
  private List<Integer> cities = new ArrayList<>();
  private List<String> winningPlayers = new ArrayList<>();

  /**
   * Creates a GameBoardJson object.
   */
  public GameBoardJson() {
  }

}
