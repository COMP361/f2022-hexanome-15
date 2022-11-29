package ca.mcgill.splendorclient.gui.gameboard;

import ca.mcgill.splendorclient.model.cards.Card;
import ca.mcgill.splendorclient.model.cards.Observer;
import javafx.scene.text.Text;

/**
 * Represents the counter corresponding to a player's total prestige.
 */
public class TotalPrestigeCountView extends Text implements Observer {

  private static int totalPrestigeCount = 0;

  public TotalPrestigeCountView(String startupText) {
    setText(startupText);
  }

  @Override
  public void onAction(Card card) {
    totalPrestigeCount += card.getPrestige();
    String text = String.format("Total Prestige Count: %d", totalPrestigeCount);
    setText(text);
  }
}
