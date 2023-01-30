package ca.mcgill.splendorserver.model.userinventory;

import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.tokens.TokenPile;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents the inventory of a Splendor player.
 * Contains cards and token piles.
 * Observes CardView to assess whether a card is affordable.
 * Observed by CardColumnView to add the card to the inventory.
 * Observed by MoveManager to create the current move
 */
public class UserInventory {

  private ArrayList<Card> cards;
  private List<TokenPile> tokenPiles;
  private String playerName;

  /**
   * Initialize User Inventory Model.
   *
   * @param pile The token piles in a user's inventory
   * @param name This player's username
   */
  public UserInventory(List<TokenPile> pile, String name) {
    cards = new ArrayList<Card>();
    tokenPiles = List.copyOf(pile);
    playerName = name;
  }

  /**
   * Returns the username of the player who owns this inventory.
   *
   * @return this player's username
   */
  public String getPlayerName() {
    return playerName;
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

}
