package gui.gameboard;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
//		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//			@Override
//			public void handle(MouseEvent arg0) {
//				//in the future we'll have to send this info to the deck, or to an observer to pop the top card on the deck
//				//alternatively, maybe better would be to register this as an observer of deck and pop the deck directly. 
//				String n = numCardsDisplay.getText();
//				Integer nCards = Integer.valueOf(n);
//				numCardsDisplay.setText(Integer.toString(--nCards));
//				numCardsDisplay.toFront();
//			}
//		
//		});
	}
	
	public Label getNumCardsDisplay() {
		return numCardsDisplay;
	}

}
