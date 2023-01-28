package ca.mcgill.splendorclient.view.gameboard;

import javafx.scene.text.Text;

/**
 * Represents the counter corresponding to the total number of tokens
 * in a player's inventory.
 */
public class TotalTokenCountView extends Text {

  private static int totalTokenCount = 15;

  /**
   * Creates a TotalTokenCountView.
   *
   * @param startupText the initial text of the counter
   */
  public TotalTokenCountView(String startupText) {
    setText(startupText);
  }

  public void increment() {
    totalTokenCount++;
    String text = String.format("Total Token Count: %d/10", totalTokenCount);
    setText(text);
  }
  
  public void decrement() {
    totalTokenCount--;
    String text = String.format("Total Token Count: %d", totalTokenCount);
    setText(text);
  }
}
