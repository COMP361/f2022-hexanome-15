package ca.mcgill.splendorserver.model.userinventory;

import ca.mcgill.splendorserver.games.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Represents the inventory of a Splendor player.
 * Contains cards and token piles.
 * Observes CardView to assess whether a card is affordable.
 * Observed by CardColumnView to add the card to the inventory.
 * Observed by MoveManagerDepr to create the current move
 */
public class UserInventory implements Iterable<Card> {

  private final List<Card> cards;
  private final List<TokenPile> tokenPiles;
  private final PlayerWrapper playerWrapper;

  /**
   * Initialize User Inventory Model.
   *
   * @param pile The token piles in a user's inventory
   * @param name This player's username
   */
  public UserInventory(List<TokenPile> pile, PlayerWrapper name) {
    cards = new ArrayList<>();
    tokenPiles = List.copyOf(pile);
    playerWrapper = name;
  }

  /**
   * Gets the number of cards in inventory.
   *
   * @return the number of cards in this inventory.
   */
  public int cardCount() {
    return cards.size();
  }

  /**
   * If the inventory has cards.
   *
   * @return if they have cards or not.
   */
  public boolean hasCards() {
    return !cards.isEmpty();
  }

  public boolean canAffordCard(Card card) {
    for (Map.Entry<TokenType, Integer> entry : card.getCardCost().entrySet()) {
      if (!hasEnoughFor(entry.getKey(), entry.getValue())) {
        return false;
      }
    }
    return true;
  }

  private int getGoldTokenCount() {
    for (TokenPile tokenPile : tokenPiles) {
      if (tokenPile.getType() == TokenType.GOLD) {
        return tokenPile.getSize();
      }
    }
    return 0;
  }

  private boolean hasEnoughFor(TokenType tokenType, int cost) {
    int goldTokenCount = getGoldTokenCount();
    for (TokenPile tokenPile : tokenPiles) {
      if (tokenPile.getType() == tokenType) {
        // if the amount of that token possessed plus any gold tokens if sufficient then true
        return tokenPile.getSize() + goldTokenCount >= cost;
      } else {
        break;
      }
    }
    return false;
  }

  //TODO: do we check if the card is already in the hand or are the cards all unique???

  /**
   * Adds card to user inventory.
   *
   * @param card card to add.
   * @throws AssertionError if card == null
   */
  public void addCard(Card card) {
    assert card != null;
    cards.add(card);
    Collections.sort(cards); // sorts the cards by color as described in rules
  }

  /**
   * Number of tokens of any type in this inventory.
   *
   * @return total number of tokens in user inventory.
   */
  public int tokenCount() {
    int count = 0;
    for (TokenPile tokenPile : tokenPiles) {
      count += tokenPile.getSize();
    }
    return count;
  }

  /**
   * Returns the username of the player who owns this inventory.
   *
   * @return this player.
   */
  public PlayerWrapper getPlayer() {
    return playerWrapper;
  }

//  @Override
//  public void onAction(CardView cardView) {
//    boolean affordable = true;
//    for (int i = 0; i < cardView.getCard().get().getCost().length; i++) {
//      for (TokenPile tokenPile : tokenPiles) {
//        if (tokenPile.getType().ordinal() == i) {
//          if (cardView.getCard().get().getCost()[i] > 0
//                && tokenPile.getSize() < cardView.getCard().get().getCost()[i]) {
//            affordable = false;
//          }
//        }
//      }
//    }
//    if (affordable) {
//      notifyObservers(cardView.getCard().get());
//      cards.add(cardView.getCard().get());
//      for (int i = 0; i < cardView.getCard().get().getCost().length; i++) {
//        for (TokenPile tokenPile : tokenPiles) {
//          if (tokenPile.getType().ordinal() == i) {
//            if (cardView.getCard().get().getCost()[i] > 0
//                  && tokenPile.getSize() >= cardView.getCard().get().getCost()[i]) {
//              for (int j = 0; j < cardView.getCard().get().getCost()[i]; j++) {
//                tokenPile.removeToken();
//              }
//            }
//          }
//        }
//      }
//    } else {
//      cardView.revokePurchaseAttempt();
//    }
//  }


  /**
   * Adds a token pile to the user inventory.
   *
   * @param pile the pile to be added
   */
  public void addPile(TokenPile pile) {
    tokenPiles.add(pile);
  }

  @Override
  public Iterator<Card> iterator() {
    return cards.iterator();
  }
}
