package comp361.f2022hexanome15.splendorclient.gui.gameboard;

import comp361.f2022hexanome15.splendorclient.model.ColorManager;
import comp361.f2022hexanome15.splendorclient.model.cards.Card;
import comp361.f2022hexanome15.splendorclient.model.cards.Observer;
import comp361.f2022hexanome15.splendorclient.model.tokens.Token;
import comp361.f2022hexanome15.splendorclient.model.tokens.TokenPile;
import comp361.f2022hexanome15.splendorclient.model.tokens.TokenType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

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

	@Override
	public void onAction(boolean bIncrement) {
		if (bIncrement) {
			tokenCounter.setText(String.valueOf(tokenCounter.getCount()+1));
			String.format("Total tokens: %d", tokenCounter.getCount());
		}
		else {
			tokenCounter.setText(String.valueOf(tokenCounter.getCount()-1));
		}
	}
}
