package model.cards;

import javafx.scene.paint.Color;
import model.tokens.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

  Deck deck;

  @BeforeEach
  void setUp() {
    deck = new Deck(CardType.BASE1);
  }
  @Test
  void getSize() {
    assertEquals(40, deck.getSize());
  }

  @Test
  void getColor() {
    assertEquals(Color.GREENYELLOW, deck.getColor());
  }

  @Test
  void deal() {
  }

  @Test
  void replaceCard() {
  }

  @Test
  void addListener() {
  }

  @Test
  void removeListener() {
  }

  @Test
  void notifyObservers() {
  }

  @Test
  void testNotifyObservers() {
  }
}