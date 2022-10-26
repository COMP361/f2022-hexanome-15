package gui.gameboard;

import javafx.scene.shape.Circle;
import model.Tokens.Token;
import model.Tokens.TokenDeck;

public class TokenDeckView extends Circle {
	
	private TokenDeck tokenDeck;
	private Counter tokenCounter;
	
	public TokenDeckView(float radius, TokenDeck tokenDeck) {
		super(radius);
		//also make this the nice img made by ojas. 
		this.tokenDeck = tokenDeck;
		this.setFill(Token.typeToColor[tokenDeck.getType().ordinal()]);
		tokenCounter = new Counter(tokenDeck.getSize());
	}
	
	public Counter getCounter() {
		return tokenCounter;
	}
}
