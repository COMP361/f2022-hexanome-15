/**
 * Nov 23, 2022.
 * TODO
 */

package ca.mcgill.splendorserver.games;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

/**
 * Take turn mechanic.
 *
 * @author zacharyhayden
 */
@Embeddable
public class TakeTurn implements Iterable<PlayerWrapper> {
  /*
   * Creating circular data structure to store and update turns
   */
  @OneToMany
  private final List<PlayerWrapper> turns;

  public TakeTurn(Collection<PlayerWrapper> players) {
    turns = new CopyOnWriteArrayList<>(players);
  }

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
   * @return the player fly-weight who's turn it now is
   */
  public PlayerWrapper endTurn() {
    PlayerWrapper player = turns.get(0);
    turns.remove(0);
    turns.add(player);
    return turns.get(0); // the new head of queue
  }

  @Override
  public Iterator<PlayerWrapper> iterator() {
    return turns.iterator();
  }

  @Override
  public String toString() {
    return "TakeTurn [aTurns=" + turns + "]";
  }

}
