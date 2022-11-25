package comp361.f2022hexanome15.splendorclient.model.tokens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import comp361.f2022hexanome15.splendorclient.model.ColorManager;
import comp361.f2022hexanome15.splendorclient.model.cards.Observable;
import comp361.f2022hexanome15.splendorclient.model.cards.Observer;
import javafx.scene.paint.Color;

/**
 * Represents a Splendor Token Pile with tokens and type.
 * Observes itself, so that grabbing tokens and buying cards messages can be relayed between the board and the user inventory
 * Observed by TokenPileView to update the counter
 * Observed by TotalAssetCountView to update the counter
 */
public class TokenPile implements Iterable<Token>, Observable, Observer {
	private final ArrayList<Token> tokens;
	private final TokenType type;
	private List<Observer> observers;
	private final Color color;

	/**
	 * Creates a TokenPile.
	 *
	 * @param type The type of tokens that are in the token pile
	 */
	public TokenPile(TokenType type) {
		this.type = type;
		this.tokens = new ArrayList<>();
		this.color = ColorManager.getColor(type);
		observers = new ArrayList<Observer>();
	}
	
	/**
	 * Sets up the TokenPile upon starting the game.
	 */
	public void setUp() {
		if (getType().equals(TokenType.GOLD)) {
			for (int i = 0; i < 5; i++) {
				Token token = new Token(getType());
				addToken(token);
			}
		} else {
			for (int i = 0; i < 7; i++) {
				Token token = new Token(getType());
				addToken(token);
			}
		}
	}
	
	/**
	 * Sets up the TokenPile for Player Inventory. Only for demo.
	 */
	public void setUpDemo() {
		if (!getType().equals(TokenType.GOLD)) {
			for (int i = 0; i < 3; i++) {
				Token token = new Token(getType());
				addToken(token);
			}
		}
	}

	/**
	 * Adds a token to the token pile.
	 *
	 * @param token The token to be added
	 */
	public void addToken(Token token) {
		if (token.getType() == type) {
			tokens.add(token);
			notifyObservers(true);
		}
	}

	/**
	 * Removes a token from the token pile.
	 *
	 */
	public Token removeToken() {
		if (!tokens.isEmpty()) {
			//i.e decrement the associated counter
			notifyObservers(false);
			Token token = tokens.remove(0);
			//notify the gameboard pile/ui pile depending on action.
			notifyObservers(token);
			return token;
		}
		return null;
	}

	/**
	 * Returns the type of the token.
	 *
	 * @return the type of the token
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Returns the number of tokens in the token pile.
	 *
	 * @return the size of the token pile
	 */
	public int getSize() {
		return tokens.size();
	}

	// again this will return the image for fill
	/**
	 * Returns the color of tokens in the token pile.
	 *
	 * @return the color of tokens in the token pile
	 */
	public Color getColor() {
		return color;
	}

	@Override
	public Iterator<Token> iterator() {
		return tokens.iterator();
	}

	@Override
	public void addListener(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void removeListener(Observer observer) {
		observers.remove(observer);
	}
	
	private void notifyObservers(boolean bIncrement) {
		for (Observer observer : observers) {
			observer.onAction(bIncrement);
		}
	}
	
	private void notifyObservers(Token token) {
		for (Observer observer : observers) {
			observer.onAction(token);
		}
	}
	
	@Override
	public void onAction(Token token) {
		tokens.add(token);
		notifyObservers(true);
	}

}