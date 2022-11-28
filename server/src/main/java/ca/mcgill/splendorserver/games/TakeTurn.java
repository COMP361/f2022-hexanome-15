/**
 * Nov 23, 2022
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
 * @author zacharyhayden
 *
 */
@Embeddable
public class TakeTurn implements Iterable<PlayerWrapper> {
	/*
	 * Creating circular data structure to store and update turns
	 */
	@OneToMany
	private final List<PlayerWrapper> aTurns;

	public TakeTurn(Collection<PlayerWrapper> pPlayers) {
		aTurns = new CopyOnWriteArrayList<>(pPlayers);
	}

	public void removePlayer(PlayerWrapper pPlayer) {
		aTurns.remove(pPlayer);
	}

	/**
	 * 
	 * @return the player wrapper of the player who's turn it is
	 */
	public PlayerWrapper whosTurn() {
		return aTurns.get(0);
	}

	/**
	 * Removes the player who's turn it currently is and puts them at the end of the
	 * line, moving the next player up
	 * 
	 * @return the player fly-weight who's turn it now is
	 */
	public PlayerWrapper endTurn() {
		PlayerWrapper player = aTurns.get(0);
		aTurns.remove(0);
		aTurns.add(player);
		return aTurns.get(0); // the new head of queue
	}

	@Override
	public Iterator<PlayerWrapper> iterator() {
		return aTurns.iterator();
	}

	@Override
	public String toString() {
		return "TakeTurn [aTurns=" + aTurns + "]";
	}

}
