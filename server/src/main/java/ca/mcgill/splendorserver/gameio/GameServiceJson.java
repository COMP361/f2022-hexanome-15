package ca.mcgill.splendorserver.games;

public class GameServiceJson {

  /**
   * @param name              name as the unique identifying name of this gameservice.
   * @param displayName       as the name with which this gameservice will be displayed.
   * @param location          as the location on which this gameservice is hosted.
   * @param minSessionPlayers
   * @param maxSessionPlayers
   * @param webSupport
   */
  public GameServiceJson(String name, String displayName, String location, String minSessionPlayers,
                         String maxSessionPlayers, String webSupport
  ) {
    super();
    this.name              = name;
    this.displayName       = displayName;
    this.location          = location;
    this.minSessionPlayers = minSessionPlayers;
    this.maxSessionPlayers = maxSessionPlayers;
    this.webSupport        = webSupport;
  }

  public GameServiceJson() {

  }

  private String name;
  private String displayName;
  private String location;
  private String minSessionPlayers;
  private String maxSessionPlayers;
  private String webSupport;

}