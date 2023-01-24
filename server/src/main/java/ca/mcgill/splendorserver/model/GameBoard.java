package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.Noble;
import ca.mcgill.splendorclient.model.cards.Card;
import ca.mcgill.splendorclient.model.cards.Deck;
import ca.mcgill.splendorclient.model.tokens.TokenPile;
import ca.mcgill.splendorclient.model.userinventory.UserInventory;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/**
 * Model of the gameboard. Necessary due to permanence requirement. 
 * Only ever used for initial setup, the rest can be taken care of by the actions. 
 *
 * @author lawrenceberardelli
 */
public class GameBoard {
  
  private List<UserInventory> inventories;
  private List<Deck> decks;
  private List<Card> cardField;
  private List<TokenPile> tokenPiles;
  private List<Noble> nobles;
  private String currentPlayer;

  /**
   * Returns the current player.
   *
   * @return the current player
   */
  public String getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * Sets the current player.
   *
   * @param currentPlayer the current player
   */
  public void setCurrentPlayer(String currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  /**
   * Creates a gameboard.
   *
   * @param inventories The player inventories in the game
   * @param decks The decks in the game
   * @param cardField The cards dealt onto the gameboard
   * @param tokenPiles The token piles that are on the gameboard
   * @param nobles The nobles in the game
   */
  public GameBoard(List<UserInventory> inventories, List<Deck> decks,
                   List<Card> cardField, List<TokenPile> tokenPiles, List<Noble> nobles) {
    this.inventories = inventories;
    this.decks = decks;
    this.cardField = cardField;
    this.tokenPiles = tokenPiles;
    this.nobles = nobles;
  }

  /**
   * Sends gameboard to server.
   *
   * @param gameid The game id
   */
  public void sendToServer(int gameid) {
    Gson gson = new Gson();
    List<List<Card>> decksAsCards = new ArrayList<List<Card>>();
    for (Deck deck : decks) {
      decksAsCards.add(deck.getCards());
    }
    String json = gson.toJson(decksAsCards);
    System.out.println(json);
    LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR.sendGameboard(json, gameid);
  }
}
