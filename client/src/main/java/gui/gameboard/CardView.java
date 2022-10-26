package gui.gameboard;

import java.util.Optional;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Cards.Card;
import model.Cards.CardObserver;

public class CardView extends Rectangle implements CardObserver {
	
	private Optional<Card> card;
	
	public CardView(double height, double width) {
		super(height, width);
		card = Optional.ofNullable(null);
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

}
