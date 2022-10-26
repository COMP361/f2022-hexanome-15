package model.Cards;

import java.util.ArrayList;

import gui.gameboard.CardView;
import javafx.scene.paint.Color;

public class Deck implements CardObservable {
	
	private ArrayList<Card> cards;
	private Color color;
	private ArrayList<CardView> cardViews;
	
	//in the future we'll automatically generate the cards and shuffle them, probably doing something functional. 
	public Deck(Color color) {
		this.color = color;
		cards = new ArrayList<Card>();
		cards.add(new Card(color));
		cards.add(new Card(color));
		cards.add(new Card(color));
		cardViews = new ArrayList<CardView>();
	}
	
	public int getSize() {
		return cards.size();
	}
	
	public Color getColor() {
		return color;
	}
	
	public void deal() {
		for (int i = 0; i < 3; ++i) {
			notifyObservers(cards.get(0));
			cards.remove(0);
		}
	}

	@Override
	public void addListener(CardView cardView) {
		cardViews.add(cardView);
	}

	@Override
	public void removeListener(CardView cardView) {
		//probably have to do something more sophisticated like an equals method.
		cardViews.remove(cardView);
	}
	
	public void notifyObservers(Card card) {
		for (CardView view : cardViews) {
			view.onAction(card);
		}
	}

}
