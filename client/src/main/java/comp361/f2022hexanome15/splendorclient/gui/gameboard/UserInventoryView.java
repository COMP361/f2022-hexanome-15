package comp361.f2022hexanome15.splendorclient.gui.gameboard;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the view of the user's inventory.
 */
public class UserInventoryView implements Iterable<CardColumnView> {

  private final ArrayList<CardColumnView> handColumns;

  /**
   * Creates a HandView.
   */
  public UserInventoryView() {
    handColumns = new ArrayList<>();
  }

  @Override
  public Iterator<CardColumnView> iterator() {
    return handColumns.iterator();
  }

  /**
   * Adds a hand column to the hand view.
   *
   * @param handColumn the handColumn to be added
   */
  public void addCardColumn(CardColumnView handColumn) {
    handColumns.add(handColumn);
  }

}
