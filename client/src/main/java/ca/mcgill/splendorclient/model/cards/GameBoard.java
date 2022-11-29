package ca.mcgill.splendorclient.model.cards;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.Noble;
import ca.mcgill.splendorclient.model.tokens.TokenPile;
import ca.mcgill.splendorclient.model.userinventory.UserInventory;

/**
 * Model of the gameboard. Necessary due to permanence requirement. 
 * Only ever used for initial setup, the rest can be taken care of by the actions. 
 * 
 * @author lawrenceberardelli
 *
 */
public class GameBoard {
  
  private List<UserInventory> inventories;
  private List<Deck> decks;
  private List<Card> cardField;
  private List<TokenPile> tokenPiles;
  private List<Noble> nobles;
  private String currentPlayer;

  public String getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(String currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public GameBoard(List<UserInventory> inventories, List<Deck> decks,
                   List<Card> cardField, List<TokenPile> tokenPiles, List<Noble> nobles) {
    this.inventories = inventories;
    this.decks = decks;
    this.cardField = cardField;
    this.tokenPiles = tokenPiles;
    this.nobles = nobles;
  }

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
