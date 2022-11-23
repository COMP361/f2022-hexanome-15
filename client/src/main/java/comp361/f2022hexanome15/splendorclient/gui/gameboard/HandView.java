package comp361.f2022hexanome15.splendorclient.gui.gameboard;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the view of the user's inventory.
 */
public class HandView implements Iterable<HandColumnView> {

  private final ArrayList<HandColumnView> handColumns;

  /**
   * Creates a HandView.
   */
  public HandView() {
    handColumns = new ArrayList<>();
  }

  @Override
  public Iterator<HandColumnView> iterator() {
    return handColumns.iterator();
  }

  /**
   * Adds a hand column to the hand view.
   *
   * @param handColumn the handColumn to be added
   */
  public void addHandColumn(HandColumnView handColumn) {
    handColumns.add(handColumn);
  }

}
