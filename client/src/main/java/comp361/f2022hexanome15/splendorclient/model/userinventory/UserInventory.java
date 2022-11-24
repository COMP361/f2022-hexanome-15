package comp361.f2022hexanome15.splendorclient.model.userinventory;

import java.util.ArrayList;

import comp361.f2022hexanome15.splendorclient.model.cards.Card;
import comp361.f2022hexanome15.splendorclient.model.cards.Observer;
import comp361.f2022hexanome15.splendorclient.model.tokens.TokenPile;


/**
 * Represents the inventory of a Splendor player.
 * Contains cards and token piles.
 */
public class UserInventory implements Observer {

  ArrayList<Card> cards;

  ArrayList<TokenPile> tokenPiles;
  
  public UserInventory() {
	  cards = new ArrayList<Card>();
	  tokenPiles = new ArrayList<TokenPile>();
  }

	@Override
	public void onAction(Card card) {
		boolean affordable = true;
		for (int i = 0; i < card.getCost().length; i++) {
			for (TokenPile tokenPile : tokenPiles) {
				if (tokenPile.getType().ordinal() == card.getCost()[i]) {
					if (card.getCost()[i] > 0 && tokenPile.getSize() < card.getCost()[i]) {
						affordable = false;
					}
				}
			}
		}
		if (affordable) {
			for (int i = 0; i < card.getCost().length; i++) {
				for (TokenPile tokenPile : tokenPiles) {
					if (tokenPile.getType().ordinal() == card.getCost()[i]) {
						if (card.getCost()[i] > 0 && tokenPile.getSize() > card.getCost()[i]) {
							for (int j = 0; j < card.getCost()[i]; j++) {
								tokenPile.removeToken();
								//notify the user inventory token pile view
							}
						}
					}
				}
			}
		}
	}
	
	//setup method after adding all the tokens, send the count to the token pile view
	public void setUp() {
		for (TokenPile pile : tokenPiles) {
			pile.setUp();
		}
	}
	
	public void setUpDemo() {
		for (TokenPile pile : tokenPiles) {
			pile.setUpDemo();
		}
	}
	
	//need tighter encapsulation eventually
	public void addPile(TokenPile pile) {
		tokenPiles.add(pile);
	}

}
