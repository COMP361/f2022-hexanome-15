package gui.gameboard;

import java.awt.Dimension;
import javafx.scene.layout.Pane;
import model.cards.Card;
import model.cards.CardObserver;
import model.tokens.TokenType;

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
public class HandColumnView extends Pane implements CardObserver {

  //This will be the token type of the bonus associated to the card
  private final TokenType typeOfColumn;
  private final Dimension screenSize;
  private int ncardsInColumn = 0;

  /**
   * Creates a HandColumnView.
   *
   * @param type the type of token bonus of the card
   * @param screenSize the size of the screen
   */
  public HandColumnView(TokenType type, Dimension screenSize) {
    this.typeOfColumn = type;
    this.screenSize = screenSize;
  }

  @Override
  public void onAction(Card card) {
    //again in this case we'll just match the type of the bonus token
    if (card.getTokenType() == typeOfColumn) {
      CardView view = new CardView(screenSize.height / 20f, screenSize.width / 20f);
      view.forceCard(card);
      //view.setFill(ColorManager.getColor(card.getTokenType()));
      view.setLayoutY(5 * ncardsInColumn);
      this.getChildren().add(view);
      ++ncardsInColumn;
    }
  }



}
