package gui.gameboard;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.converter.NumberStringConverter;
import model.Cards.*;

import java.util.Optional;

public class DeckView extends Rectangle implements DeckObserver {
	
	private Label numCardsDisplay;
	private Deck deck;
	
	public DeckView(Deck deck, double height, double width) {
		super(height, width);
		this.setArcHeight(height/5);
		this.setArcWidth(height/5);
		numCardsDisplay = new Label();
		numCardsDisplay.textProperty().bind(new SimpleIntegerProperty(deck.getSize()).asString());
		this.deck = deck;
		this.setFill(deck.getColor());
	}
	
	public Label getNumCardsDisplay() {
		return numCardsDisplay;
	}

	@Override
	public void onAction() {
		deck.replaceCard();
		if (deck.getSize() == 0) {
			this.setFill(Color.WHITE);
		}
	}
}
