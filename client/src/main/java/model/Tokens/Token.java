package model.Tokens;

import javafx.scene.paint.Color;

public class Token {
	
	private TokenType type;
	
	//again eventually using the imgs ojas provided, also this hack is very brittle and bad but it's what i've got for now. 
	public static Color[] typeToColor = new Color[] {Color.GREEN, Color.LIGHTBLUE, Color.BLUE, Color.DARKGREY, Color.MAGENTA, Color.GOLD};
	
	public Token(TokenType type) {
		this.type = type;
	}
	
	public Color getColor() {
		return typeToColor[type.ordinal()];
	}
	
	public TokenType getType() {
		return type;
	}

}
