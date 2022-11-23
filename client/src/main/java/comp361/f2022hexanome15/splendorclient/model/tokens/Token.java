package comp361.f2022hexanome15.splendorclient.model.tokens;

import comp361.f2022hexanome15.splendorclient.model.ColorManager;
import javafx.scene.paint.Color;

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
  private final Color color;

  /**
   * Creates a Token.
   *
   * @param type The type of token
   */
  public Token(TokenType type) {
    this.type = type;
    this.color = ColorManager.getColor(type);
  }

  /**
   * Returns the color of the token.
   *
   * @return the color of the token
   */
  public Color getColor() {
    return color;
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
