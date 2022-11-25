package comp361.f2022hexanome15.splendorclient.model.userinventory;

import java.util.ArrayList;
import java.util.List;

import comp361.f2022hexanome15.splendorclient.model.cards.Card;
import comp361.f2022hexanome15.splendorclient.model.cards.Observable;
import comp361.f2022hexanome15.splendorclient.model.cards.Observer;
import comp361.f2022hexanome15.splendorclient.model.tokens.TokenPile;


/**
 * Represents the inventory of a Splendor player.
 * Contains cards and token piles.
 * Observes CardView to assess whether a card is affordable
 * Observed by HandColumnView to add the card to the inventory
 */
public class UserInventory implements Observer, Observable {

  ArrayList<Card> cards;
  ArrayList<Observer> observers;
  List<TokenPile> tokenPiles;
  
  public UserInventory(List<TokenPile> pile) {
	  cards = new ArrayList<Card>();
	  tokenPiles = List.copyOf(pile);
	  observers = new ArrayList<Observer>();
  }

	@Override
	public void onAction(Card card) {
		boolean affordable = true;
		for (int i = 0; i < card.getCost().length; i++) {
			for (TokenPile tokenPile : tokenPiles) {
				if (tokenPile.getType().ordinal() == i) {
					if (card.getCost()[i] > 0 && tokenPile.getSize() < card.getCost()[i]) {
						affordable = false;
					}
				}
			}
		}
		if (affordable) {
			notifyObservers(card);
			for (int i = 0; i < card.getCost().length; i++) {
				for (TokenPile tokenPile : tokenPiles) {
					if (tokenPile.getType().ordinal() == i) {
						if (card.getCost()[i] > 0 && tokenPile.getSize() >= card.getCost()[i]) {
							for (int j = 0; j < card.getCost()[i]; j++) {
								tokenPile.removeToken();
							}
						}
					}
				}
			}
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
