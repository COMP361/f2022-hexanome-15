package comp361.f2022hexanome15.splendorclient.model.userinventory;

import comp361.f2022hexanome15.splendorclient.gui.gameboard.CardView;
import comp361.f2022hexanome15.splendorclient.model.cards.Card;
import comp361.f2022hexanome15.splendorclient.model.cards.Observable;
import comp361.f2022hexanome15.splendorclient.model.cards.Observer;
import comp361.f2022hexanome15.splendorclient.model.tokens.TokenPile;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents the inventory of a Splendor player.
 * Contains cards and token piles.
 * Observes CardView to assess whether a card is affordable.
 * Observed by HandColumnView to add the card to the inventory.
 */
public class UserInventory implements Observer, Observable {

  private ArrayList<Card> cards;
  private ArrayList<Observer> observers;
  private List<TokenPile> tokenPiles;
  private int playerNumber;

  /**
   * Initialize User Inventory Model.
   *
   * @param pile The token piles in a user's inventory
   * @param number The player number of this user
   */
  public UserInventory(List<TokenPile> pile, int number) {
    cards = new ArrayList<Card>();
    tokenPiles = List.copyOf(pile);
    observers = new ArrayList<Observer>();
    playerNumber = number;
  }

  public int getPlayerNumber() {
    return playerNumber;
  }

  @Override
  public void onAction(CardView cardView) {
    boolean affordable = true;
    for (int i = 0; i < cardView.getCard().get().getCost().length; i++) {
      for (TokenPile tokenPile : tokenPiles) {
        if (tokenPile.getType().ordinal() == i) {
          if (cardView.getCard().get().getCost()[i] > 0
                && tokenPile.getSize() < cardView.getCard().get().getCost()[i]) {
            affordable = false;
          }
        }
      }
    }
    if (affordable) {
      notifyObservers(cardView.getCard().get());
      cards.add(cardView.getCard().get());
      for (int i = 0; i < cardView.getCard().get().getCost().length; i++) {
        for (TokenPile tokenPile : tokenPiles) {
          if (tokenPile.getType().ordinal() == i) {
            if (cardView.getCard().get().getCost()[i] > 0
                  && tokenPile.getSize() >= cardView.getCard().get().getCost()[i]) {
              for (int j = 0; j < cardView.getCard().get().getCost()[i]; j++) {
                tokenPile.removeToken();
              }
            }
          }
        }
      }
    } else {
      cardView.revokePurchaseAttempt();
    }
  }

  //need tighter encapsulation eventually
  public void addPile(TokenPile pile) {
    tokenPiles.add(pile);
  }

  @Override
  public void addListener(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeListener(Observer observer) {
    observers.remove(observer);
  }

  private void notifyObservers(Card card) {
    for (Observer observer : observers) {
      observer.onAction(card);
    }
  }

}
