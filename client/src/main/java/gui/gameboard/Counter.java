package gui.gameboard;

import javafx.scene.control.Label;

/**
 * Represents a label used to count the number of cards in a deck
 * or the number of tokens in a token pile.
 */
public class Counter extends Label {

  public Counter(int count) {
    this.setText(String.valueOf(count));
  }

}
