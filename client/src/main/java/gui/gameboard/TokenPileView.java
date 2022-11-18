package gui.gameboard;

import javafx.scene.shape.Circle;
import model.ColorManager;
import model.tokens.TokenPile;

/**
 * Represents the view of a token pile.
 */
public class TokenPileView extends Circle {

  private final Counter tokenCounter;

  /**
   * Creates a TokenPileView.
   *
   * @param radius the radius of the circle used to represent the token pile
   * @param tokenPile the token pile that is represented by this token pile view
   */
  public TokenPileView(float radius, TokenPile tokenPile) {
    super(radius);
    //also make this the nice img made by ojas.
    this.setFill(ColorManager.getColor(tokenPile.getType()));
    tokenCounter = new Counter(tokenPile.getSize());
  }

  public Counter getCounter() {
    return tokenCounter;
  }
}
