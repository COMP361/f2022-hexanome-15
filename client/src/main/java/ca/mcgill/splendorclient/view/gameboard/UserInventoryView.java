package ca.mcgill.splendorclient.view.gameboard;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the view of the user's inventory.
 */
public class UserInventoryView implements Iterable<CardColumnView> {

  private final ArrayList<CardColumnView> cardColumns;
  private final String player;
  private final float sizeX;
  private final float sizeY;

  /**
   * Creates a UserInventoryView.
   *
   * @param playerName the name of the player with this user inventory
   */
  public UserInventoryView(String playerName, float cardSizeX, float cardSizeY) {
    player = playerName;
    cardColumns = new ArrayList<>();
    sizeX = cardSizeX;
    sizeY = cardSizeY;
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
  
  /**
   * Makes the inventory display the given cards.
   * @param cardIDs IDs of the cards in the player's possession
   */
  public void updateCards(int[] cardIDs) {
	  cardColumns.get(0).getChildren().clear();
	  for (int card : cardIDs) {
		  CardView toAdd = new CardView(sizeX, sizeY);
		  toAdd.updateView(card);
		  cardColumns.get(0).getChildren().add(toAdd);
	  }
  }
}
