package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.games.PlayerWrapper;
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
  private String gameServer;
  private PlayerWrapper gameCreator;
  private String saveGameId;

  public SessionInfo(List<PlayerWrapper> players) {
    this.players = players;
  }

  /**
   * @param gameServer  the location of the server.
   * @param players     list of players in game.
   * @param gameCreator game creator name.
   * @param saveGameId  can be null.
   */
  public SessionInfo(String gameServer, List<PlayerWrapper> players, PlayerWrapper gameCreator,
                     String saveGameId
  ) {
    assert gameServer != null && players != null && gameCreator != null;
    this.gameServer = gameServer;
    this.players = players;
    this.gameCreator = gameCreator;
    this.saveGameId = saveGameId;
  }

  public List<PlayerWrapper> getPlayers() {
    return players;
  }

  public String getGameServer() {
    return gameServer;
  }

  public PlayerWrapper getGameCreator() {
    return gameCreator;
  }

  public int getNumPlayers() {
    return players.size();
  }

  public Optional<PlayerWrapper> getPlayerByName(String pName) {
    for (PlayerWrapper pw : this) {
      if (pw.getName().equals(pName)) {
        return Optional.of(pw);
      }
    }
    return Optional.empty();
  }

  @Override
  public String toString() {
    return "LaunchInfo{" + "gameServer='" + gameServer + '\'' + ", gameCreator='" + gameCreator
        + '\'' + '}';
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
