package gui.gameboard;

import java.awt.Dimension;
import java.util.ArrayList;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.ColorManager;
import model.Cards.Card;
import model.Cards.CardObserver;
import model.Tokens.TokenType;

/*
 * TODO: A big design phase is gonna be adding a hand model to the user inventory model
 * Much like other choices, communication between the HandColumnView and the Hand itself can be done with the listener pattern
 * When this class receives a ping (executes onAction) just notify the hand model and pass the card through that way. 
 */
public class HandColumnView extends Pane implements CardObserver {
	
	//This will be the token type of the bonus associated to the card
	private TokenType typeOfColumn;
	private Dimension screenSize;
	private int nCardsInColumn = 0;
	
	public HandColumnView(TokenType type, Dimension screenSize) {
		this.typeOfColumn = type;
		this.screenSize = screenSize;
	}

	@Override
	public void onAction(Card card) {
		//again in this case we'll just match the type of the bonus token
		if (card.getTokenBonus() == typeOfColumn) {
			//might need this to be a minicardview, rectangle for now.<<made cardview, no reason not to yet
			CardView view = new CardView(screenSize.height/20f, screenSize.width/20f);
			view.forceCard(card);
			//view.setFill(ColorManager.getColor(card.getTokenType()));
			view.setLayoutY(5*nCardsInColumn);
			this.getChildren().add(view);
			++nCardsInColumn;
		}
	}
	
	

}
