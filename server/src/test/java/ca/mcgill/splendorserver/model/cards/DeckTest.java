package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.savegame.DeckJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ca.mcgill.splendorserver.model.cards.DeckType.*;
import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

  private Deck deck;

  @BeforeEach
  void setUp() {
    deck = new Deck(BASE2);
  }

  @Test
  void draw() {
    deck.draw();
    assertEquals(29, deck.getSize());
  }

  @Test
  void isEmpty() {
    assertFalse(deck.isEmpty());
  }

  @Test
  void getType() {
    assertEquals(BASE2, deck.getType());
  }

  @Test
  void getCards() {
    assertEquals(30, (deck.getCards()).size());
  }

  @Test
  void getSize() {
    assertEquals(30,deck.getSize());
  }

  @Test
  void dealBaseDeck() {
    assertEquals(4,deck.deal().size());
  }

  @Test
  void dealOrientDeck() {
    Deck newDeck = new Deck(ORIENT1);
    assertEquals(2, newDeck.deal().size());
  }

  @Test
  void createDeck() {
    DeckJson deckJson = new DeckJson(deck);
    Deck deck1 = new Deck(deckJson);
    assertEquals(deck.getSize(), deck1.getSize());
  }

}