package ca.mcgill.splendorclient.control;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import javafx.application.Platform;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

/**
 * Game Controller.
 */
public class GameController {
  
  private Long gameId;
  private int currentState;
  
  private static GameController instance = new GameController();
  
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
      while (true) {
        HttpResponse<JsonNode> response = Unirest
            .get(String.format("http://%s/api/games/%d/board", LobbyServiceExecutor.SERVERLOCATION, gameId))
            .asJson();
        if (response.hashCode() != currentState) {
          System.out.println(response.getBody().toPrettyString());
          currentState = response.hashCode();
          Platform.runLater(new Runnable() {

            @Override
            public void run() {
              //update gameboard view
            }
            
          });
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
