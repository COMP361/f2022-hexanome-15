package model.Cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import javafx.scene.paint.Color;
import model.Tokens.TokenType;

public class Card {
	private static final ArrayList<Card> cards = new ArrayList<Card>();
	//this eventually will be the png that is on the actual card
	private final int id;
	private final int prestige;
	private final TokenType tokenBonus;
	private int discount;//idk if we need this, but orient pairing can increase discount from 1 to 2 so maybe
	private final int[] cost;
	private Color color;
	
	public Card(int id, int prestige, TokenType tokenBonus, int discount, int[] cost) {
		this.id = id;
		this.prestige = prestige;
		this.tokenBonus = tokenBonus;
		this.discount = discount;
		this.cost = cost;
		//this.color = color;
	}
	
	public Card GetCard(int id) {//bad design ill get back to this		
		return cards.get(id);
	}
	
	public static List<Card> MakeDeck(CardType type) {
		if (cards.size() == 0) {
			generateCards();
		}
		List<Card> toSend;
		switch (type) {
		case BASE1:
			toSend = new ArrayList<Card>(cards.subList(0, 40));
			break;
		case BASE2:
			toSend = new ArrayList<Card>(cards.subList(40, 70));
			break;
		case BASE3:
			toSend = new ArrayList<Card>(cards.subList(70, 90));
			break;
		default: return null;
		}
		Collections.shuffle(toSend);
		return toSend;
	}
	
	public Color getColor() {
		return color;
	}
	
	public TokenType getTokenBonus() {
		return tokenBonus;
	}
	
	private static void generateCards() {//dw i didnt write this its generated
		cards.add(new Card(0,  1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(1,  1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(2,  1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(3,  1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(4,  1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(5,  1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(6,  1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(7,  1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(8,  1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(9,  1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(10, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(11, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(12, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(13, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(14, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(15, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(16, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(17, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(18, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(19, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(20, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(21, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(22, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(23, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(24, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(25, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(26, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(27, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(28, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(29, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(30, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(31, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(32, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(33, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(34, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(35, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(36, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(37, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(38, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(39, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(40, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(41, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(42, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(43, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(44, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(45, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(46, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(47, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(48, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(49, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(50, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(51, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(52, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(53, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(54, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(55, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(56, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(57, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(58, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(59, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(60, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(61, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(62, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(63, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(64, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(65, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(66, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(67, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(68, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(69, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(70, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(71, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(72, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(73, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(74, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(75, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(76, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(77, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(78, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(79, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(80, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(81, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(82, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(83, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(84, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));

		cards.add(new Card(85, 1, TokenType.DIAMOND,   1, new int[]{0,0,4,0,0}));
		cards.add(new Card(86, 1, TokenType.SAPPHIRE,  1, new int[]{0,0,0,4,0}));
		cards.add(new Card(87, 1, TokenType.EMERALD,   1, new int[]{0,0,0,0,4}));
		cards.add(new Card(88, 1, TokenType.RUBY,      1, new int[]{4,0,0,0,0}));
		cards.add(new Card(89, 1, TokenType.ONYX,      1, new int[]{0,4,0,0,0}));
	}
	
	//TODO: need things like cost, associated gem discount, and prestige. Robillard might want these to be flyweights.

}
