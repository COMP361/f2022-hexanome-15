package comp361.f2022hexanome15.splendorclient.gui.gameboard;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the view of the user's inventory.
 */
public class UserInventoryView implements Iterable<CardColumnView> {

  private final ArrayList<CardColumnView> cardColumns;

  /**
   * Creates a UserInventoryView.
   */
  public UserInventoryView() {
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

}
