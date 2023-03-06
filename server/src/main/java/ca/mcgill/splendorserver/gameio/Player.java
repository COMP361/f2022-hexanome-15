package ca.mcgill.splendorserver.gameio;

public class Player {
  
  private String name;
  private String preferredColour;
  
  public Player(String name, String preferredColour) {
    this.name = name;
    this.preferredColour = preferredColour;
  }
  
  public String getName() {
    return name;
  }

}
