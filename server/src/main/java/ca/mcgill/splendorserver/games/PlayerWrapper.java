/**
 * Nov 23, 2022.
 * TODO
 */

package ca.mcgill.splendorserver.games;

import java.util.HashMap;
import java.util.Map;
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
  private int    age;

  private static final Map<String, PlayerWrapper> PLAYER_MAP = new HashMap<>();

  private PlayerWrapper(String userName, int age) {
    this.userName = userName;
    this.age      = age;
  }

  public PlayerWrapper() {

  }

  /**
   * Returns the player wrapper with the given username.
   * If the player does not exist in the player map, it is added to the map.
   *
   * @param userName The player's username
   * @param age      age of the player, can be null if player with username is already created.
   * @return The requested player wrapper
   * @throws AssertionError if userName is null or age is less than 1 if not null.
   */
  public static PlayerWrapper newPlayerWrapper(String userName, Integer age) {
    assert userName != null;
    // if player hasn't been created then age must be supplied
    assert PLAYER_MAP.containsKey(userName) || age != null;
    return PLAYER_MAP.computeIfAbsent(userName, s -> new PlayerWrapper(s, age));
  }

  /**
   * Returns a player's username.
   *
   * @return a player's username
   */
  public String getName() {
    return userName;
  }

  public int getAge() {
    return age;
  }

  @Override
  public String toString() {
    return "Player{" + userName + "}";
  }
}
