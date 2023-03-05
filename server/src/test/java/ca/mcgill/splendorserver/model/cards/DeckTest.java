package ca.mcgill.splendorserver.model.cards;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static ca.mcgill.splendorserver.model.cards.DeckType.BASE2;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.tokens.TokenType.ONYX;
import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

  Deck deck = new Deck(BASE2);
  CardCost cardCost = new CardCost(4, 0, 1, 0, 2);
  @Test
  void draw() {
    int asize = deck.getSize();
    deck.draw();
    assertNotEquals(deck.getSize(),asize);
  }

  @Test
  void isEmpty() {
    assertFalse(deck.isEmpty(),"");
  }

  @Test
  void getType() {
    assertEquals(BASE2,deck.getType(),"");
  }

  @Test
  void getCards() {
    int i = 30;
    assertEquals(i,(deck.getCards()).size(),"");
  }

  @Test
  void getSize() {
    assertEquals(30,deck.getSize(),"");
  }

  @Test
  void deal() {
    assertEquals(4,deck.deal().size(),"");
  }

}