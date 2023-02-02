/**
 * Nov 23, 2022.
 * TODO
 */

package ca.mcgill.splendorserver.games;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Flyweight PlayerWrapper.
 *
 * @author zacharyhayden
 */
@Entity
public class PlayerWrapper {
  @Id
  private final String userName; // assuming this is unique amongst all players
  // TODO: should we track prefered color???

  private static final Map<String, PlayerWrapper> PLAYER_MAP = new HashMap<>();

  private PlayerWrapper(String userName) {
    this.userName = userName;
  }

  /**
   * Returns the player wrapper with the given username.
   * If the player does not exist in the player map, it is added to the map.
   *
   * @param userName The player's username
   * @return The requested player wrapper
   * @throws AssertionError if userName is null.
   */
  public static PlayerWrapper newPlayerWrapper(String userName) {
    assert userName != null;
    if (PLAYER_MAP.containsKey(userName)) {
      return PLAYER_MAP.get(userName);
    } else {
      PlayerWrapper newPlayerWrapper = new PlayerWrapper(userName);
      PLAYER_MAP.put(userName, newPlayerWrapper);
      return newPlayerWrapper;
    }
  }

  /**
   * Returns a player's username.
   *
   * @return a player's username
   */
  public String getName() {
    return userName;
  }

}
