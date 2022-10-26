package model.Cards;

import javafx.scene.paint.Color;
import model.Tokens.TokenType;

public class Card {
	
	//this eventually will be the png that is on the actual card
	private Color color;
	private TokenType tokenBonus;
	
	public Card(Color color, TokenType type) {
		this.color = color;
		tokenBonus = type;
	}
	
	public Color getColor() {
		return color;
	}
	
	public TokenType getTokenBonus() {
		return tokenBonus;
	}
	
	//TODO: need things like cost, associated gem discount, and prestige. Robillard might want these to be flyweights.

}
