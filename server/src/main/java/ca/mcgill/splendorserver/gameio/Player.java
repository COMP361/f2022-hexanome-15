package ca.mcgill.splendorserver.gameio;

/**
 * Represents a player in the game.
 */
public class Player {
  
  private String name;
  private String preferredColour;

  /**
   * Creates a Player object.
   *
   * @param name the player's name
   * @param preferredColour the player's preferred colour
   */
  public Player(String name, String preferredColour) {
    this.name = name;
    this.preferredColour = preferredColour;
  }

  /**
   * Returns the name of the player.
   *
   * @return the name of the player
   */
  public String getName() {
    return name;
  }

}
