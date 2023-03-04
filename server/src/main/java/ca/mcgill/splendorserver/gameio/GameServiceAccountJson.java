package ca.mcgill.splendorserver.gameio;

/**
 * GameServiceAccountJson.
 */
public class GameServiceAccountJson {

  /**
   * Creates a GameServiceAccountJson object.
   *
   * @param name            as the unique identifying name of the corresponding gameservice.
   * @param password        as the unique password of the corresponding gameservice.
   * @param preferredColour as the preferred colour with which this account is displayed.
   */
  public GameServiceAccountJson(String name, String password, String preferredColour) {
    super();
    this.name            = name;
    this.password        = password;
    this.preferredColour = preferredColour;
    this.role            = "ROLE_SERVICE";
  }

  /**
   * Creates a GameServiceAccountJson object.
   */
  public GameServiceAccountJson() {

  }

  private String name;
  private String password;
  private String preferredColour;
  private String role;

}
