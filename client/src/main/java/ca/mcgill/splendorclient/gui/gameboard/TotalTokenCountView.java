package ca.mcgill.splendorclient.gui.gameboard;

import ca.mcgill.splendorclient.model.cards.Observer;
import javafx.scene.text.Text;

/**
 * Represents the counter corresponding to the total number of tokens
 * in a player's inventory.
 */
public class TotalTokenCountView extends Text implements Observer {

  private static int totalTokenCount = 15;

  /**
   * Creates a TotalTokenCountView.
   *
   * @param startupText the initial text of the counter
   */
  public TotalTokenCountView(String startupText) {
    setText(startupText);
  }

  @Override
  public void onAction(boolean increment) {
    if (increment) {
      totalTokenCount++;
      String text = String.format("Total Token Count: %d/10", totalTokenCount);
      setText(text);
    } else {
      totalTokenCount--;
      String text = String.format("Total Token Count: %d", totalTokenCount);
      setText(text);
    }
  }
}
