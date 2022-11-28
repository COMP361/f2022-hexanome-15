package ca.mcgill.splendorserver.games;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Represents a game session.
 *
 * @author zacharyhayden
 */
@Entity
public class Game implements Iterable<PlayerWrapper> {
	private final String savegame;
	@Id
	private Long gameID; // TODO: implement this from the LS
	@Embedded
	private final TakeTurn aTakeTurn; // contains player user-names
	@Embedded
	private final GameBoard aGameBoard;

	/**
	 * Creates a Game.
	 * 
	 * @param saveGame optional fork of previously saved game; can be null
	 * @param gameid   unique game ID
	 * @param players  list of players in game session as indicated from the rest
	 *                 call
	 */
	public Game(String saveGame, long gameid, List<PlayerWrapper> players, GameBoard pGameBoard) {
		this.savegame = saveGame;
		this.gameID = gameid;
		aTakeTurn = new TakeTurn(players);
		aGameBoard = pGameBoard;
	}

	public long gameID() {
		return gameID;
	}

	public String savegame() {
		return savegame;
	}

	public void removePlayer(PlayerWrapper username) {
		aTakeTurn.removePlayer(username);
	}

	/**
	 * ends the current players turn and updates the current player
	 * 
	 * @return the player wrapper who's turn it now is
	 */
	public PlayerWrapper endTurn() {
		return aTakeTurn.endTurn();
	}

	/**
	 * 
	 * @return the player who's turn it is
	 */
	public PlayerWrapper whosTurn() {
		return aTakeTurn.whosTurn();
	}

	@Override
	public String toString() {
		return "Game [savegame=" + savegame + ", gameID=" + gameID + ", aTakeTurn=" + aTakeTurn + "]";
	}

	@Override
	public Iterator<PlayerWrapper> iterator() {
		return aTakeTurn.iterator();
	}

}
