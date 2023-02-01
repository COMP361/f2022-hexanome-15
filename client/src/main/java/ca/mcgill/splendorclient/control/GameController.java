package ca.mcgill.splendorclient.control;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import javafx.application.Platform;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class GameController {
  
  private Long gameId;
  private static BoardUpdater updater;
  private int currentState;
  
  public GameController(Long gameId) {
    this.gameId = gameId;
    updater = new BoardUpdater();
  }
  
  public void start() {
    new Thread(updater).start();
  }
  
  
  private class BoardUpdater extends Thread {

    @Override
    public void run() {
      while(true) {
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
