package ca.mcgill.splendorserver.model.userinventory;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cards.OrientCard;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ca.mcgill.splendorserver.model.cards.CardStatus.*;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE1;
import static ca.mcgill.splendorserver.model.cards.DeckType.ORIENT1;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.TWO;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ZERO;
import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static ca.mcgill.splendorserver.model.tokens.TokenType.ONYX;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static org.junit.jupiter.api.Assertions.*;

class UserInventoryTest {
  UserInventory uinv;
  List<Action> acts = new ArrayList<>();
  CardCost oCost1 = new CardCost(2,0,0,0,1);
  OrientCard oCard1 = new OrientCard(1,3,ONYX,ORIENT1,ZERO,oCost1,true,acts);
  CardCost cost1 = new CardCost(2,0,0,0,0);
  CardCost cost2 = new CardCost(0,0,0,0,2);
  Card card1 = new Card(1,2,DIAMOND,BASE1,ZERO,cost1);
  Card card2 = new Card(1,2,DIAMOND,BASE1,ZERO,cost2);
  Token token1 = new Token(DIAMOND);
  Token token2 = new Token(DIAMOND);
  Token token3 = new Token(ONYX);
  Token token4 = new Token(ONYX);
  PlayerWrapper aPlayer = PlayerWrapper.newPlayerWrapper("Slava");
  Optional<CoatOfArmsType> coat = Optional.of(RED);
  TokenPile tp1 = new TokenPile(DIAMOND);
  TokenPile tp2 = new TokenPile(ONYX);

  List<TokenPile> ltp = new ArrayList<>();

  public UserInventoryTest() {
    tp1.addToken(token1); tp1.addToken(token2);
    tp2.addToken(token3); tp2.addToken(token4);
    ltp.add(tp1); ltp.add(tp2);
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
    uinv.purchaseCard(card2);
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

  }

  @Test
  void addCascadeLevelOne() {
  }

  @Test
  void addCascadeLevelTwo() {
  }

  @Test
  void discardByBonusType() {
  }

  @Test
  void purchaseCard() {
  }

  @Test
  void canBeVisitedByNoble() {
  }

  @Test
  void canBeVisitedByNobleWithCardPurchase() {
  }

  @Test
  void receiveVisitFrom() {
  }

  @Test
  void tokenCount() {
  }

  @Test
  void getPlayer() {
  }

  @Test
  void addPile() {
  }

  @Test
  void addPower() {
  }

  @Test
  void canReceivePower() {
  }

  @Test
  void iterator() {
  }

  @Test
  void getCards() {
  }

  @Test
  void getTokenPiles() {
  }

  @Test
  void getNobles() {
  }

  @Test
  void getPowers() {
  }

  @Test
  void getCoatOfArmsPile() {
  }

  @Test
  void purchasedCardCountByType() {
  }
}