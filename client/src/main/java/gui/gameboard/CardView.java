package gui.gameboard;

import java.util.ArrayList;
import java.util.Optional;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Cards.Card;
import model.Cards.CardObservable;
import model.Cards.CardObserver;

public class CardView extends Rectangle implements CardObserver, CardObservable {
	
	private Optional<Card> card;
	
	private ArrayList<CardObserver> observers;
	
	public CardView(double height, double width) {
		super(height, width);
		card = Optional.ofNullable(null);
		observers = new ArrayList<CardObserver>();
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO onlick notify the deck associated with this row of cards and add a card and also add this card to the users inventory
				if (card.isPresent()) {
					notifyObservers(card.get());
					card = Optional.ofNullable(null);
					setFill(Color.WHITE);
				}
			}
		
		});
	}

	@Override
	public void onAction(Card card) {
		if (card == null) {
			this.setFill(Color.WHITE);
		}
		else if (this.card.isEmpty())
		{
			//when the card has an associated png, make that the fill 
			//https://stackoverflow.com/questions/22848829/how-do-i-add-an-image-inside-a-rectangle-or-a-circle-in-javafx
			this.card = Optional.of(card);
			this.setFill(card.getColor());
		}
	}

	@Override
	public void addListener(CardObserver observer) {
		observers.add(observer);
	}

	@Override
	public void removeListener(CardObserver observer) {
		observers.remove(observer);
	}
	
	public void notifyObservers(Card card) {
		for (CardObserver observer : observers) {
			observer.onAction(card);
		}
	}

}
