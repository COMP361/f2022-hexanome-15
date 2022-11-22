package model.cards;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

  @Test
  void getSize() {
    Deck deck = new Deck(CardType.BASE1);
    assertEquals(40, deck.getSize());
  }

  @Test
  void getColor() {
    Deck deck = new Deck(CardType.BASE1);
    assertEquals(Color.GREENYELLOW, deck.getColor());
  }

  @Test
  void deal() {

  }

  @Test
  void replaceCard() {
  }
}