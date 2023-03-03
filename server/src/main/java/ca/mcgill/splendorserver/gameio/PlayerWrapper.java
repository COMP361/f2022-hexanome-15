/**
 * Nov 23, 2022.
 * TODO
 */

package ca.mcgill.splendorserver.gameio;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.hibernate.annotations.Immutable;

/**
 * Flyweight PlayerWrapper.
 *
 * @author zacharyhayden
 */
@Entity
@Immutable // immutable so no issues saving or deleting, but no change is made if changes are tried
public class PlayerWrapper {
  @Id
  private String userName; // assuming this is unique amongst all players

  private static final Map<String, PlayerWrapper> PLAYER_MAP = new HashMap<>();

  PlayerWrapper(String userName) {
    this.userName = userName;
  }

  public PlayerWrapper() {

  }

  /**
   * Returns the player wrapper with the given username.
   * If the player does not exist in the player map, it is added to the map.
   *
   * @param userName The player's username
   * @return The requested player wrapper
   * @throws AssertionError if userName is null or age is less than 1 if not null.
   */
  public static PlayerWrapper newPlayerWrapper(String userName) {
    assert userName != null;
    // if player hasn't been created then age must be supplied
    return PLAYER_MAP.computeIfAbsent(userName, PlayerWrapper::new);
  }

  /**
   * Returns a player's username.
   *
   * @return a player's username
   */
  public String getName() {
    return userName;
  }

  @Override
  public String toString() {
    return "Player{" + userName + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlayerWrapper that)) {
      return false;
    }
    return userName.equals(that.userName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userName);
  }
}
