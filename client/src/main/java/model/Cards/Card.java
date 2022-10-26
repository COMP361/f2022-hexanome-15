package model.Cards;

import javafx.scene.paint.Color;

public class Card {
	
	//this eventually will be the png that is on the actual card
	private Color color;
	
	public Card(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	//TODO: need things like cost, associated gem discount, and prestige. Robillard might want these to be flyweights.

}
