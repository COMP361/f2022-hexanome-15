package ca.mcgill.splendorserver.control;

import java.util.Iterator;
import java.util.List;

public class SessionInfo implements Iterable<String> {

  private String gameServer;
  private List<String> players;
  private String gameCreator;
  private String saveGameId;

  public String getGameServer() {
    return gameServer;
  }

  public String getGameCreator() {
    return gameCreator;
  }
  
  public int getNumPlayers() {
    return players.size();
  }  

  @Override
  public String toString() {
    return "LaunchInfo{" +
             "gameServer='" + gameServer + '\'' +
             ", gameCreator='" + gameCreator + '\'' +
             '}';
  }

  @Override
  public Iterator<String> iterator() {
    return players.iterator();
  }
}
