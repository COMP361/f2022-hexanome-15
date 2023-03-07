package ca.mcgill.splendorclient.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.GameBoardJson;
import ca.mcgill.splendorclient.model.MoveInfo;
import ca.mcgill.splendorclient.model.users.User;
import ca.mcgill.splendorclient.view.gameboard.GameBoardView;
import ca.mcgill.splendorclient.view.gameboard.TokenPileView;
import javafx.application.Platform;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

/**
 * Game Controller.
 */
public class GameController {
  
  private Long gameId;
  private String currentState;
  private GameBoardView localView;
  
  private static GameController instance = new GameController();

  /**
   * Sets the GameBoardView to the given view.
   *
   * @param view the given view
   */
  public void bindView(GameBoardView view) {
    localView = view;
  }

  /**
   * Creates a GameController object.
   */
  private GameController() {
    
  }

  /**
   * Returns this instance of GameController.
   *
   * @return this instance of GameController
   */
  public static GameController getInstance() {
    return instance;
  }

  /**
   * Sets the game id of the game.
   *
   * @param gameId the game id
   */
  public void setGameId(Long gameId) {
    this.gameId = gameId;
  }

  /**
   * Returns the game id of the game.
   *
   * @return the game id of the game
   */
  public Long getGameId() {
    return gameId;
  }
  
  
  //TODO: disable multiple calls to this method

  /**
   * Starts the game.
   */
  public static void start() {
    new Thread(instance.new BoardUpdater()).start();
  }
  
  
  private class BoardUpdater extends Thread {

    @Override
    public void run() {
      boolean requestedActions = false;
      while (true) {
        HttpResponse<JsonNode> response = Unirest
            .get(String.format("http://%s/api/games/%d/board", LobbyServiceExecutor.SERVERLOCATION, gameId))
            .asJson();
        if (response.getStatus() == 404) {
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          continue;
        }
        if (!response.getBody().toPrettyString().equals(currentState)) {
          System.out.println(response.getBody().toPrettyString());
          currentState = response.getBody().toPrettyString();
          Platform.runLater(new Runnable() {

            @Override
            public void run() {
              //update gameboard view
              JSONArray cardArray = response.getBody().getObject().optJSONArray("cardField");
              int[] cardids = new int[cardArray.length()];
              for (int i = 0; i < cardArray.length(); i++) {
                cardids[i] = cardArray.getInt(i);
              }
              GameBoardView.updateCardViews(cardids);
              
              //update inventories
              JSONArray inventories = response.getBody().getObject().getJSONArray("inventories");
              for (int player = 0; player < inventories.length(); player++) {
            	JSONObject inventory = (JSONObject) inventories.get(player);
            	JSONArray playerCardArray = inventory.getJSONArray("cards");
            	int[] playerCardids = new int[playerCardArray.length()];
                for (int i = 0; i < playerCardArray.length(); i++) {
                	playerCardids[i] = playerCardArray.getInt(i);
                }
                int[] placeholder = null;
            	GameBoardView.updateInventories(player,
            	  playerCardids,
                  inventory.getJSONObject("tokens").getInt("DIAMOND"),
                  inventory.getJSONObject("tokens").getInt("SAPPHIRE"),
                  inventory.getJSONObject("tokens").getInt("EMERALD"),
                  inventory.getJSONObject("tokens").getInt("RUBY"),
                  inventory.getJSONObject("tokens").getInt("ONYX"),
                  inventory.getJSONObject("tokens").getInt("GOLD"),
                  inventory.getInt("prestige"),
                  placeholder,
                  placeholder);
              }
              
              //update tokens
              JSONObject tokens = response.getBody().getObject().getJSONObject("tokenField");
              GameBoardView.getInstance().getTokenPileViews().get(0).getCounter().setCount(
            		  tokens.getInt("DIAMOND"));
              GameBoardView.getInstance().getTokenPileViews().get(1).getCounter().setCount(
            		  tokens.getInt("SAPPHIRE"));
              GameBoardView.getInstance().getTokenPileViews().get(2).getCounter().setCount(
            		  tokens.getInt("EMERALD"));
              GameBoardView.getInstance().getTokenPileViews().get(3).getCounter().setCount(
            		  tokens.getInt("RUBY"));
              GameBoardView.getInstance().getTokenPileViews().get(4).getCounter().setCount(
            		  tokens.getInt("ONYX"));
              GameBoardView.getInstance().getTokenPileViews().get(5).getCounter().setCount(
            		  tokens.getInt("GOLD"));
              
              //update nobles
              
              
              //update decks
              JSONArray decks = response.getBody().getObject().getJSONArray("decks");
              int[] decksArray = new int[decks.length()];
              for (int i = 0; i < decks.length(); i++) {
            	  decksArray[i] = ((JSONObject) decks.get(i)).getInt("ncards");
              }
              GameBoardView.updateDecks(decksArray);
            }
            
          });
          String currentTurn = response.getBody().getObject().getString("whoseTurn");
          if (currentTurn.equals(User.THISUSER.getUsername()) && !requestedActions) {
            requestedActions = true;
            HttpResponse<JsonNode> moveMap = Unirest
                .get(String.format("http://%s/api/games/%d/players/%s/actions", 
                    LobbyServiceExecutor.SERVERLOCATION, gameId, currentTurn))
                .queryString("access_token", User.THISUSER.getAccessToken())
                .asJson();
            System.out.println(moveMap.getBody().toPrettyString()); 
            String moves = moveMap.getBody().toString();
            Gson gson = new Gson();
            Map<String, MoveInfo> availableMoves = gson.fromJson(moves, new TypeToken<Map<String, MoveInfo>>() {}.getType());
            ActionManager.setCurrentMoveMap(availableMoves);
          }
          else if (!currentTurn.equals(User.THISUSER.getUsername())) {
            requestedActions = false;
            ActionManager.setCurrentMoveMap(new HashMap<String, MoveInfo>());
          }
        }
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    
  }

}
