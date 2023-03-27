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

  /**
   * Sets the total card count and updates the label.
   *
   * @param amount the amount of cards to set
   */
  public void set(int amount) {
    totalCardCount = amount;
    String text = String.format("Total Purchased Card Count: %d", totalCardCount);
    setText(text);
  }
}
