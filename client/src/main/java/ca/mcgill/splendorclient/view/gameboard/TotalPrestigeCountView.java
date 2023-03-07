package ca.mcgill.splendorclient.view.gameboard;

import javafx.scene.text.Text;

/**
 * Represents the counter corresponding to a player's total prestige.
 */
public class TotalPrestigeCountView extends Text {

  private static int totalPrestigeCount = 0;

  /**
   * Creates a TotalPrestigeCountView.
   *
   * @param startupText the initial text of the counter
   */
  public TotalPrestigeCountView(String startupText) {
    setText(startupText);
  }

  /**
   * Increments the prestige count and updates the label.
   *
   * @param amount the amount of prestige to add
   */
  public void increment(int amount) {
    totalPrestigeCount += amount;
    String text = String.format("Total Prestige Count: %d", totalPrestigeCount);
    setText(text);
  }
  /**
   * Sets the prestige count and updates the label.
   *
   * @param amount the amount of prestige to set
   */
  public void set(int amount) {
	  totalPrestigeCount = amount;
	  String text = String.format("Total Prestige Count: %d", totalPrestigeCount);
	  setText(text);
  }
}
