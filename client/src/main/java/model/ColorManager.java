package model;

import javafx.scene.paint.Color;
import model.Cards.CardType;
import model.Tokens.TokenType;

public class ColorManager {
	public static Color getColor(TokenType type) {
		switch (type) {//yes ik switch statements used liberally are an antipattern but i have to bind colors Somewhere
		//might refactor after 
		case DIAMOND:
			return Color.LIGHTCYAN;
		case SAPPHIRE:
			return Color.BLUE;
		case EMERALD:
			return Color.GREEN;
		case RUBY:
			return Color.RED;
		case ONYX:
			return Color.BLACK;
		default:
			return Color.WHITE;
		}
	}
	public static Color getColor(CardType type) {
		switch (type) {//yes ik switch statements used liberally are an antipattern but i have to bind colors Somewhere
		//might refactor after 
		case BASE1:
			return Color.GREENYELLOW;
		case BASE2:
			return Color.GOLD;
		case BASE3:
			return Color.DEEPSKYBLUE;
		case ORIENT1:
			return Color.DARKRED;
		case ORIENT2:
			return Color.FIREBRICK;
		case ORIENT3:
			return Color.CRIMSON;
		default:
			return Color.WHITE;
		}
	}
}