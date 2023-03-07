package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.splendorserver.model.cards.CardStatus.NONE;
import static ca.mcgill.splendorserver.model.cards.CardStatus.PURCHASED;
import static ca.mcgill.splendorserver.model.cards.DeckType.*;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.TWO;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {
  CardCost acost;
  Card acard;

  @BeforeEach
  void setUp() {
    acost = new CardCost(0, 0, 4, 0, 0);
    acard = new Card(0, 1, DIAMOND, BASE1, ONE, acost);
  }

  @Test
  void testIsOnBoard() {
    assertEquals(NONE, acard.getCardStatus());
  }

  @Test
  void isReserved() {
    acard.setCardStatus(CardStatus.RESERVED);
    assertTrue(acard.isReserved());
  }

  @Test
  void isPurchased() {
    acard.setCardStatus(PURCHASED);
    assertTrue(acard.isPurchased());

  }

  @Test
  void getCard() {
    Card.makeDeck(BASE1);
    assertEquals(acard, acard.getCard(0));
  }

  @Test
  void makeDeckBase1() {
    List<Card> deck = Card.makeDeck(BASE1);
    assertEquals(40, deck.size());
  }

  @Test
  void makeDeckBase2() {
    List<Card> deck = Card.makeDeck(BASE2);
    assertEquals(30, deck.size());
  }
  @Test
  void makeDeckBase3() {
    List<Card> deck = Card.makeDeck(BASE3);
    assertEquals(20, deck.size());
  }

  @Test
  void makeDeckOrient1() {
    List<Card> deck = Card.makeDeck(ORIENT1);
    assertEquals(10, deck.size());
  }

  @Test
  void makeDeckOrient2() {
    List<Card> deck = Card.makeDeck(ORIENT2);
    assertEquals(10, deck.size());
  }

  @Test
  void makeDeckOrient3() {
    List<Card> deck = Card.makeDeck(ORIENT3);
    assertEquals(10, deck.size());
  }

  @Test
  void getId() {
    assertEquals(0, acard.getId());
  }

  @Test
  void getPrestige() {
    assertEquals(1, acard.getPrestige());
  }

  @Test
  void getTokenBonusAmount() {
    assertEquals(1, acard.getTokenBonusAmount());
  }

  @Test
  void setBonusAmount() {
    acard.setBonusAmount(TWO);
    assertEquals(2,acard.getTokenBonusAmount());
  }

  @Test
  void setBonusType() {
    acard.setBonusType(SAPPHIRE);
    assertEquals(SAPPHIRE,acard.getTokenBonusType());

  }

  @Test
  void getDeckType() {
    assertEquals(BASE1,acard.getDeckType());
  }

  @Test
  void getTokenBonusType() {
    assertEquals(DIAMOND, acard.getTokenBonusType());
  }

  @Test
  void compareTo() {
    Card bcard = new Card(0, 1, TokenType.DIAMOND, DeckType.BASE1, TokenBonusAmount.ONE, acost);
    assertEquals(0, acard.compareTo(bcard));
  }

  @Test
  void getCardCost() {
    assertEquals(acost, acard.getCardCost());
  }

  @Test
  void testToString() {
    assertEquals("Card{"
      + "prestige=" + 1
      + ", tokenBonus=" + DIAMOND
      + ", deckType=" + BASE1
      + ", discount=" + ONE
      + ", cardCost=" + acost
      + '}',
      acard.toString(),
      "");
  }
  @Test
  void testHashCode() {
    CardCost cost = new CardCost(0, 0, 4, 0, 0);
    Card newCard = new Card(0, 1, DIAMOND, BASE1, ONE, acost);
    assertEquals(acard.hashCode(), newCard.hashCode());
  }
}