package comp361.f2022hexanome15.splendorserver.games;

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
public class Game implements Iterable<String> {
	private final String savegame;
	@Id
	private Long gameID; // TODO: implement this from the LS
	@ElementCollection
	@CollectionTable(name = "players")
	private final List<String> players; // contains player user-names

	/**
	 * Creates a Game.
	 * 
	 * @param saveGame optional fork of previously saved game; can be null
	 * @param gameid   unique game ID
	 * @param players  list of players in session
	 */
	public Game(String saveGame, long gameid, List<String> players) {
		this.savegame = saveGame;
		this.gameID = gameid;
		this.players = players;
	}

	public long gameID() {
		return gameID;
	}

	public String savegame() {
		return savegame;
	}

	public void removePlayer(String username) {
		players.remove(username);
	}

	@Override
	public String toString() {
		return "Game [gameID=" + gameID + ", players=" + players + "]";
	}

	@Override
	public Iterator<String> iterator() {
		return players.iterator();
	}

}
