/**
 * Nov 23, 2022
 * TODO
 */
package comp361.f2022hexanome15.splendorserver.games;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author zacharyhayden
 *
 */
public class TakeTurn implements Iterable<PlayerWrapper> {
	/*
	 * Creating circular data structure to store and update turns
	 */
	private final Deque<PlayerWrapper> aTurns;

	public TakeTurn(Collection<PlayerWrapper> pPlayers) {
		aTurns = new ConcurrentLinkedDeque<>(pPlayers);
	}

	public void removePlayer(PlayerWrapper pPlayer) {
		aTurns.remove(pPlayer);
	}

	/**
	 * 
	 * @return the player wrapper of the player who's turn it is
	 */
	public PlayerWrapper whosTurn() {
		return aTurns.element();
	}

	/**
	 * 
	 * @return the player fly-weight who's turn it now is
	 */
	public PlayerWrapper endTurn() {
		PlayerWrapper player = aTurns.poll();
		aTurns.offer(player);
		return aTurns.element(); // the new head of queue
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
