package ca.mcgill.splendorserver.model.tokens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;

/**
 * Represents a Splendor Token Pile with tokens and type.
 */
public class TokenPile implements Iterable<Token> {
  private final ArrayList<Token> tokens;
  private final TokenType        type;

  /**
   * Creates a TokenPile.
   *
   * @param tokenType The type of tokens that are in the token pile
   */
  public TokenPile(TokenType tokenType) {
    this.type   = tokenType;
    this.tokens = new ArrayList<>();
  }

  public void removeTokens(int n) {
    if (n > tokens.size()) {
      n = tokens.size();
    }
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
    int numTokens = 0;
    switch (numPlayers) {
      case 2:
        numTokens = 4;
        break;
      case 3:
        numTokens = 5;
        break;
      case 4:
        numTokens = 7;
        break;
      default:
        numTokens = 4;
    }
    if (getType().equals(TokenType.GOLD)) {
      for (int i = 0; i < 5; i++) {
        Token token = new Token(getType());
        addToken(token);
      }
    } else {
      for (int i = 0; i < numTokens; i++) {
        Token token = new Token(getType());
        addToken(token);
      }
    }
  }

  /**
   * Sets up the TokenPile for Player Inventory. Only for demo.
   */
  public void setUpDemo() {
    if (!getType().equals(TokenType.GOLD)) {
      for (int i = 0; i < 3; i++) {
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

  public Optional<Token> removeTokenOpt() {
    if (tokens.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(tokens.remove(0));
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

  @Override
  public Iterator<Token> iterator() {
    return tokens.iterator();
  }

}
