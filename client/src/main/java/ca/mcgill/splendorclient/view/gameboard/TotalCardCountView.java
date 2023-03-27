package ca.mcgill.splendorclient.view.gameboard;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
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
    this.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
  }

  /**
   * Increments the total card count and updates the label.
   */
  public void increment() {
    totalCardCount++;
    String text = String.format("Total Card Count: %d", totalCardCount);
    setText(text);
    this.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
  }

  /**
   * Decrements the total card count and updates the label.
   */
  public void decrement() {
    --totalCardCount;
    String text = String.format("Total Card Count: %d", totalCardCount);
    setText(text);
    this.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
  }
}
