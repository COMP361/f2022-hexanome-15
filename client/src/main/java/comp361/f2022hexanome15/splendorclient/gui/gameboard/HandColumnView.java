package comp361.f2022hexanome15.splendorclient.gui.gameboard;

import java.awt.Dimension;

import comp361.f2022hexanome15.splendorclient.model.cards.Card;
import comp361.f2022hexanome15.splendorclient.model.cards.Observer;
import comp361.f2022hexanome15.splendorclient.model.tokens.TokenType;
import javafx.scene.layout.Pane;

/*
 * TODO: A big design phase is gonna be adding a hand model to the user inventory model
 * Much like other choices, communication between the HandColumnView
 * and the Hand itself can be done with the listener pattern
 * When this class receives a ping (executes onAction)
 * just notify the hand model and pass the card through that way.
 */

/**
 * Represents the view of the user's inventory.
 */
public class HandColumnView extends Pane implements Observer {

	// This will be the token type of the bonus associated to the card
	private final TokenType typeOfColumn;
	private final Dimension screenSize;
	private int ncardsInColumn = 0;

	/**
	 * Creates a HandColumnView.
	 *
	 * @param type       the type of token bonus of the card
	 * @param screenSize the size of the screen
	 */
	public HandColumnView(TokenType type, Dimension screenSize) {
		this.typeOfColumn = type;
		this.screenSize = screenSize;
	}

	@Override
	public void onAction(Card card) {
		// again in this case we'll just match the type of the bonus token
		if (card.getTokenType() == typeOfColumn) {
			CardView cardView = new CardView(screenSize.height / 20f, screenSize.width / 20f);
			cardView.forceCard(card);
			// view.setFill(ColorManager.getColor(card.getTokenType()));
			cardView.setLayoutY(5 * ncardsInColumn);
			this.getChildren().add(cardView);
			++ncardsInColumn;
		}
	}

}
