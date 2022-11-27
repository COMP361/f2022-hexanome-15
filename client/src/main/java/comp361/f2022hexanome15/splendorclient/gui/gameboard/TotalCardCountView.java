package comp361.f2022hexanome15.splendorclient.gui.gameboard;

import comp361.f2022hexanome15.splendorclient.model.cards.Observer;
import javafx.scene.text.Text;

/**
 * Represents the counter corresponding to the total number of cards
 * in a player's inventory.
 */
public class TotalCardCountView extends Text implements Observer {

  private static int totalCardCount = 0;

  public TotalCardCountView(String startupText) {
    setText(startupText);
  }

  @Override
  public void onAction(boolean increment) {
    totalCardCount++;
    String text = String.format("Total Card Count: %d", totalCardCount);
    setText(text);
  }
}
