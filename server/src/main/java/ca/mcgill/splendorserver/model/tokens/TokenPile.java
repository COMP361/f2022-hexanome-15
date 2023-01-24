package ca.mcgill.splendorserver.model.tokens;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.mcgill.splendorserver.model.cards.Observable;
import ca.mcgill.splendorserver.model.cards.Observer;

/**
 * Represents a Splendor Token Pile with tokens and type.
 * Observes itself, so that grabbing tokens and buying cards messages
 * can be relayed between the board and the user inventory
 * Observed by TokenPileView to update the counter
 * Observed by TotalAssetCountView to update the counter
 */
public class TokenPile implements Iterable<Token>, Observable, Observer {
  private final ArrayList<Token> tokens;
  private final TokenType type;
  private List<Observer> observers;

  /**
   * Creates a TokenPile.
   *
   * @param type The type of tokens that are in the token pile
   */
  public TokenPile(TokenType type) {
    this.type = type;
    this.tokens = new ArrayList<>();
    observers = new ArrayList<Observer>();
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
      notifyObservers(true);
    }
  }

  /**
   * Removes a token from the token pile.
   *
   * @return the token that is removed
   */
  public Token removeToken() {
    if (!tokens.isEmpty()) {
      //i.e decrement the associated counter
      notifyObservers(false);
      Token token = tokens.remove(0);
      //notify the gameboard pile/ui pile depending on action.
      notifyObservers(token);
      return token;
    }
    return null;
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

  @Override
  public void addListener(Observer observer) {
    observers.add(observer);
  }

  public void removeListener(Observer observer) {
    observers.remove(observer);
  }

  private void notifyObservers(boolean increment) {
    for (Observer observer : observers) {
      observer.onAction(increment);
    }
  }

  private void notifyObservers(Token token) {
    for (Observer observer : observers) {
      observer.onAction(token);
    }
  }

  @Override
  public void onAction(Token token) {
    tokens.add(token);
    notifyObservers(true);
  }

}
