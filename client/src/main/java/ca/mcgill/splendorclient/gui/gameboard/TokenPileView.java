package ca.mcgill.splendorclient.gui.gameboard;

import ca.mcgill.splendorclient.model.ColorManager;
import ca.mcgill.splendorclient.model.cards.Card;
import ca.mcgill.splendorclient.model.cards.Observer;
import ca.mcgill.splendorclient.model.tokens.Token;
import ca.mcgill.splendorclient.model.tokens.TokenPile;
import ca.mcgill.splendorclient.model.tokens.TokenType;
import javafx.scene.shape.Circle;

/**
 * Represents the view of a token pile.
 */
public class TokenPileView extends Circle implements Observer {

  private final Counter tokenCounter;
  private final TokenPile tokenPile;

  /**
   * Creates a TokenPileView.
   *
   * @param radius    the radius of the circle used to represent the token pile
   * @param tokenPile the token pile that is represented by this token pile view
   */
  public TokenPileView(float radius, TokenPile tokenPile) {
    super(radius);
    // also make this the nice img made by ojas.
    this.setFill(ColorManager.getColor(tokenPile.getType()));
    tokenCounter = new Counter(tokenPile.getSize());
    this.tokenPile = tokenPile;
  }

  public Counter getCounter() {
    return tokenCounter;
  }

  @Override
  public void onAction(Card card) {
    for (int i = 0; i < card.getCost().length; i++) {
      if (tokenPile.getType().ordinal() == card.getCost()[i]) {
        if (card.getCost()[i] > 0 && tokenPile.getSize() > card.getCost()[i]) {
          for (int j = 0; j < card.getCost()[i]; j++) {
            tokenPile.removeToken();
          }
        }
      }
    }
    tokenCounter.setText(Integer.toString(tokenPile.getSize()));
  }

  /**
   * Sets up the TokenPileView upon starting the game.
   */
  public void setUp() {
    if (tokenPile.getType().equals(TokenType.GOLD)) {
      for (int i = 0; i < 5; i++) {
        Token token = new Token(tokenPile.getType());
        tokenPile.addToken(token);
      }
    } else {
      for (int i = 0; i < 7; i++) {
        Token token = new Token(tokenPile.getType());
        tokenPile.addToken(token);
      }
    }
    tokenCounter.setText(Integer.toString(tokenPile.getSize()));
  }

  /**
   * Sets up the TokenPileView for Player Inventory. Only for demo.
   */
  public void setUpDemo() {
    if (!tokenPile.getType().equals(TokenType.GOLD)) {
      for (int i = 0; i < 3; i++) {
        Token token = new Token(tokenPile.getType());
        tokenPile.addToken(token);
      }
      tokenCounter.setText(Integer.toString(tokenPile.getSize()));
    }
  }
}
