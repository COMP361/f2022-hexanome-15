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
  private final ArrayList<Counter> listOfCardCounters = new ArrayList<>();

  /**
   * Creates a UserInventoryView.
   *
   * @param playerName the name of the player with this user inventory
   * @param cardSizeX the width of the cards
   * @param cardSizeY the length of the cards
   * @param tokenCountView the total token counter
   * @param cardCountView the total card counter
   * @param prestigeCountView the total prestige counter
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
   *
   * @param cardids IDs of the cards in the player's possession
   * @param reservedcards IDs of the reserved cards in the player's possession
   */
  public void updateCards(int[] cardids, int[] reservedcards) {
    cardColumns.get(0).getChildren().clear();
    cardColumns.get(1).getChildren().clear();
    cardColumns.get(2).getChildren().clear();
    cardColumns.get(3).getChildren().clear();
    float counter = 0.0f;
    for (int card : cardids) {
      CardView toAdd = new CardView(sizeX, sizeY);
      toAdd.updateView(card);
      cardColumns.get((int) counter % 4).getChildren().add(toAdd);
      toAdd.setTranslateY(30f * ((int) (counter / 4)));
      counter++;
    }
    counter = 0;
    for (int card : reservedcards) {
      CardView toAdd = new CardView(sizeX, sizeY);
      toAdd.updateViewReserved(card);
      cardColumns.get(3).getChildren().add(toAdd);
      toAdd.setTranslateY(30f * (counter));
      counter++;
    }
  }

  /**
   * Updates the token piles in the user inventory.
   *
   * @param numOfDiamonds the number of diamond tokens in the user inventory
   * @param numOfSapphires the number of sapphire tokens in the user inventory
   * @param numOfEmeralds the number of emerald tokens in the user inventory
   * @param numOfRubies the number of ruby tokens in the user inventory
   * @param numOfOnyx the number of onyx tokens in the user inventory
   * @param numOfGolds the number of gold tokens in the user inventory
   */
  public void updateTokens(int numOfDiamonds,
                           int numOfSapphires,
                           int numOfEmeralds,
                           int numOfRubies,
                           int numOfOnyx,
                           int numOfGolds) {
    tokenCountView.set(numOfDiamonds
                         + numOfSapphires
                         + numOfEmeralds
                         + numOfRubies
                         + numOfOnyx
                         + numOfGolds);
    listOfTokenCounters.get(0).setCount(numOfDiamonds);
    listOfTokenCounters.get(1).setCount(numOfSapphires);
    listOfTokenCounters.get(2).setCount(numOfEmeralds);
    listOfTokenCounters.get(3).setCount(numOfRubies);
    listOfTokenCounters.get(4).setCount(numOfOnyx);
    listOfTokenCounters.get(5).setCount(numOfGolds);
  }

  /**
   * Updates the mini card labels in the user inventory.
   *
   * @param numWhiteCards the number of white cards in the user inventory
   * @param numBlueCards the number of blue cards in the user inventory
   * @param numGreenCards the number of green cards in the user inventory
   * @param numRedCards the number of red cards in the user inventory
   * @param numBlackCards the number of black cards in the user inventory
   * @param numGoldCards the number of gold cards in the user inventory
   */
  public void updateMiniCards(int numWhiteCards,
                           int numBlueCards,
                           int numGreenCards,
                           int numRedCards,
                           int numBlackCards,
                           int numGoldCards) {
    listOfCardCounters.get(0).setCount(numWhiteCards);
    listOfCardCounters.get(1).setCount(numBlueCards);
    listOfCardCounters.get(2).setCount(numGreenCards);
    listOfCardCounters.get(3).setCount(numRedCards);
    listOfCardCounters.get(4).setCount(numBlackCards);
    listOfCardCounters.get(5).setCount(numGoldCards);
  }

  /**
   * Updates the prestige counter in the user inventory.
   *
   * @param prestige the prestige in the user inventory
   */
  public void updatePrestige(int prestige) {
    prestigeCountView.set(prestige);
  }

  /**
   * Updates the total purchased card counter in the user inventory.
   *
   * @param purchasedCardCount the number of purchased cards in the user inventory
   */
  public void updateCardCount(int purchasedCardCount) {
    cardCountView.set(purchasedCardCount);
  }

  /**
   * Adds a token counter to the user inventory.
   *
   * @param c the counter to be added
   */
  public void addTokenCounter(Counter c) {
    listOfTokenCounters.add(c);
  }

  /**
   * Adds a card counter to the user inventory.
   *
   * @param c the counter to be added
   */
  public void addCardCounter(Counter c) {
    listOfCardCounters.add(c);
  }
}
