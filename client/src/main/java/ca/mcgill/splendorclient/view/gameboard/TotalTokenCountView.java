package ca.mcgill.splendorclient.view.gameboard;

import javafx.scene.text.Text;

/**
 * Represents the counter corresponding to the total number of tokens
 * in a player's inventory.
 */
public class TotalTokenCountView extends Text {

  private static int totalTokenCount = 0;

  /**
   * Creates a TotalTokenCountView.
   *
   * @param startupText the initial text of the counter
   */
  public TotalTokenCountView(String startupText) {
    setText(startupText);
  }

  /**
   * Increments the total token count and updates the label.
   */
  public void increment() {
    totalTokenCount++;
    String text = String.format("Total Token Count: %d/10", totalTokenCount);
    setText(text);
  }

  /**
   * Decrements the total token count and updates the label.
   */
  public void decrement() {
    totalTokenCount--;
    String text = String.format("Total Token Count: %d", totalTokenCount);
    setText(text);
  }
  /**
   * Sets the token count and updates the label.
   *
   * @param amount the amount of tokens to set
   */
  public void set(int amount) {
	  totalTokenCount = amount;
	  String text = String.format("Total Token Count: %d", totalTokenCount);
	  setText(text);
  }
}
