package gui.gameboard;

import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.ColorManager;
import model.cards.Card;
import model.cards.Observable;
import model.cards.Observer;

/**
 * Represents the view of a Splendor Card.
 */
public class CardView extends StackPane implements Observer, Observable {

  private Optional<Card> card;
  private final Rectangle outer;
  private final Rectangle inner;
  private final ArrayList<Observer> observers;

  /**
   * Creates a CardView.
   *
   * @param height The height of the CardView
   * @param width The width of the CardView
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
      // TODO onclick notify the deck associated with this row of cards
      //  and add a card and also add this card to the users inventory
      if (card.isPresent()) {
        final Card purchasedCard = card.get();
        card = Optional.empty();
        inner.setFill(Color.WHITE);
        outer.setFill(Color.WHITE);
        notifyObservers(purchasedCard);
      }
    });
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
   *
   * @param card The card that was purchased or reserved
   */
  public void notifyObservers(Card card) {
    for (Observer observer : observers) {
      observer.onAction(card);
    }
  }

  @Override
  public void onAction(Card card) {
    if (card == null) {
      inner.setFill(Color.WHITE);
      outer.setFill(Color.WHITE);
    } else if (this.card.isEmpty()) {
      //when the card has an associated png, make that the fill
      //https://stackoverflow.com/questions/22848829/how-do-i-add-an-image-inside-a-rectangle-or-a-circle-in-javafx
      this.card = Optional.of(card);
      inner.setFill(ColorManager.getColor(card.getTokenType()));
      outer.setFill(ColorManager.getColor(card.getCardType()));
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
