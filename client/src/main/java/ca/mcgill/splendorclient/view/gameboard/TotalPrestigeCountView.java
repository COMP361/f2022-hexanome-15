package ca.mcgill.splendorclient.view.gameboard;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
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
    this.setFont(Font.font("Comic Sans MS", 
        FontWeight.BOLD, 
        FontPosture.REGULAR, 
        GameBoardView.getFontSize() / 2));
  }

  /**
   * Sets the prestige count and updates the label.
   *
   * @param amount the amount of prestige to set
   */
  public void set(int amount) {
    totalPrestigeCount = amount;
    String text = String.format("Prestige: %d", totalPrestigeCount);
    setText(text);
    this.setFont(Font.font("Comic Sans MS", 
        FontWeight.BOLD, 
        FontPosture.REGULAR, 
        GameBoardView.getFontSize() / 2));
  }
}
