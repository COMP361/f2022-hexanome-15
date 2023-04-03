package ca.mcgill.splendorserver.model.userinventory;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cards.OrientCard;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsPile;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static ca.mcgill.splendorserver.model.cards.DeckType.*;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.*;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.BLUE;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static ca.mcgill.splendorserver.model.tradingposts.Power.*;
import static org.junit.jupiter.api.Assertions.*;

class UserInventoryTest {
  private UserInventory uinv;
  private CardCost cost;
  private OrientCard oCard1;
  private Card card1;

  private Noble anoble;

  private PlayerWrapper aPlayer;

  @BeforeEach
  void setUp() {
    cost = new CardCost(1,0,0,0,0);
    oCard1 = new OrientCard(1,3, null, ORIENT1, ZERO, cost,true,
      new ArrayList<>(List.of(Action.PAIR_SPICE_CARD)));
    card1 = new Card(1,1, DIAMOND, BASE1, ONE, cost);
    anoble = new Noble(0, new CardCost(1,0,0,0,0));
    aPlayer = PlayerWrapper.newPlayerWrapper("Slava");
    uinv = new UserInventory(aPlayer, Optional.ofNullable(RED));
  }

  @Test
  void createUserInventoryNoTradingPosts() {
    UserInventory userInventory = new UserInventory(aPlayer, Optional.empty());
    assertEquals(null, userInventory.getCoatOfArmsPile());
  }
  @Test
  void getPrestigeWon() {
    assertEquals(0, uinv.getPrestigeWon());
  }

  @Test
  void getCards() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    assertTrue(uinv.getCards().contains(card1));
  }

  @Test
  void getPowers() {
    TradingPostSlot tradingSlot1 = new TradingPostSlot(0, false,
      GAIN_1_PRESTIGE_FOR_EVERY_PLACED_COAT_OF_ARMS, cost);
    TradingPostSlot tradingSlot2 = new TradingPostSlot(0, false, GAIN_5_PRESTIGE, cost);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.addPower(tradingSlot1.getPower());
    uinv.addPower(tradingSlot2.getPower());
    assertTrue(uinv.getPowers().contains(GAIN_1_PRESTIGE_FOR_EVERY_PLACED_COAT_OF_ARMS)
                 && uinv.getPowers().contains(GAIN_5_PRESTIGE));
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
  void addPurchasedCard() {
    uinv.addToken(new Token(DIAMOND));
    uinv.addPurchasedCard(card1);
    assertTrue(uinv.hasCard(card1));
  }

  @Test
  void getUnpairedSpiceCard() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(oCard1);
    assertEquals(oCard1, uinv.getUnpairedSpiceCard());
  }

  @Test
  void getUnpairedSpiceCardNull() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    assertEquals(null, uinv.getUnpairedSpiceCard());
  }

  @Test
  void hasCardReserved() {
    uinv.addReservedCard(card1);
    assertTrue(uinv.hasCardReserved(card1));
  }

  @Test
  void purchaseCardGold() {
    uinv.addToken(new Token(GOLD));
    uinv.purchaseCard(card1);
    assertTrue(uinv.hasCard(card1));
    assertEquals(0, uinv.getTokenPiles().get(GOLD).getSize());
  }

  @Test
  void purchaseCardGold2() {
    uinv.addToken(new Token(GOLD));
    uinv.addToken(new Token(DIAMOND));
    uinv.addToken(new Token(DIAMOND));
    CardCost cost2 = new CardCost(3,0,0,0,0);
    Card card2 = new Card(1,1, DIAMOND, BASE1, ONE, cost2);
    uinv.purchaseCard(card2);
    assertTrue(uinv.hasCard(card2));
    assertEquals(0, uinv.getTokenPiles().get(GOLD).getSize());
    assertEquals(0, uinv.getTokenPiles().get(DIAMOND).getSize());
  }

  @Test
  void purchaseCardGoldCard() {
    OrientCard goldCard = new OrientCard(1,0, GOLD, ORIENT1, TWO, cost,false,
      new ArrayList<>());
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(goldCard);
    uinv.purchaseCard(card1);
    assertFalse(uinv.hasCard(goldCard));
  }

  @Test
  void purchaseCardGoldCardAndPower() {
    TradingPostSlot tradingSlot = new TradingPostSlot(0, false, GOLD_TOKENS_WORTH_2_GEMS_SAME_COL, cost);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.addPower(tradingSlot.getPower());
    OrientCard goldCard = new OrientCard(1,0, GOLD, ORIENT1, TWO, cost,false,
      new ArrayList<>());
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(goldCard);
    CardCost cost2 = new CardCost(0, 1, 2, 0, 0);
    Card card2 = new Card(1,1, DIAMOND, BASE1, ONE, cost2);
    uinv.purchaseCard(card2);
    assertFalse(uinv.hasCard(goldCard));
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
  void canAffordCardDoubleGoldPower() {
    TradingPostSlot tradingSlot = new TradingPostSlot(0, false, GOLD_TOKENS_WORTH_2_GEMS_SAME_COL, cost);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.addPower(tradingSlot.getPower());
    uinv.addToken(new Token(GOLD));
    CardCost cost2 = new CardCost(0, 0, 2, 0, 0);
    Card card2 = new Card(1,1, DIAMOND, BASE1, ONE, cost2);
    assertTrue(uinv.canAffordCard(card2));
  }

  @Test
  void cannotAffordCard() {
    assertFalse(uinv.canAffordCard(card1));
  }

  @Test
  void canAffordOrientCard() {
    uinv.addToken(new Token(DIAMOND));
    OrientCard oCard2 = new OrientCard(1,3, null, ORIENT1, ZERO, cost,false,
      new ArrayList<>(List.of(Action.RESERVE_NOBLE)));
    assertTrue(uinv.canAffordCard(oCard2));
  }

  @Test
  void canAffordSpiceCard() {
    uinv.addToken(new Token(DIAMOND));
    assertFalse(uinv.canAffordCard(oCard1));
  }

  @Test
  void canAffordSpiceOnlyGoldCards() {
    OrientCard goldCard = new OrientCard(1,0, GOLD, ORIENT1, TWO, cost,false,
      new ArrayList<>());
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(goldCard);
    assertFalse(uinv.canAffordCard(oCard1));
  }

  @Test
  void canAffordDiscardWhiteCard() {
    uinv.addToken(new Token(DIAMOND));
    OrientCard oCard2 = new OrientCard(1,3, null, ORIENT1, ZERO, cost,false,
      new ArrayList<>(List.of(Action.DISCARD_FIRST_WHITE_CARD, Action.DISCARD_SECOND_WHITE_CARD)));
    assertFalse(uinv.canAffordCard(oCard2));
  }

  @Test
  void canAffordDiscardBlueCard() {
    uinv.addToken(new Token(DIAMOND));
    OrientCard oCard2 = new OrientCard(1,3, null, ORIENT1, ZERO, cost,false,
      new ArrayList<>(List.of(Action.DISCARD_FIRST_BLUE_CARD, Action.DISCARD_SECOND_BLUE_CARD)));
    assertFalse(uinv.canAffordCard(oCard2));
  }

  @Test
  void canAffordDiscardGreenCard() {
    uinv.addToken(new Token(DIAMOND));
    OrientCard oCard2 = new OrientCard(1,3, null, ORIENT1, ZERO, cost,false,
      new ArrayList<>(List.of(Action.DISCARD_FIRST_GREEN_CARD, Action.DISCARD_SECOND_GREEN_CARD)));
    assertFalse(uinv.canAffordCard(oCard2));
  }

  @Test
  void canAffordDiscardRedCard() {
    uinv.addToken(new Token(DIAMOND));
    OrientCard oCard2 = new OrientCard(1,3, null, ORIENT1, ZERO, cost,false,
      new ArrayList<>(List.of(Action.DISCARD_FIRST_RED_CARD, Action.DISCARD_SECOND_RED_CARD)));
    assertFalse(uinv.canAffordCard(oCard2));
  }

  @Test
  void canAffordDiscardBlackCard() {
    uinv.addToken(new Token(DIAMOND));
    OrientCard oCard2 = new OrientCard(1,3, null, ORIENT1, ZERO, cost,false,
      new ArrayList<>(List.of(Action.DISCARD_FIRST_BLACK_CARD, Action.DISCARD_SECOND_BLACK_CARD)));
    assertFalse(uinv.canAffordCard(oCard2));
  }

  @Test
  void addReservedNoble() {
    uinv.addReservedNoble(anoble);
    assertTrue(uinv.getNobles().contains(anoble));
  }

  @Test
  void addCascadeLevelOne() {
    uinv.addCascadeLevelOne(oCard1);
    assertTrue(uinv.hasCard(oCard1));
  }

  @Test
  void addCascadeLevelTwo() {
    OrientCard oCard2 = new OrientCard(1,3, ONYX, ORIENT2, ZERO, cost,false, new ArrayList<>());
    uinv.addCascadeLevelTwo(oCard2);
    assertTrue(uinv.hasCard(oCard2));
  }

  @Test
  void canBeVisitedByNoble() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    assertTrue(uinv.canBeVisitedByNoble(anoble));
  }

  @Test
  void cannotBeVisitedByNoble() {
    assertFalse(uinv.canBeVisitedByNoble(anoble));
  }

  @Test
  void cannotBeVisitedByNobleInInventory() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.receiveVisitFrom(anoble);
    assertFalse(uinv.canBeVisitedByNoble(anoble));
  }

  @Test
  void canBeVisitedByNobleReservedByAnotherPlayer() {
    PlayerWrapper player = PlayerWrapper.newPlayerWrapper("Sofia");
    UserInventory userInventory = new UserInventory(player, Optional.ofNullable(BLUE));
    uinv.addReservedNoble(anoble);
    assertFalse(userInventory.canBeVisitedByNoble(anoble));
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
    TradingPostSlot tradingSlot = new TradingPostSlot(0, false, GAIN_5_PRESTIGE, cost);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.addPower(tradingSlot.getPower());
    assertTrue(uinv.hasPower(GAIN_5_PRESTIGE));
  }

  @Test
  void canReceivePower() {
    TradingPostSlot tradingSlot = new TradingPostSlot(0, false, GAIN_5_PRESTIGE, cost);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    assertTrue(uinv.canReceivePower(tradingSlot));
  }

  @Test
  void cannotReceivePower() {
    TradingPostSlot tradingSlot = new TradingPostSlot(0, false, GAIN_5_PRESTIGE, cost);
    assertFalse(uinv.canReceivePower(tradingSlot));
  }

  @Test
  void cannotReceivePowerNoble() {
    TradingPostSlot tradingSlot = new TradingPostSlot(0, true, GAIN_5_PRESTIGE, cost);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    assertFalse(uinv.canReceivePower(tradingSlot));
  }

  @Test
  void cannotReceivePowerInInventory() {
    TradingPostSlot tradingSlot = new TradingPostSlot(0, false, GAIN_5_PRESTIGE, cost);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.addPower(tradingSlot.getPower());
    assertFalse(uinv.canReceivePower(tradingSlot));
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
    assertEquals(1, uinv.tokenBonusAmountByType(DIAMOND));
  }

  @Test
  void removePower1() {
    TradingPostSlot tradingSlot1 = new TradingPostSlot(0, false, GAIN_5_PRESTIGE, cost);
    TradingPostSlot tradingSlot2 =
        new TradingPostSlot(1, false, GAIN_1_PRESTIGE_FOR_EVERY_PLACED_COAT_OF_ARMS, cost);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(oCard1);
    uinv.addPower(tradingSlot2.getPower());
    uinv.addPower(tradingSlot1.getPower());
    uinv.removePower(tradingSlot1.getPower());
    assertFalse(uinv.hasPower(GAIN_5_PRESTIGE));
  }

  @Test
  void removePower2() {
    TradingPostSlot tradingSlot =
      new TradingPostSlot(1, false, GAIN_1_PRESTIGE_FOR_EVERY_PLACED_COAT_OF_ARMS, cost);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.addPower(tradingSlot.getPower());
    uinv.removePower(tradingSlot.getPower());
    assertFalse(uinv.hasPower(GAIN_1_PRESTIGE_FOR_EVERY_PLACED_COAT_OF_ARMS));
  }

  @Test
  void removeNoble() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.receiveVisitFrom(anoble);
    uinv.removeNoble(anoble);
    assertEquals(0, uinv.getNobles().size());
  }

  @Test
  void getNumSpiceCardsByType() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(oCard1);
    oCard1.pairWithCard(card1);
    assertEquals(1, uinv.getNumSpiceCardsByType(DIAMOND));
  }

  @Test
  void discardCard() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.discardCard(card1);
    assertFalse(uinv.hasCard(card1));
  }

  @Test
  void discardSpiceCard() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(oCard1);
    oCard1.pairWithCard(card1);
    uinv.discardSpiceCard(oCard1.getTokenBonusType());
    assertFalse(uinv.hasCard(oCard1));
  }

  @Test
  void addCity() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    City city = new City(1, 0,
      new CardCost(1,0,0,0,0),0);
    uinv.addCity(city);
    assertTrue(uinv.getCities().contains(city));
  }

  @Test
  void hasCity() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    City city = new City(1, 0,
      new CardCost(1,0,0,0,0),0);
    uinv.addCity(city);
    assertTrue(uinv.hasCity(city));
  }

  @Test
  void canReceiveCity() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    City city = new City(1, 0,
      new CardCost(1,0,0,0,0),0);
    assertTrue(uinv.canReceiveCity(city));
  }

  @Test
  void cannotReceiveCity() {
    City city = new City(1, 0,
      new CardCost(1,1,1,1,1),0);
    assertFalse(uinv.canReceiveCity(city));
  }

  @Test
  void cannotReceiveCityInInventory() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    City city = new City(1, 0,
      new CardCost(1,0,0,0,0),0);
    uinv.addCity(city);
    assertFalse(uinv.canReceiveCity(city));
  }

  @Test
  void cannotReceiveCityPrestige() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    City city = new City(1, 10,
      new CardCost(0,0,0,0,0),0);
    assertFalse(uinv.canReceiveCity(city));
  }

  @Test
  void canReceiveCitySameCards() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    City city = new City(1, 0,
      new CardCost(0,0,0,0,0),1);
    assertTrue(uinv.canReceiveCity(city));
  }

  @Test
  void cannotReceiveCitySameCards() {
    uinv.addToken(new Token(DIAMOND));
    uinv.purchaseCard(card1);
    City city = new City(1, 0,
      new CardCost(1,0,0,0,0),2);
    assertFalse(uinv.canReceiveCity(city));
  }
}