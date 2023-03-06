package ca.mcgill.splendorclient.control;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.GameBoardJson;
import ca.mcgill.splendorclient.model.MoveInfo;
import ca.mcgill.splendorclient.model.users.User;
import ca.mcgill.splendorclient.view.gameboard.GameBoardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Map;
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
      while (true) {
        HttpResponse<JsonNode> response = Unirest
            .get(String.format("http://%s/api/games/%d/board", LobbyServiceExecutor.SERVERLOCATION, gameId))
            .asJson();
        if (response.getBody().toPrettyString() != currentState) {
          System.out.println(response.getBody().toPrettyString());
          currentState = response.getBody().toPrettyString();
          GameBoardJson gameboardJson =
              new Gson().fromJson(response.getBody().toString(), GameBoardJson.class);
          String currentTurn = gameboardJson.getWhoseTurn();
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
            }
            
          });
          if (currentTurn.equals(User.THISUSER.getUsername())) {
            HttpResponse<JsonNode> moveMap = Unirest
                .get(String.format("http://%s/api/games/%d/players/%s/action", 
                    LobbyServiceExecutor.SERVERLOCATION, gameId, currentTurn))
                .asJson();
            System.out.println(moveMap.getBody().toPrettyString()); 
            String moves = moveMap.getBody().toString();
            Gson gson = new Gson();
            Map<String, MoveInfo> availableMoves =
                gson.fromJson(moves, new TypeToken<Map<String, MoveInfo>>() {}.getType());
            ActionManager.setCurrentMoveMap(availableMoves);
          } else {
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
