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

  public void increment(int amount) {
    totalPrestigeCount += amount;
    String text = String.format("Total Prestige Count: %d", totalPrestigeCount);
    setText(text);
  }
}
