package ca.mcgill.splendorserver.control;

import org.json.JSONObject;

/**
 * Interface for LobbyServiceExecutor.
 */
public interface LobbyServiceExecutorInterface {

  /**
   * Registers a game service. This should not be called by client and is kept here
   * for reference while changes are made. Servers should register themselves
   * as per LS diagram.
   *
   * @param maxSessionPlayers the max amount of players that can be in a session
   * @param minSessionPlayers the min amount of players that can be in a session
   * @param gameName          the name of the game
   * @param displayName       the name of the display
   * @param webSupport        boolean value for webSupport
   */
  void register_gameservice(int maxSessionPlayers,
                                   int minSessionPlayers, String gameName,
                                   String displayName,
                                   boolean webSupport);

  /**
   * Sends a savegame to the lobby service.
   *
   * @param body the body of the save game
   * @param gameserviceName the name of the game service
   * @param id the id of the save game
   */
  void save_game(String body, String gameserviceName, String id);
}
