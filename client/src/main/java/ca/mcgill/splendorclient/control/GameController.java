package ca.mcgill.splendorclient.control;

import com.google.gson.Gson;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.GameBoardJson;
import ca.mcgill.splendorclient.model.users.User;
import ca.mcgill.splendorclient.view.gameboard.GameBoardView;
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
  public static void start() {
    new Thread(instance.new BoardUpdater()).start();
  }
  
  
  private class BoardUpdater extends Thread {

    @Override
    public void run() {
      //localView = GameBoardView.
      while (true) {
        HttpResponse<JsonNode> response = Unirest
            .get(String.format("http://%s/api/games/%d/board", LobbyServiceExecutor.SERVERLOCATION, gameId))
            .asJson();
        if (response.getBody().toPrettyString() != currentState) {
          System.out.println(response.getBody().toPrettyString());
          currentState = response.getBody().toPrettyString();
          //GameBoardJson gameboardJson = new Gson().fromJson(response.getBody().toString(), GameBoardJson.class);
          //String currentTurn = gameboardJson.getWhoseTurn();
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
          /*if (currentTurn.equals(User.THISUSER.getUsername())) {
            HttpResponse<JsonNode> moveMap = Unirest
                .get(String.format("http://%s/api/games/%d/players/%s/action", 
                    LobbyServiceExecutor.SERVERLOCATION, gameId, currentTurn))
                .asJson();
            System.out.println(moveMap.getBody().toPrettyString()); 
          }*/
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
