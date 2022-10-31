package gui.gameboard;

import java.util.ArrayList;
import java.util.Optional;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.ColorManager;
import model.Cards.*;

public class CardView extends StackPane implements CardObserver, CardObservable, DeckObservable {
	
	private Optional<Card> card;
	private final Rectangle outer;
	private final Rectangle inner;
	private ArrayList<CardObserver> cardObservers;
	private ArrayList<DeckObserver> deckObservers;
	
	public CardView(double height, double width) {
		//super(height, width);
		//this.setArcHeight(height/5);
		//this.setArcWidth(height/5);
		this.outer = new Rectangle(height,width);
		outer.setArcHeight(height/5);
		outer.setArcWidth(height/5);
		this.inner = new Rectangle(height-20,width-20);
		inner.setArcHeight((height-20)/5);
		inner.setArcWidth((height-20)/5);
		this.getChildren().addAll(outer,inner);
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
					inner.setFill(Color.WHITE);
					outer.setFill(Color.WHITE);
					notifyDeckObservers();
				}
			}
		
		});
	}
	
	public void forceCard(Card card) {
		this.card = Optional.of(card);
		inner.setFill(ColorManager.getColor(card.getTokenType()));
		outer.setFill(ColorManager.getColor(card.getCardType()));
	}

	@Override
	public void onAction(Card card) {
		if (card == null) {
			inner.setFill(Color.WHITE);
			outer.setFill(Color.WHITE);
		}
		else if (this.card.isEmpty())
		{
			//when the card has an associated png, make that the fill 
			//https://stackoverflow.com/questions/22848829/how-do-i-add-an-image-inside-a-rectangle-or-a-circle-in-javafx
			this.card = Optional.of(card);
			inner.setFill(ColorManager.getColor(card.getTokenType()));
			outer.setFill(ColorManager.getColor(card.getCardType()));
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
