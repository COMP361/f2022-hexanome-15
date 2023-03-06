package ca.mcgill.splendorserver.model.userinventory;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cards.OrientCard;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.nobles.NobleStatus;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
import ca.mcgill.splendorserver.model.tradingposts.Power;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static ca.mcgill.splendorserver.model.cards.CardStatus.*;
import static ca.mcgill.splendorserver.model.cards.DeckType.*;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.*;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static ca.mcgill.splendorserver.model.tradingposts.Power.GAIN_5_PRESTIGE;
import static ca.mcgill.splendorserver.model.tradingposts.Power.GOLD_TOKENS_WORTH_2_GEMS_SAME_COL;
import static org.junit.jupiter.api.Assertions.*;

class UserInventoryTest {
  UserInventory uinv;
  List<Action> acts = new ArrayList<>();
  CardCost oCost1 = new CardCost(2,0,0,0,1);
  CardCost oCost2 = new CardCost(1,0,0,0,2);
  OrientCard oCard1 = new OrientCard(1,3,ONYX,ORIENT1,ZERO,oCost1,true,acts);
  OrientCard oCard2 = new OrientCard(1,3,ONYX,ORIENT2,ZERO,oCost1,true,acts);
  CardCost cost1 = new CardCost(2,0,0,0,0);
  CardCost cost2 = new CardCost(0,0,0,0,1);
  CardCost cost3 = new CardCost(0,0,0,0,1);
  Card card1 = new Card(1,1,DIAMOND,BASE1,ZERO,cost1);
  Card card2 = new Card(2,1,ONYX,BASE1,ONE,cost2);
  Token token1 = new Token(DIAMOND);
  Token token2 = new Token(DIAMOND);
  Token token3 = new Token(ONYX);
  Token token4 = new Token(ONYX);
  Token token5 = new Token(ONYX);
  Token tokenA = new Token(EMERALD);
  Token tokenB = new Token(SAPPHIRE);
  Token tokenC = new Token(RUBY);

  PlayerWrapper aPlayer = PlayerWrapper.newPlayerWrapper("Slava");
  Optional<CoatOfArmsType> coat = Optional.of(RED);
  TokenPile tp1 = new TokenPile(DIAMOND);
  TokenPile tp2 = new TokenPile(ONYX);
  TokenPile tp3 = new TokenPile(EMERALD);
  TokenPile tp4 = new TokenPile(RUBY);
  TokenPile tp5 = new TokenPile(SAPPHIRE);

  List<TokenPile> ltp = new ArrayList<>();

  Noble anoble = new Noble(cost3);
  Noble anoble2 = new Noble(cost1);
  TradingPostSlot tradingSlot = new TradingPostSlot(true,GOLD_TOKENS_WORTH_2_GEMS_SAME_COL,cost1);



  public UserInventoryTest() {
    tp1.addToken(token1); tp1.addToken(token2);
    tp2.addToken(token3); tp2.addToken(token4);
    ltp.add(tp1); ltp.add(tp2); ltp.add(tp3); ltp.add(tp4); ltp.add(tp5);
    this.uinv = new UserInventory(ltp,aPlayer,coat);
  }


  @Test
  void getPrestigeWon() {
    assertEquals(0,uinv.getPrestigeWon(),"");
  }

  @Test
  void addTokens() {
    uinv.addTokens(token1);
    assertEquals(token1.getType(),uinv.getTokenPiles().get(DIAMOND).getType(),"");
  }

  @Test
  void removeTokenByTokenType() {
    uinv.addTokens(token1);
    Token rmToken = new Token(DIAMOND);
    assertEquals(
      rmToken.getType(),
      uinv.removeTokenByTokenType(DIAMOND).getType(),"");
  }

  @Test
  void reservedCardCount() {
    card1.setCardStatus(NONE);
    uinv.reservedCardCount();
    uinv.addReservedCard(card1);
    assertEquals(
      1,
      uinv.getCards().size(),
      "");

  }

  @Test
  void purchasedCardCount() {
    card1.setCardStatus(NONE);
    uinv.purchaseCard(card1);
    assertEquals(
      1,
      uinv.purchasedCardCount(),
      "");

  }

  @Test
  void getUnpairedSpiceCard() {
    uinv.addCascadeLevelOne(oCard1);
    assertEquals(
      oCard1,
      uinv.getUnpairedSpiceCard(),
      "");
  }

  @Test
  void hasCardReserved() {
    card1.setCardStatus(NONE);
    uinv.addReservedCard(card1);
    assertTrue(uinv.hasCardReserved(card1),"");
  }

  @Test
  void hasCard() {
    oCard1.setCardStatus(PURCHASED);
    uinv.addCascadeLevelOne(oCard1);
    assertTrue(uinv.hasCard(oCard1),"");
  }

  @Test
  void hasCardsReserved() {
    card1.setCardStatus(NONE);
    uinv.addReservedCard(card1);
    assertTrue(uinv.hasCardsReserved(),"");
  }

  @Test
  void canAffordCard() {
    card2.setCardStatus(NONE);
    assertTrue(uinv.canAffordCard(card2),"");
  }

  @Test
  void addReservedCard() {
    card1.setCardStatus(NONE);
    uinv.addReservedCard(card1);
    assertTrue(uinv.hasCardsReserved(),"");
  }

  @Test
  void addReservedNoble() {
    uinv.addReservedNoble(anoble);
    assertEquals(1,uinv.getNobles().size(),"");
  }

  @Test
  void addCascadeLevelOne() {
    uinv.addCascadeLevelOne(oCard1);
    assertEquals(
      oCard1.getCardCost(),
      uinv.getCards().get(0).getCardCost(),
      "");
  }

  @Test
  void addCascadeLevelTwo() {
    uinv.addCascadeLevelTwo(oCard2);
    assertEquals(
      oCard2.getCardCost(),
      uinv.getCards().get(0).getCardCost(),
      "");
  }

  @Test
  void discardByBonusType() {

  }

  @Test
  void purchaseCard() {
    uinv.purchaseCard(card1);
    assertTrue(
      uinv.hasCard(card1), "");
  }

  @Test
  void canBeVisitedByNoble() {
    card1.setCardStatus(NONE);
    card2.setCardStatus(NONE);
    uinv.addTokens(token1,token2);
    uinv.addTokens(token3,token4);
    uinv.purchaseCard(card1);
    uinv.addTokens(token3,token4,token5);
    uinv.purchaseCard(card2);
    anoble.setStatus(NobleStatus.ON_BOARD);
    assertTrue(uinv.canBeVisitedByNoble(anoble));
    assertFalse(uinv.canBeVisitedByNoble(anoble2));
  }

  @Test
  void receiveVisitFrom() {
    uinv.receiveVisitFrom(anoble);
    assertEquals(anoble,uinv.getNobles().get(0),"");

  }

  @Test
  void tokenCount() {
    assertEquals(4,uinv.tokenCount(),"");
  }

  @Test
  void getPlayer() {
    assertEquals(aPlayer,uinv.getPlayer(),"");
  }

  @Test
  void addPile() {
    uinv.addPile(tp5);
    assertEquals(tp5,uinv.getTokenPiles().remove(tp5.getType()));
  }

  @Test
  void addPower() {
    uinv.addPower(GAIN_5_PRESTIGE);
    assertEquals(GAIN_5_PRESTIGE,uinv.getPowers().get(0));
  }

  @Test
  void canReceivePower() {
    uinv.addPower(GAIN_5_PRESTIGE);
    assertFalse(uinv.canReceivePower(tradingSlot));
  }

  @Test
  void iterator() {
    uinv.addTokens(token3,token4, token5);
    uinv.purchaseCard(card1);
    uinv.purchaseCard(card2);
    Iterator<Card> iter = uinv.iterator();
    assertTrue(iter.hasNext());
  }

  @Test
  void getCards() {
    uinv.purchaseCard(card1);
    uinv.purchaseCard(card2);
    assertEquals(2,uinv.getCards().size(),"");
  }

  @Test
  void getTokenPiles() {
    uinv.addPile(tp1);
    assertEquals(2,uinv.getTokenPiles().get(DIAMOND).getSize(),"");
  }

  @Test
  void getNobles() {
    uinv.receiveVisitFrom(anoble);
    assertTrue(uinv.getNobles().contains(anoble));
  }

  @Test
  void getPowers() {
    uinv.addPower(GAIN_5_PRESTIGE);
    assertTrue(uinv.getPowers().contains(GAIN_5_PRESTIGE));
  }

  @Test
  void getCoatOfArmsPile() {
    assertEquals(RED,uinv.getCoatOfArmsPile().getType(),"");
  }

  @Test
  void purchasedCardCountByType() {
    uinv.purchaseCard(card1);
    assertEquals(1,uinv.purchasedCardCountByType(DIAMOND),"");
  }
}