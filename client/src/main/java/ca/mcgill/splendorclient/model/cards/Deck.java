package ca.mcgill.splendorclient.model.cards;

import ca.mcgill.splendorclient.model.ColorManager;
import java.util.ArrayList;
import javafx.scene.paint.Color;

/**
 * Represents a Splendor Deck with cards, color, tokenBonus, cardType, discount
 * and cost This class implements the Flyweight design pattern.
 */
public class Deck implements Observable {

  private final ArrayList<Card> cards;
  private final Color color;
  private final ArrayList<Observer> observers;

  /**
   * Creates a deck made of cards of a certain type.
   *
   * @param type The type of cards
   */
  public Deck(CardType type) {
    this.color = ColorManager.getColor(type);
    this.cards = (ArrayList<Card>) Card.makeDeck(type);
    observers = new ArrayList<>();
  }

  /**
   * Returns the size of the deck.
   *
   * @return cards.size()
   */
  public int getSize() {
    return cards.size();
  }

  /**
   * Returns the color of the deck.
   *
   * @return color
   */
  public Color getColor() {
    return color;
  }

  /**
   * Deals cards in the deck to the board.
   */
  public void deal() {
    if (!cards.isEmpty()) {
      for (int i = 0; i < 4; ++i) {
        notifyObservers(cards.get(0), i);
        cards.remove(0);
      }
    }
  }

  /**
   * Replaces an empty spot on the board with a card from the deck. This method is
   * used after a card is reserved or purchased.
   */
  public void replaceCard() {
    if (!cards.isEmpty()) {
      notifyObservers(cards.get(0));
      cards.remove(0);
    }
  }

  @Override
  public void addListener(Observer cardView) {
    observers.add(cardView);
  }

  @Override
  public void removeListener(Observer cardView) {
    // probably have to do something more sophisticated like an equals method.
    observers.remove(cardView);
  }

  // a terrible hack to work around instantiating each card view for the first
  // time
  /**
   * Instantiates card view.
   *
   * @param card          The card that needs to be instantiated
   * @param observerIndex The index of the observer that must be notified
   */
  public void notifyObservers(Card card, int observerIndex) {
    // this is a terrible hack, needs a re-design.
    observers.get(observerIndex).onAction(card);
  }

  // use this one to deal out a new card after one got purchased
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

}