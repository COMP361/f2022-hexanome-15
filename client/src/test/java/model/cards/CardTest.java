package model.cards;

import model.tokens.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

  @Test
  void getCard() {

  }

  @Test
  void makeDeck() {

  }

  @Test
  void getId() {
    Card card = new Card(80, 1, TokenType.DIAMOND, CardType.BASE3,   1, new int[]{0, 0, 1, 0, 0});
    assertEquals(80, card.getId());
  }

  @Test
  void getPrestige() {
    Card card = new Card(80, 1, TokenType.DIAMOND, CardType.BASE3,   1, new int[]{0, 0, 1, 0, 0});
    assertEquals(1, card.getPrestige());
  }

  @Test
  void getDiscount() {
    Card card = new Card(80, 1, TokenType.DIAMOND, CardType.BASE3,   1, new int[]{0, 0, 1, 0, 0});
    assertEquals(1, card.getDiscount());
  }

  @Test
  void getCost() {
    Card card = new Card(80, 1, TokenType.DIAMOND, CardType.BASE3,   1, new int[]{0, 0, 1, 0, 0});
    assertEquals(1, card.getDiscount());
  }

  @Test
  void getCardType() {
    Card card = new Card(80, 1, TokenType.DIAMOND, CardType.BASE3,   1, new int[]{0, 0, 1, 0, 0});
    assertEquals(CardType.BASE3, card.getCardType());
  }

  @Test
  void getTokenType() {
    Card card = new Card(80, 1, TokenType.DIAMOND, CardType.BASE3,   1, new int[]{0, 0, 1, 0, 0});
    assertEquals(TokenType.DIAMOND, card.getTokenType());
  }
}