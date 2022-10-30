package gui.gameboard;

import java.util.ArrayList;
import java.util.Optional;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Cards.*;

public class CardView extends Rectangle implements CardObserver, CardObservable, DeckObservable {
	
	private Optional<Card> card;
	
	private ArrayList<CardObserver> cardObservers;
	private ArrayList<DeckObserver> deckObservers;
	
	public CardView(double height, double width) {
		super(height, width);
		this.setArcHeight(height/5);
		this.setArcWidth(height/5);
		card = Optional.ofNullable(null);
		cardObservers = new ArrayList<CardObserver>();
		deckObservers = new ArrayList<DeckObserver>();
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO onlick notify the deck associated with this row of cards and add a card and also add this card to the users inventory
				if (card.isPresent()) {
					notifyCardObservers(card.get());
					card = Optional.ofNullable(null);
					setFill(Color.WHITE);
					notifyDeckObservers();
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
		cardObservers.add(observer);
	}

	@Override
	public void removeListener(CardObserver observer) {
		cardObservers.remove(observer);
	}
	
	public void notifyCardObservers(Card card) {
		for (CardObserver observer : cardObservers) {
			observer.onAction(card);
		}
	}
	public void notifyDeckObservers() {
		for (DeckObserver observer : deckObservers) {
			observer.onAction();
		}
	}

	@Override
	public void addListener(DeckObserver observer) {deckObservers.add(observer);}

	@Override
	public void removeListener(DeckObserver observer) {deckObservers.remove(observer);}
}
