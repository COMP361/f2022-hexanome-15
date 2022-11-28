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
 * PlayerWrapper.
 *
 * @author zacharyhayden
 */
@Entity
public class PlayerWrapper {
  @Id
  private final String userName;

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
   */
  public static PlayerWrapper newPlayerWrapper(String userName) {
    if (PLAYER_MAP.containsKey(userName)) {
      return PLAYER_MAP.get(userName);
    } else {
      PlayerWrapper newPlayerWrapper = new PlayerWrapper(userName);
      PLAYER_MAP.put(userName, newPlayerWrapper);
      return newPlayerWrapper;
    }
  }

  public String getaUserName() {
    return userName;
  }

}
