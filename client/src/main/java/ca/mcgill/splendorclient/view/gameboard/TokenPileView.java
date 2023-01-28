package ca.mcgill.splendorclient.view.gameboard;


import ca.mcgill.splendorclient.control.ColorManager;
import ca.mcgill.splendorclient.model.TokenType;
import javafx.scene.shape.Circle;

/**
 * Represents the view of a token pile.
 * Observes token piles.
 */
public class TokenPileView extends Circle {

  private final Counter tokenCounter;

  /**
   * Creates a TokenPileView.
   *
   * @param radius the radius of the circle used to represent the token pile
   * @param type   the type of tokens in the token pile
   */
  public TokenPileView(float radius, TokenType type) {
    super(radius);
    this.setFill(ColorManager.getColor(type));
    tokenCounter = new Counter(0);
  }

  /**
   * Returns the token counter.
   *
   * @return the token counter
   */
  public Counter getCounter() {
    return tokenCounter;
  }
  
}
