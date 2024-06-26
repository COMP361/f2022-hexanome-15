package ca.mcgill.splendorserver.model.tokens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Represents a Splendor Token Pile with tokens and type.
 */
public class TokenPile implements Iterable<Token> {
  private final List<Token> tokens;
  private final TokenType        type;

  /**
   * Creates a TokenPile.
   *
   * @param tokenType The type of tokens that are in the token pile
   */
  public TokenPile(TokenType tokenType) {
    assert tokenType != null;
    this.type   = tokenType;
    this.tokens = new ArrayList<>();
  }

  /**
   * Removes tokens from this token pile.
   *
   * @param n the amount of tokens to be removed
   */
  public void removeTokens(int n) {
    assert n <= tokens.size();
    for (int i = 0; i < n; i++) {
      tokens.remove(0);
    }
  }

  /**
   * Sets up the TokenPile upon starting the game.
   *
   * @param numPlayers the number of players in the game
   */
  public void setUp(int numPlayers) {
    if (type == TokenType.GOLD) {
      for (int i = 0; i < 5; i++) {
        Token token = new Token(type);
        addToken(token);
      }
    } else {
      int numTokens = 0;
      switch (numPlayers) {
        case 3:
          numTokens = 5;
          break;
        case 4:
          numTokens = 7;
          break;
        default:
          numTokens = 4;
      }
      for (int i = 0; i < numTokens; i++) {
        Token token = new Token(getType());
        addToken(token);
      }
    }
  }

  /**
   * Adds a token to the token pile.
   *
   * @param token The token to be added
   */
  public void addToken(Token token) {
    assert token != null;
    if (token.getType() == type) {
      tokens.add(token);
    }
  }

  /**
   * Removes a token from the token pile. Assumes that the token pile is not empty, otherwise use
   * the optional version of this method.
   *
   * @return the token that is removed
   */
  public Token removeToken() {
    assert !tokens.isEmpty();
    return tokens.remove(0);
  }

  /**
   * Returns the type of the token in the token pile.
   *
   * @return the type of the token in the token pile
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

  @Override
  public Iterator<Token> iterator() {
    return tokens.iterator();
  }

}
