package ca.mcgill.splendorserver.model.cards;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Splendor Deck with cards, color, tokenBonus, cardType, discount
 * and cost This class implements the Flyweight design pattern.
 */
public class Deck {

  private final ArrayList<Card> cards;
  private final CardType type;

  /**
   * Creates a deck made of cards of a certain type.
   *
   * @param type The type of cards
   */
  public Deck(CardType type) {
    this.cards = (ArrayList<Card>) Card.makeDeck(type);
    this.type = type;
  }

  /**
   * Returns the card type of the deck.
   *
   * @return the card type of the deck
   */
  public CardType getType() {
    return type;
  }

  /**
   * Returns the cards in the deck.
   *
   * @return the cards in the deck
   */
  public ArrayList<Card> getCards() {
    return cards;
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
   * Deals cards in the deck to the board.
   */
  public List<Card> deal() {
    List<Card> playingField = new ArrayList<Card>();
    if (!cards.isEmpty()) {
      for (int i = 0; i < 4; ++i) {
        playingField.add(cards.remove(0));
      }
    }
    return playingField;
  }

}
