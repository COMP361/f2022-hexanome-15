package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.GameNotFoundException;
import ca.mcgill.splendorserver.model.SplendorGame;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Manages and stores the game instances locally.
 *
 * @author lawrenceberardelli
 */
@Component
public class LocalGameStorage {

  private static final Map<Long, SplendorGame> activeGames = new HashMap<>();
  private static final Map<Long, Integer> gameStates = new HashMap<>();

  private LocalGameStorage() {
  }

  public static void addActiveGame(SplendorGame manager) {
    if (!activeGames.containsKey(manager.getGameId())) {
      activeGames.put(manager.getGameId(), manager);
      gameStates.put(manager.getGameId(), manager.hashCode());
    }
  }

  public static void removeActiveGame(SplendorGame manager) {
    activeGames.remove(manager.getGameId());
  }

  public static Optional<SplendorGame> getActiveGame(long gameid) {
    if (activeGames.containsKey(gameid)) {
      return Optional.of(activeGames.get(gameid));
    } else {
      return Optional.empty();
    }
  }
  
  public static boolean requiresUpdate(long gameid) {
    if (activeGames.containsKey(gameid)) {
      if (gameStates.containsKey(gameid)) {
        if(activeGames.get(gameid).hashCode() != gameStates.get(gameid))
          return true;
        else
          return false;
      }
    }
    throw new GameNotFoundException(gameid);
  }

  /**
   * Check if gameid exists and is active game.
   *
   * @param gameid game ID
   * @return if exists and is active.
   */
  public static boolean exists(long gameid) {
    return activeGames.containsKey(gameid);
  }

}
