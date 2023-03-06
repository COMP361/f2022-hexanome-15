package ca.mcgill.splendorserver.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Splendor Deck with cards, color, tokenBonus, cardType, discount
 * and cost. This class implements the Flyweight design pattern.
 */
public class Deck {

  private final ArrayList<Card> cards;
  private final DeckType        type;

  /**
   * Creates a deck made of cards of a certain type.
   *
   * @param type The type of cards
   */
  public Deck(DeckType type) {
    assert type != null;
    this.cards = (ArrayList<Card>) Card.makeDeck(type);
    Collections.shuffle(this.cards);
    this.type  = type;
  }

  /**
   * Removes the top card from the deck and returns it.
   *
   * @return the top card from the deck
   */
  public Card draw() {
  assert !cards.isEmpty();
    return cards.remove(0);
  }

  /**
   * Checks if the deck is empty.
   *
   * @return a boolean determining if the deck is empty
   */
  public boolean isEmpty() {
    return cards.isEmpty();
  }

  /**
   * Returns the card type of the deck.
   *
   * @return the card type of the deck
   */
  public DeckType getType() {
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
   * If the deck is a base game deck, 4 cards will be dealt.
   * If the deck is an orient expansion deck, 2 cards will be dealt.
   *
   * @return a list of cards that were dealt
   */
  public List<Card> deal() {
    List<Card> playingField = new ArrayList<Card>();
    if (!cards.isEmpty()) {
      if (type == DeckType.BASE1 || type == DeckType.BASE2 || type == DeckType.BASE3) {
        for (int i = 0; i < 4; i++) {
          playingField.add(cards.remove(0));
        }
      } else {
        for (int i = 0; i < 2; i++) {
          playingField.add(cards.remove(0));
        }
      }
    }
    return playingField;
  }

}
