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

  /**
   * Adds an active game to the map of active games.
   *
   * @param manager the game to be added
   */
  public static void addActiveGame(SplendorGame manager) {
    if (!activeGames.containsKey(manager.getGameId())) {
      activeGames.put(manager.getGameId(), manager);
      gameStates.put(manager.getGameId(), manager.hashCode());
    }
  }

  /**
   * Removes an active game from the map of active games.
   *
   * @param manager the game to be removed
   */
  public static void removeActiveGame(SplendorGame manager) {
    activeGames.remove(manager.getGameId());
  }

  /**
   * Retrieves the active game with the given id.
   *
   * @param gameid the id of the active game
   * @return the active game
   */
  public static Optional<SplendorGame> getActiveGame(long gameid) {
    if (activeGames.containsKey(gameid)) {
      return Optional.of(activeGames.get(gameid));
    } else {
      return Optional.empty();
    }
  }

  /**
   * Checks if an update is required.
   *
   * @param gameid the game id of the active game
   * @return a boolean determining if an update is required
   */
  public static boolean requiresUpdate(long gameid) {
    if (activeGames.containsKey(gameid)) {
      if (gameStates.containsKey(gameid)) {
        if (activeGames.get(gameid).hashCode() != gameStates.get(gameid)) {
          gameStates.put(gameid, activeGames.get(gameid).hashCode());
          return true;
        } else {
          if (activeGames.get(gameid).getRequiresUpdate()) {
            activeGames.get(gameid).setRequiresUpdate(false);
            return true;
          }
          return false;
        }
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
