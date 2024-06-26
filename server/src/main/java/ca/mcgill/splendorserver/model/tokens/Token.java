package ca.mcgill.splendorserver.model.tokens;

/**
 * Represents a Splendor Token with type.
 */
public class Token {

  private final TokenType type;

  // again eventually using the images ojas provided,
  // also this hack is very brittle and bad, but it's what I've got for now.
  /*
   * public static Color[] typeToColor = new Color[] {Color.GREEN,
   * Color.LIGHTBLUE, Color.BLUE, Color.DARKGREY, Color.MAGENTA, Color.GOLD};
   */

  /**
   * Creates a Token.
   *
   * @param type The type of token
   */
  public Token(TokenType type) {
    assert type != null;
    this.type = type;
  }

  /**
   * Returns the type of the token.
   *
   * @return the type of the token
   */
  public TokenType getType() {
    return type;
  }

}
