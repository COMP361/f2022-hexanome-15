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
  private final TotalTokenCountView tokenCountView;
  private final TotalCardCountView cardCountView;
  private final TotalPrestigeCountView prestigeCountView;
  private final ArrayList<Counter> listOfTokenCounters = new ArrayList<>();

  /**
   * Creates a UserInventoryView.
   *
   * @param playerName the name of the player with this user inventory
   */
  public UserInventoryView(String playerName, 
		  float cardSizeX, 
		  float cardSizeY, 
		  TotalTokenCountView tokenCountView,
		  TotalCardCountView cardCountView,
		  TotalPrestigeCountView prestigeCountView) {
    player = playerName;
    cardColumns = new ArrayList<>();
    sizeX = cardSizeX;
    sizeY = cardSizeY;
    this.tokenCountView = tokenCountView;
    this.cardCountView = cardCountView;
    this.prestigeCountView = prestigeCountView;
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
  public void updateCards(int[] cardIDs, int[] reservedcards) {
    cardColumns.get(0).getChildren().clear();
    cardColumns.get(1).getChildren().clear();
    cardColumns.get(2).getChildren().clear();
    cardColumns.get(3).getChildren().clear();
    float counter = 0.0f;
	  for (int card : cardIDs) {
		  CardView toAdd = new CardView(sizeX, sizeY);
		  toAdd.updateView(card);
		  cardColumns.get((int) (counter/4)).getChildren().add(toAdd);
		  toAdd.setTranslateY(20f* (counter%4));
		  counter++;
	  }
	  counter = 0;
	  for (int card : reservedcards) {
      CardView toAdd = new CardView(sizeX, sizeY);
      toAdd.updateViewReserved(card);
      cardColumns.get(3).getChildren().add(toAdd);
      toAdd.setTranslateY(40f* (counter%4));
      counter++;
    }
  }
  /**
   * Makes the inventory display the given cards.
   * @param cardIDs IDs of the cards in the player's possession
   */
  public void updateTokens(int numOfDiamonds,
		  int numOfSapphires,
		  int numOfEmeralds,
		  int numOfRubies,
		  int numOfOnyx,
		  int numOfGolds) {
	  tokenCountView.set(numOfDiamonds +
		  numOfSapphires +
		  numOfEmeralds +
		  numOfRubies +
		  numOfOnyx +
		  numOfGolds);
	  listOfTokenCounters.get(0).setCount(numOfDiamonds);
	  listOfTokenCounters.get(1).setCount(numOfSapphires);
	  listOfTokenCounters.get(2).setCount(numOfEmeralds);
	  listOfTokenCounters.get(3).setCount(numOfRubies);
	  listOfTokenCounters.get(4).setCount(numOfOnyx);
	  listOfTokenCounters.get(5).setCount(numOfGolds);
  }
  /**
   * Makes the inventory display the given cards.
   * @param cardIDs IDs of the cards in the player's possession
   */
  public void updatePrestige(int prestige) {
	  prestigeCountView.set(prestige);
  }
  public void addCounter(Counter c) {
	  listOfTokenCounters.add(c);
  }
}
