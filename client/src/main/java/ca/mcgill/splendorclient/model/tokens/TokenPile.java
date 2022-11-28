package ca.mcgill.splendorclient.model.tokens;

import java.util.ArrayList;

import ca.mcgill.splendorclient.model.ColorManager;
import javafx.scene.paint.Color;

/**
 * Represents a Splendor Token Pile with tokens and type.
 */
public class TokenPile {
  private final ArrayList<Token> tokens;
  private final TokenType type;

  private final Color color;

  /**
   * Creates a TokenPile.
   *
   * @param type The type of tokens that are in the token pile
   */
  public TokenPile(TokenType type) {
    this.type = type;
    this.tokens = new ArrayList<>();
    this.color = ColorManager.getColor(type);
  }

  /**
   * Adds a token to the token pile.
   *
   * @param token The token to be added
   */
  public void addToken(Token token) {
    if (token.getType() == type) {
      tokens.add(token);
    }
  }

  /**
   * Removes a token from the token pile.
   */
  public void removeToken() {
    if (!tokens.isEmpty()) {
      tokens.remove(0);
    }
  }

  /**
   * Returns the type of the token.
   *
   * @return the type of the token
   */
  public TokenType getType() {
    return type;
  }

  /**
   * Returns the number of tokens in the token pile.
   *
   * @return the size of the token pile
   */
  public int getSize() {
    return tokens.size();
  }

  // again this will return the image for fill

  /**
   * Returns the color of tokens in the token pile.
   *
   * @return the color of tokens in the token pile
   */
  public Color getColor() {
    return color;
  }

}
