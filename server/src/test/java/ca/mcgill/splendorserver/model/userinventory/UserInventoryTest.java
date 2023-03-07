package ca.mcgill.splendorserver.model.userinventory;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cards.OrientCard;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsPile;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static ca.mcgill.splendorserver.model.cards.CardStatus.*;
import static ca.mcgill.splendorserver.model.cards.DeckType.*;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.*;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static ca.mcgill.splendorserver.model.tradingposts.Power.GAIN_5_PRESTIGE;
import static org.junit.jupiter.api.Assertions.*;

class UserInventoryTest {
  UserInventory uinv;
  CardCost cost;
  OrientCard oCard1;
  Card card1;

  Noble anoble;

  PlayerWrapper aPlayer;

  @BeforeEach
  void setUp() {
    cost = new CardCost(1,0,0,0,0);
    oCard1 = new OrientCard(1,3, null, ORIENT1, ZERO, cost,true, new ArrayList<>());
    card1 = new Card(1,1, DIAMOND, BASE1, ZERO, cost);
    anoble = new Noble(new CardCost(1,0,0,0,0));
    aPlayer = PlayerWrapper.newPlayerWrapper("Slava");
    uinv = new UserInventory(aPlayer, Optional.ofNullable(RED));
  }

  @Test
  void getPrestigeWon() {
    assertEquals(0, uinv.getPrestigeWon());
  }

  @Test
  void addTokens() {
    Token token1 = new Token(DIAMOND);
    uinv.addToken(token1);
    assertEquals(1, uinv.getTokenPiles().get(DIAMOND).getSize());
  }

  @Test
  void removeTokenByTokenType() {
    Token token1 = new Token(DIAMOND);
    uinv.addToken(token1);
    assertEquals(DIAMOND, uinv.removeTokenByTokenType(DIAMOND).getType());
  }

  @Test
  void reservedCardCount() {
    uinv.addReservedCard(card1);
    assertEquals(1, uinv.reservedCardCount());
  }

  @Test
  void purchasedCardCount() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    assertEquals(1, uinv.purchasedCardCount());

  }

  @Test
  void getUnpairedSpiceCard() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(oCard1);
    assertEquals(oCard1, uinv.getUnpairedSpiceCard());
  }

  @Test
  void hasCardReserved() {
    uinv.addReservedCard(card1);
    assertTrue(uinv.hasCardReserved(card1));
  }

  @Test
  void hasCard() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    assertTrue(uinv.hasCard(card1));
  }

  @Test
  void hasCardsReserved() {
    uinv.addReservedCard(card1);
    assertTrue(uinv.hasCardsReserved());
  }

  @Test
  void canAffordCard() {
    uinv.addToken(new Token(DIAMOND));
    assertTrue(uinv.canAffordCard(card1));
  }

  @Test
  void addReservedNoble() {
    uinv.addReservedNoble(anoble);
    assertEquals(anoble, uinv.getNobles().get(0));
  }

  @Test
  void addCascadeLevelOne() {
    uinv.addCascadeLevelOne(oCard1);
    assertEquals(oCard1, uinv.getCards().get(0));
  }

  @Test
  void addCascadeLevelTwo() {
    OrientCard oCard2 = new OrientCard(1,3, ONYX, ORIENT2, ZERO, cost,false, new ArrayList<>());
    uinv.addCascadeLevelTwo(oCard2);
    assertEquals(oCard2, uinv.getCards().get(0));
  }

  @Test
  void canBeVisitedByNoble() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    assertTrue(uinv.canBeVisitedByNoble(anoble));
  }

  @Test
  void receiveVisitFrom() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.receiveVisitFrom(anoble);
    assertEquals(anoble, uinv.getNobles().get(0));
  }

  @Test
  void tokenCount() {
    assertEquals(0, uinv.tokenCount());
  }

  @Test
  void getPlayer() {
    assertEquals(aPlayer, uinv.getPlayer());
  }

  @Test
  void addPower() {
    TradingPostSlot tradingSlot = new TradingPostSlot(false, GAIN_5_PRESTIGE, cost);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.addPower(tradingSlot.getPower());
    assertEquals(GAIN_5_PRESTIGE, uinv.getPowers().get(0));
  }

  @Test
  void canReceivePower() {
    TradingPostSlot tradingSlot = new TradingPostSlot(false, GAIN_5_PRESTIGE, cost);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    assertTrue(uinv.canReceivePower(tradingSlot));
  }

  @Test
  void iterator() {
    OrientCard card2 = new OrientCard(1,3, ONYX, ORIENT2, ZERO, cost,false, new ArrayList<>());
    uinv.addToken(new Token(DIAMOND));
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.purchaseCard(card2);
    Iterator<Card> iter = uinv.iterator();
    assertTrue(iter.hasNext());
  }

  @Test
  void getTokenPiles() {
    assertEquals(6, uinv.getTokenPiles().size());
  }

  @Test
  void getCoatOfArmsPile() {
    CoatOfArmsPile pile = new CoatOfArmsPile(RED);
    assertEquals(pile, uinv.getCoatOfArmsPile());
  }

  @Test
  void purchasedCardCountByType() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    assertEquals(1, uinv.purchasedCardCountByType(DIAMOND));
  }

  @Test
  void removePower() {
    TradingPostSlot tradingSlot = new TradingPostSlot(false, GAIN_5_PRESTIGE, cost);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.addPower(tradingSlot.getPower());
    uinv.removePower(GAIN_5_PRESTIGE);
    assertEquals(0, uinv.getPowers().size());
  }

  @Test
  void removeNoble() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.receiveVisitFrom(anoble);
    uinv.removeNoble(anoble);
    assertEquals(0, uinv.getNobles().size());
  }
}