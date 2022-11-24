package comp361.f2022hexanome15.splendorclient.gui.gameboard;

import comp361.f2022hexanome15.splendorclient.model.ColorManager;
import comp361.f2022hexanome15.splendorclient.model.cards.Card;
import comp361.f2022hexanome15.splendorclient.model.cards.Observer;
import comp361.f2022hexanome15.splendorclient.model.tokens.Token;
import comp361.f2022hexanome15.splendorclient.model.tokens.TokenPile;
import comp361.f2022hexanome15.splendorclient.model.tokens.TokenType;
import javafx.scene.shape.Circle;

/**
 * Represents the view of a token pile. 
 * Observes token piles.
 */
public class TokenPileView extends Circle implements Observer {

	private final Counter tokenCounter;

	/**
	 * Creates a TokenPileView.
	 *
	 * @param radius    the radius of the circle used to represent the token pile
	 * @param tokenPile the token pile that is represented by this token pile view
	 */
	public TokenPileView(float radius, TokenType type) {
		super(radius);
		this.setFill(ColorManager.getColor(type));
		tokenCounter = new Counter(0);
	}

	public Counter getCounter() {
		return tokenCounter;
	}

	//only doing this for purchasing action for now, need another one for grabbing action. 
	@Override
	public void onAction(boolean bIncrement) {
		if (bIncrement) {
			tokenCounter.setText(String.valueOf(tokenCounter.getCount()+1));
		}
		else {
			tokenCounter.setText(String.valueOf(tokenCounter.getCount()-1));
		}
	}
}
