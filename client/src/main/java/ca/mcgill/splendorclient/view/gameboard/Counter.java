package ca.mcgill.splendorclient.view.gameboard;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Represents a label used to count the number of cards in a deck
 * or the number of tokens in a token pile.
 */
public class Counter extends Text {
  /**
   * Creates a counter label.
   *
   * @param count the value of the counter
   */
  public Counter(int count) {
    this.setText(String.valueOf(count));
    this.setFont(Font.font("Comic Sans MS",
        FontWeight.BOLD, 
        FontPosture.REGULAR, 
        GameBoardView.getFontSize() / 2));
  }

  /**
   * Returns the integer value of the counter.
   *
   * @return the integer value of the counter
   */
  public int getCount() {
    return Integer.valueOf(this.getText());
  }

  /**
   * Sets the integer value of the counter to the given integer.
   *
   * @param count the given integer
   */
  public void setCount(int count) {

    this.setText(String.valueOf(count));
    this.setFont(Font.font("Comic Sans MS", 
        FontWeight.BOLD, 
        FontPosture.REGULAR, 
        GameBoardView.getFontSize() / 2));
  }

}
