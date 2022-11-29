package ca.mcgill.splendorclient.gui.gameboard;


import ca.mcgill.splendorclient.model.ColorManager;
import ca.mcgill.splendorclient.model.cards.Observer;
import ca.mcgill.splendorclient.model.tokens.TokenType;
import javafx.scene.shape.Circle;

/**
 * Represents the view of a token pile.
 * Observes token piles.
 */
public class TokenPileView extends Circle implements Observer {

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

  public Counter getCounter() {
    return tokenCounter;
  }

  @Override
  public void onAction(boolean increment) {
    if (increment) {
      tokenCounter.setText(String.valueOf(tokenCounter.getCount() + 1));
      String.format("Total tokens: %d", tokenCounter.getCount());
    } else {
      tokenCounter.setText(String.valueOf(tokenCounter.getCount() - 1));
    }
  }
}
