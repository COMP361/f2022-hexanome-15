package ca.mcgill.splendorclient.gui.gameboard;

import ca.mcgill.splendorclient.model.cards.Observer;
import javafx.scene.text.Text;

/**
 * Represents the counter corresponding to the total number of cards
 * in a player's inventory.
 */
public class TotalCardCountView extends Text implements Observer {

  private static int totalCardCount = 0;

  /**
   * Creates a TotalCardCountView.
   *
   * @param startupText the initial text of the counter
   */
  public TotalCardCountView(String startupText) {
    setText(startupText);
  }

  @Override
  public void onAction(boolean increment) {
    totalCardCount++;
    String text = String.format("Total Card Count: %d", totalCardCount);
    setText(text);
  }
}
