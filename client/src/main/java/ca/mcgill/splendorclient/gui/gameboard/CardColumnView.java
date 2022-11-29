package ca.mcgill.splendorclient.gui.gameboard;


import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.splendorclient.model.cards.Card;
import ca.mcgill.splendorclient.model.cards.Observable;
import ca.mcgill.splendorclient.model.cards.Observer;
import ca.mcgill.splendorclient.model.tokens.TokenType;
import javafx.scene.layout.Pane;


/**
 * Represents the view of the user's inventory.
 * Observes UserInventory for notification of valid purchase and associated card
 * Observed by Deck for notification of dealing new card
 */
public class CardColumnView extends Pane implements Observer, Observable {

  // This will be the token type of the bonus associated to the card
  private final TokenType typeOfColumn;
  private final Dimension screenSize;
  private int ncardsInColumn = 0;
  private List<Observer> observers;
  private final Counter numCardsDisplay;

  /**
   * Creates a HandColumnView.
   *
   * @param type       the type of token bonus of the card
   * @param screenSize the size of the screen
   */
  public CardColumnView(TokenType type, Dimension screenSize) {
    this.typeOfColumn = type;
    this.screenSize = screenSize;
    observers = new ArrayList<Observer>();
    this.numCardsDisplay = new Counter(ncardsInColumn);
  }

  @Override
  public void onAction(Card card) {
    // again in this case we'll just match the type of the bonus token
    if (card.getTokenType() == typeOfColumn) {
      // TODO: refactor the cardView class to accept a functional definition
      // of what to do onclick, for these, do nothing.
      CardView cardView = new CardView(screenSize.height / 20f, screenSize.width / 20f);
      cardView.forceCard(card);
      cardView.setLayoutY(5 * ncardsInColumn);
      this.getChildren().add(cardView);
      ++ncardsInColumn;
      notifyObservers(card);
      notifyObservers(true);
    }
    numCardsDisplay.setText(Integer.toString(ncardsInColumn));
  }

  @Override
  public void onAction(boolean increment) {
    notifyObservers(true);
  }

  @Override
  public void addListener(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeListener(Observer observer) {
    observers.remove(observer);
  }

  private void notifyObservers(Card card) {
    for (Observer observer : observers) {
      observer.onAction(card);
    }
  }

  private void notifyObservers(boolean increment) {
    for (Observer observer : observers) {
      observer.onAction(increment);
    }
  }

  public Counter getNumCardsDisplay() {
    return numCardsDisplay;
  }

}
