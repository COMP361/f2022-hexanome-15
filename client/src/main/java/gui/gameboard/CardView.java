package gui.gameboard;

import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.ColorManager;
import model.cards.Card;
import model.cards.CardObservable;
import model.cards.CardObserver;
import model.cards.DeckObservable;
import model.cards.DeckObserver;

/**
 * Represents the view of a Splendor Card.
 */
public class CardView extends StackPane implements CardObserver, CardObservable, DeckObservable {

  private Optional<Card> card;
  private final Rectangle outer;
  private final Rectangle inner;
  private final ArrayList<CardObserver> cardObservers;
  private final ArrayList<DeckObserver> deckObservers;

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
    cardObservers = new ArrayList<>();
    deckObservers = new ArrayList<>();
    this.setOnMouseClicked(arg0 -> {
      // TODO onclick notify the deck associated with this row of cards
      //  and add a card and also add this card to the users inventory
      if (card.isPresent()) {
        notifyCardObservers(card.get());
        card = Optional.empty();
        inner.setFill(Color.WHITE);
        outer.setFill(Color.WHITE);
        notifyDeckObservers();
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
  public void notifyCardObservers(Card card) {
    for (CardObserver observer : cardObservers) {
      observer.onAction(card);
    }
  }

  /**
   * Notifies all observers that a card has been purchased or reserved.
   *
   */
  public void notifyDeckObservers() {
    for (DeckObserver observer : deckObservers) {
      observer.onAction();
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
  public void addListener(CardObserver observer) {
    cardObservers.add(observer);
  }

  @Override
  public void addListener(DeckObserver observer) {
    deckObservers.add(observer);
  }

  @Override
  public void removeListener(CardObserver observer) {
    cardObservers.remove(observer);
  }

  @Override
  public void removeListener(DeckObserver observer) {
    deckObservers.add(observer);
  }
}
