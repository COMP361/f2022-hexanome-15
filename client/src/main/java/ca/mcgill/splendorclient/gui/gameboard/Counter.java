package ca.mcgill.splendorclient.gui.gameboard;

import javafx.scene.control.Label;

/**
 * Represents a label used to count the number of cards in a deck
 * or the number of tokens in a token pile.
 */
public class Counter extends Label {
  /**
   * Creates a counter label.
   *
   * @param count the value of the counter
   */
  public Counter(int count) {
    this.setText(String.valueOf(count));
  }

  /**
   * Returns the integer value of the counter.
   *
   * @return the integer value of the counter
   */
  public int getCount() {
    return Integer.valueOf(this.getText());
  }

}
