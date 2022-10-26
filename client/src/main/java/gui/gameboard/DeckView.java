package gui.gameboard;

import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import model.Cards.*;

public class DeckView extends Rectangle {
	
	private Label numCardsDisplay;
	private Deck deck;
	
	public DeckView(Deck deck, double height, double width) {
		super(height, width);
		numCardsDisplay = new Label(Integer.toString(deck.getSize()));
		this.deck = deck;
		this.setFill(deck.getColor());
	}
	
	public Label getNumCardsDisplay() {
		return numCardsDisplay;
	}

}
