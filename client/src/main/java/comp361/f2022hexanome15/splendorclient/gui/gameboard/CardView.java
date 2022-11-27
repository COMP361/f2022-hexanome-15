package comp361.f2022hexanome15.splendorclient.gui.gameboard;

import comp361.f2022hexanome15.splendorclient.model.ColorManager;
import comp361.f2022hexanome15.splendorclient.model.cards.Card;
import comp361.f2022hexanome15.splendorclient.model.cards.Observable;
import comp361.f2022hexanome15.splendorclient.model.cards.Observer;
import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents the view of a Splendor Card.
 * Observed by UserInventory, for purpose of adding purchased card to User Inventory
 * Observes Deck in order to populate card from top of deck.
 */
public class CardView extends StackPane implements Observer, Observable {

  private Optional<Card> card;
  private final Rectangle outer;
  private final Rectangle inner;
  private final ArrayList<Observer> observers;
  private boolean hasActivePurchaseAttempt = true;

  /**
   * Creates a CardView.
   *
   * @param height The height of the CardView
   * @param width  The width of the CardView
   */
  public CardView(double height, double width) {
    this.outer = new Rectangle(height, width);
    outer.setArcHeight(height / 5);
    outer.setArcWidth(height / 5);
    this.inner = new Rectangle(height - 20, width - 20);
    inner.setArcHeight((height - 20) / 5);
    inner.setArcWidth((height - 20) / 5);
    this.getChildren().addAll(outer, inner);
    card = Optional.empty();
    observers = new ArrayList<>();
    this.setOnMouseClicked(arg0 -> {
      if (card.isPresent()) {
        hasActivePurchaseAttempt = true;
        notifyObservers();
      }
    });
  }

  public Optional<Card> getCard() {
    return card;
  }

  public void revokePurchaseAttempt() {
    hasActivePurchaseAttempt = false;
  }

  /**
   * Fills the CardView with the appropriate colors.
   *
   * @param card The card that is represented by this CardView
   */
  public void forceCard(Card card) {
    this.card = Optional.of(card);
    inner.setFill(ColorManager.getColor(card.getTokenType()));
    outer.setFill(ColorManager.getColor(card.getCardType()));
  }

  /**
   * Notifies all observers that a card has been purchased or reserved.
   */
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.onAction(this);
    }
  }

  @Override
  public void onAction(Card card) {
    if (card == null) {
      inner.setFill(Color.WHITE);
      outer.setFill(Color.WHITE);
    } else if (hasActivePurchaseAttempt) {
      // when the card has an associated png, make that the fill
      // https://stackoverflow.com/questions/22848829/how-do-i-add-an-image-inside-a-rectangle-or-a-circle-in-javafx
      this.card = Optional.of(card);
      inner.setFill(ColorManager.getColor(card.getTokenType()));
      outer.setFill(ColorManager.getColor(card.getCardType()));
      hasActivePurchaseAttempt = false;
    }
  }

  @Override
  public void addListener(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeListener(Observer observer) {
    observers.remove(observer);
  }

}
