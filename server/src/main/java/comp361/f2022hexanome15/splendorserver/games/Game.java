package comp361.f2022hexanome15.splendorserver.games;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Represents a game session.
 *
 * @author zacharyhayden
 */
@Entity
public class Game implements Iterable<PlayerWrapper> {
	private final TakeTurn aTakeTurn;
	private final String savegame;
	@Id
	private Long gameID; // TODO: implement this from the LS

	/**
	 * Creates a Game.
	 * 
	 * @param saveGame optional fork of previously saved game; can be null
	 * @param gameid   unique game ID
	 * @param players  list of players in session
	 */
	public Game(String saveGame, long gameid, Collection<PlayerWrapper> players) {
		this.savegame = saveGame;
		this.gameID = gameid;
		aTakeTurn = new TakeTurn(players);
	}

	public long gameID() {
		return gameID;
	}

	public String savegame() {
		return savegame;
	}

	public void removePlayer(String username) {
		aTakeTurn.removePlayer(PlayerWrapper.newPlayerWrapper(username));
	}

	public PlayerWrapper whosTurn() {
		return aTakeTurn.whosTurn();
	}

	public PlayerWrapper endTurn() {
		return aTakeTurn.endTurn();
	}

	@Override
	public Iterator<PlayerWrapper> iterator() {
		return aTakeTurn.iterator();
	}

}
