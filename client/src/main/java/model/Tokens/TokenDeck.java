package model.Tokens;

import java.util.ArrayList;

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

}
