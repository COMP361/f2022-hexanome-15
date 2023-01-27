package ca.mcgill.splendorserver.control;

public class SessionInfo {

  String gameServer;

  //TODO: Make Player class (subclass of User) and save list of players here

  String gameCreator;

  //TODO: Implement launching from a saved game

  public SessionInfo(String gameServer, String gameCreator) {
    this.gameServer = gameServer;
    this.gameCreator = gameCreator;
  }

  public String getGameServer() {
    return gameServer;
  }

  public String getGameCreator() {
    return gameCreator;
  }

  public void setGameServer(String gameServer) {
    this.gameServer = gameServer;
  }

  public void setGameCreator(String gameCreator) {
    this.gameCreator = gameCreator;
  }

  @Override
  public String toString() {
    return "LaunchInfo{" +
             "gameServer='" + gameServer + '\'' +
             ", gameCreator='" + gameCreator + '\'' +
             '}';
  }
}
