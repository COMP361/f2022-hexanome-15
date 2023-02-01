package ca.mcgill.splendorserver.control;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the game instances. TODO: maybe long polling but seems more nice to have than necessary. 
 * 
 * @author lawrenceberardelli
 *
 */
public class BroadcastManager {
  
  private static List<GameBoardManager> activeGames = new ArrayList<GameBoardManager>();
  
  private BroadcastManager() {
  }
  
  public static void addActiveGame(GameBoardManager manager) {
    for (GameBoardManager gameManager : activeGames) {
      if (gameManager.getGameId() == manager.getGameId()) 
        return;
    }
    activeGames.add(manager);
  }
  
  public static GameBoardManager getActiveGame(long gameid) {
    for (GameBoardManager manager : activeGames) {
      if (manager.getGameId() == gameid) {
        return manager;
      }
    }
    return null;
  }

}
