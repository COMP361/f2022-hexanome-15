package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.tokens.TokenType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.splendorserver.model.cards.CardStatus.PURCHASED;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE2;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE3;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.TWO;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {
  CardCost acost = new CardCost(3,4,5,2,1);
  Card acard = new Card(2,3,EMERALD,BASE3,ONE,acost);
  Card bcard = new Card(5,3,DIAMOND,BASE2,TWO,acost);
  List<Card> cards = new ArrayList<>();

  @Test
  void isReserved() {
    acard.setCardStatus(CardStatus.NONE);
    assertTrue(!acard.isReserved());
    acard.setCardStatus(CardStatus.RESERVED);
    assertTrue(acard.isReserved());
  }

  @Test
  void isPurchased() {
    acard.setCardStatus(CardStatus.NONE);
    assertTrue(!acard.isPurchased());
    acard.setCardStatus(PURCHASED);
    assertTrue(acard.isPurchased());

  }

  @Test
  void getCard() {
    acard.makeDeck(BASE3);
    assertEquals(acard,acard.getCard(2),"");
  }

  @Test
  void makeDeck() {
  }

  @Test
  void getId() {
    assertEquals(2,acard.getId(),"");
  }

  @Test
  void getPrestige() {
    assertEquals(3,acard.getPrestige(),"");
  }

  @Test
  void getTokenBonusAmount() {
    assertEquals(1,acard.getTokenBonusAmount(),"");
  }

  @Test
  void setBonusAmount() {
    acard.setBonusAmount(TWO);
    assertEquals(2,acard.getTokenBonusAmount(),"");
  }

  @Test
  void setBonusType() {
    acard.setBonusType(SAPPHIRE);
    assertEquals(SAPPHIRE,acard.getTokenBonusType(),"");

  }

  @Test
  void getDeckType() {
    assertEquals(BASE3,acard.getDeckType(),"");
  }

  @Test
  void getTokenBonusType() {
    assertEquals(EMERALD,acard.getTokenBonusType(),"");
  }

  @Test
  void testEquals() {
    assertTrue(acard.equals(acard),"");
    assertFalse(acard.equals(BASE2),"");
  }

  @Test
  void testHashCode() {
    assertEquals(33,acard.hashCode(),"");
  }

  @Test
  void compareTo() {
    assertEquals(2,acard.compareTo(bcard),"");
  }

  @Test
  void getCardCost() {
    assertEquals(acost,acard.getCardCost(),"");
  }

  @Test
  void getCardStatus() {
    acard.setCardStatus(CardStatus.NONE);
    assertEquals(CardStatus.NONE,acard.getCardStatus(),"");
  }

  @Test
  void setCardStatus() {
    acard.setCardStatus(PURCHASED);
    assertEquals(PURCHASED,acard.getCardStatus(),"");
  }

  @Test
  void testToString() {
    assertEquals("Card{"
      + "prestige=" + 3
      + ", tokenBonus=" + EMERALD
      + ", deckType=" + BASE3
      + ", discount=" + ONE
      + ", cardCost=" + acost
      + '}',
      acard.toString(),
      "");
  }
}