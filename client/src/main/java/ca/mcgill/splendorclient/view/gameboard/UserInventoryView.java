package ca.mcgill.splendorclient.view.gameboard;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the view of the user's inventory.
 */
public class UserInventoryView implements Iterable<CardColumnView> {

  private final ArrayList<CardColumnView> cardColumns;
  private final String player;

  /**
   * Creates a UserInventoryView.
   *
   * @param playerName the name of the player with this user inventory
   */
  public UserInventoryView(String playerName) {
    player = playerName;
    cardColumns = new ArrayList<>();
  }

  @Override
  public Iterator<CardColumnView> iterator() {
    return cardColumns.iterator();
  }

  /**
   * Adds a card column to the user inventory view.
   *
   * @param handColumn the handColumn to be added
   */
  public void addCardColumn(CardColumnView handColumn) {
    cardColumns.add(handColumn);
  }

  /**
   * Returns the username of the player with this user inventory.
   *
   * @return the username of the player with this user inventory
   */
  public String getAssociatedPlayerName() {

    return player;
  }

}
