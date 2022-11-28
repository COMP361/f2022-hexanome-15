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
  private Long gameid; // TODO: implement this from the LS
  @Embedded
  private final TakeTurn takeTurn; // contains player user-names
  @Embedded
  private final GameBoard gameBoard;

  /**
   * Creates a Game.
   *
   * @param saveGame optional fork of previously saved game; can be null
   * @param gameid   unique game ID
   * @param players  list of players in game session as indicated from the rest
   *                 call
   */
  public Game(String saveGame, long gameid, List<PlayerWrapper> players, GameBoard gameBoard) {
    this.savegame = saveGame;
    this.gameid = gameid;
    this.takeTurn = new TakeTurn(players);
    this.gameBoard = gameBoard;
  }

  public long gameid() {
    return gameid;
  }

  public String savegame() {
    return savegame;
  }

  public void removePlayer(PlayerWrapper username) {
    takeTurn.removePlayer(username);
  }

  /**
   * Ends the current players turn and updates the current player.
   *
   * @return the player wrapper whose turn it now is
   */
  public PlayerWrapper endTurn() {
    return takeTurn.endTurn();
  }

  /**
   * Returns the player whose turn it is.
   *
   * @return the player whose turn it is
   */
  public PlayerWrapper whoseTurn() {
    return takeTurn.whoseTurn();
  }

  @Override
  public String toString() {
    return "Game [savegame=" + savegame + ", gameID=" + gameid + ", aTakeTurn=" + takeTurn + "]";
  }

  @Override
  public Iterator<PlayerWrapper> iterator() {
    return takeTurn.iterator();
  }

}
