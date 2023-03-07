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

/**
 * Game Controller.
 */
public class GameController {
  
  private Long gameId;
  private String currentState;
  private GameBoardView localView;
  
  private static GameController instance = new GameController();
  
  public void bindView(GameBoardView view) {
	localView = view;
  }
  
  private GameController() {
    
  }
  
  public static GameController getInstance() {
    return instance;
  }
  
  public void setGameId(Long gameId) {
    this.gameId = gameId;
  }
  
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
//          GameBoardJson gameboardJson = new Gson().fromJson(response.getBody().toString(), GameBoardJson.class);
//          String currentTurn = gameboardJson.getWhoseTurn();
          Platform.runLater(new Runnable() {

            @Override
            public void run() {
              //update gameboard view
              JSONArray cardArray = response.getBody().getObject().optJSONArray("cardField");
              int[] cardIDs = new int[cardArray.length()];
              for (int i = 0; i < cardArray.length(); i++) {
                cardIDs[i] = cardArray.getInt(i);
              }
              GameBoardView.updateCardViews(cardIDs);
            }
            
          });
          String currentTurn = response.getBody().getObject().getString("whoseTurn");
          if (currentTurn.equals(User.THISUSER.getUsername()) && !requestedActions) {
            requestedActions = true;
            GameBoardView.setWhoseTurnField("Your Turn");
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
          else {
            requestedActions = false;
            GameBoardView.setWhoseTurnField(currentTurn + "'s Turn");
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
