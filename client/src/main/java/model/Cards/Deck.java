package model.Cards;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import model.Tokens.TokenType;

public class Deck implements CardObservable {
	
	private ArrayList<Card> cards;
	private Color color;
	private ArrayList<CardObserver> cardObservers;
	
	//in the future we'll automatically generate the cards and shuffle them, probably doing something functional. 
	public Deck(Color color) {
		this.color = color;
		cards = new ArrayList<Card>();
		cards.add(new Card(color, TokenType.EMERALD));
		cards.add(new Card(color, TokenType.DIAMOND));
		cards.add(new Card(color, TokenType.LAPIS));
		cards.add(new Card(color, TokenType.ONYX));
		cards.add(new Card(color, TokenType.SAPHIRE));
		cardObservers = new ArrayList<CardObserver>();
	}
	
	public int getSize() {
		return cards.size();
	}
	
	public Color getColor() {
		return color;
	}
	
	public void deal() {
		for (int i = 0; i < 4; ++i) {
			notifyObservers(cards.get(0), i);
			cards.remove(0);
		}
	}

	@Override
	public void addListener(CardObserver cardView) {
		cardObservers.add(cardView);
	}

	@Override
	public void removeListener(CardObserver cardView) {
		//probably have to do something more sophisticated like an equals method.
		cardObservers.remove(cardView);
	}
	
	//a terrible hack to work around instantiating each card view for the first time
	public void notifyObservers(Card card, int observerIndex) {
		//this is a terrible hack, needs a re-design.
		cardObservers.get(observerIndex).onAction(card);
	}
	
	//use this one to deal out a new card after one got purchased
	public void notifyObservers(Card card) {
		for (CardObserver observer : cardObservers) {
			observer.onAction(card);
		}
	}

}
