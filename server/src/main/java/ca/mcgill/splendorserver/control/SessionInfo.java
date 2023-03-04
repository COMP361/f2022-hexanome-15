package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Encapsulates the session info which is populated upon session startup when request received from
 * the lobby service.
 */
public class SessionInfo implements Iterable<PlayerWrapper> {

  private final List<PlayerWrapper> players;
  private       String              gameServer;
  private       PlayerWrapper       gameCreator;
  private       String              saveGameId;

  /**
   * Creates a SessionInfo object.
   *
   * @param players the players in the session
   */
  public SessionInfo(List<PlayerWrapper> players) {
    this.players = players;
  }

  @Override
  public String toString() {
    return "SessionInfo{"
             + "players=" + players
             + ", gameServer='" + gameServer + '\''
             + ", gameCreator=" + gameCreator
             + ", saveGameId='" + saveGameId + '\''
             + '}';
  }

  /**
   * Creates a SessionInfo object.
   *
   * @param gameServer  the location of the server.
   * @param players     list of players in game.
   * @param gameCreator game creator name.
   * @param saveGameId  can be null.
   */
  public SessionInfo(String gameServer, List<PlayerWrapper> players, PlayerWrapper gameCreator,
                     String saveGameId
  ) {
    assert gameServer != null && players != null && gameCreator != null;
    this.gameServer  = gameServer;
    this.players     = players;
    this.gameCreator = gameCreator;
    this.saveGameId  = saveGameId;
  }

  /**
   * Returns the players in the session.
   *
   * @return the players in the session
   */
  public List<PlayerWrapper> getPlayers() {
    return players;
  }

  /**
   * Returns the game server of the session.
   *
   * @return the game server of the session
   */
  public String getGameServer() {
    return gameServer;
  }

  /**
   * Returns the game creator of the session.
   *
   * @return the game creator of the session
   */
  public PlayerWrapper getGameCreator() {
    return gameCreator;
  }

  /**
   * Returns the number of players in the session.
   *
   * @return the number of players in the session
   */
  public int getNumPlayers() {
    return players.size();
  }

  /**
   * Retrieves the player with the given username.
   *
   * @param username the given username
   * @return the requested player
   */
  public Optional<PlayerWrapper> getPlayerByName(String username) {
    for (PlayerWrapper pw : this) {
      if (pw.getName()
            .equals(username)) {
        return Optional.of(pw);
      }
    }
    return Optional.empty();
  }


  @Override
  public Iterator<PlayerWrapper> iterator() {
    return players.iterator();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SessionInfo strings)) {
      return false;
    }
    return Objects.equals(getGameServer(), strings.getGameServer())
        && Objects.equals(players, strings.players)
        && Objects.equals(getGameCreator(), strings.getGameCreator())
        && Objects.equals(saveGameId, strings.saveGameId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getGameServer(), players, getGameCreator(), saveGameId);
  }
}
