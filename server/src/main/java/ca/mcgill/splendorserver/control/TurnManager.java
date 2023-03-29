/**
 * Nov 23, 2022.
 */

package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

/**
 * Take turn functionality.
 *
 * @author zacharyhayden
 */
@Embeddable
public class TurnManager implements Iterable<PlayerWrapper> {
  /*
   * Creating circular data structure to store and update turns
   */
  private List<PlayerWrapper> turns;

  /**
   * Take turn.
   *
   * @param players the players in the game
   */
  public TurnManager(Collection<PlayerWrapper> players) {
    turns = new ArrayList<>(players);
  }

  /**
   * Creates a turn manager.
   */
  public TurnManager() {

  }

  /**
   * Removes a player from turns.
   *
   * @param player the player to be removed
   */
  public void removePlayer(PlayerWrapper player) {
    turns.remove(player);
  }

  /**
   * Returns the player wrapper of the player whose turn it is.
   *
   * @return the player wrapper of the player whose turn it is
   */
  public PlayerWrapper whoseTurn() {
    return turns.get(0);
  }

  /**
   * Removes the player whose turn it currently is and puts them at the end of the
   * line, moving the next player up.
   *
   * @return the player whose turn it now is
   */
  public PlayerWrapper endTurn() {
    assert !turns.isEmpty() : "Cannot end turns for a list which isn't populated";
    PlayerWrapper player = turns.get(0);
    turns.remove(0);
    turns.add(player);
    return turns.get(0); // the new head of queue -> player whose turn it is
  }

  @Override
  public Iterator<PlayerWrapper> iterator() {
    return turns.iterator();
  }

  @Override
  public String toString() {
    return "TurnManager [aTurns=" + turns + "]";
  }

}
