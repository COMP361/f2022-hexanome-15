package ca.mcgill.splendorclient.view.gameboard;

import javafx.scene.text.Text;

/**
 * Represents the counter corresponding to the total number of cards
 * in a player's inventory.
 */
public class TotalCardCountView extends Text {

  private static int totalCardCount = 0;

  /**
   * Creates a TotalCardCountView.
   *
   * @param startupText the initial text of the counter
   */
  public TotalCardCountView(String startupText) {
    setText(startupText);
  }

  public void increment() {
    totalCardCount++;
    String text = String.format("Total Card Count: %d", totalCardCount);
    setText(text);
  }
  
  public void decrement() {
    --totalCardCount;
    String text = String.format("Total Card Count: %d", totalCardCount);
    setText(text);
  }
}
