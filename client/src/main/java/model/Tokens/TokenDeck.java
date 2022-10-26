package model.Tokens;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class TokenDeck {
	
	private ArrayList<Token> tokens;
	
	private TokenType type;
	
	public TokenDeck(TokenType type) {
		this.type = type;
		tokens = new ArrayList<Token>();
	}
	
	public void addToken(Token token) {
		if (token.getType() == type) {
			tokens.add(token);
		}
	}
	
	public TokenType getType() {
		return type;
	}
	
	public int getSize() {
		return tokens.size();
	}
	
	//again this will return the image for fill
	public Color getColor() {
		return Token.typeToColor[type.ordinal()];
	}

}
