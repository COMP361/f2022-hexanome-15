package ca.mcgill.splendorclient.model.cards;

import ca.mcgill.splendorclient.model.tokens.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
  Card card;

  @BeforeEach
  void setUp() {
    card = new Card(30, 1, TokenType.DIAMOND, CardType.BASE1,   1, new int[]{0, 0, 1, 0, 0});
  }

  @Test
  void makeDeckBase1() {
    card.makeDeck(card.getCardType());
    assertEquals(card, card.getCard(card.getId()));
  }

  @Test
  void makeDeckBase2() {
    Card card1 = new Card(60, 1, TokenType.DIAMOND, CardType.BASE2,   1, new int[]{0, 0, 1, 0, 0});
    card.makeDeck(card1.getCardType());
    assertEquals(card1,card1.getCard(card1.getId()));
  }

  @Test
  void makeDeckBase3() {
    Card card1 = new Card(80, 1, TokenType.DIAMOND, CardType.BASE3,   1, new int[]{0, 0, 1, 0, 0});
    card.makeDeck(card1.getCardType());
    assertEquals(card1,card1.getCard(card1.getId()));
  }

  @Test
  void getPrestige() {
    assertEquals(1, card.getPrestige());
  }

  @Test
  void getDiscount() {
    assertEquals(1, card.getDiscount());
  }

  @Test
  void getCost() {
    int[] cost = new int[]{0, 0, 1, 0, 0};
    Card card1 = new Card(30, 1, TokenType.DIAMOND, CardType.BASE1,   1, cost);
    assertEquals(cost, card1.getCost());
  }

  @Test
  void getCardType() {
    assertEquals(CardType.BASE1, card.getCardType());
  }

  @Test
  void getTokenType() {
    assertEquals(TokenType.DIAMOND, card.getTokenType());
  }

  @Test
  void equalsSameObject() {
    assertEquals(card, card);
  }

  @Test
  void equalsWithNonCardObject() {
    assertNotEquals(card, new Object());
  }

  @Test
  void hashCodeTest() {
    Card card1 = new Card(30, 1, TokenType.DIAMOND, CardType.BASE1,   1, new int[]{0, 0, 1, 0, 0});
    assertEquals(card.hashCode(), card1.hashCode());
  }
}