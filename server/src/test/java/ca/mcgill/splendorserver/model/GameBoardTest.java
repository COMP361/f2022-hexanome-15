package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.OrientCard;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArms;
import ca.mcgill.splendorserver.model.tradingposts.Power;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ca.mcgill.splendorserver.model.cards.DeckType.*;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.*;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class GameBoardTest {
  private GameBoard gb;
  private PlayerWrapper sofia;
  private PlayerWrapper jeff;

  private List<UserInventory> inventories;
  private List<Deck> decks;
  private List<Card> cards = new ArrayList<>();
  private List <TokenPile> tokenPiles;
  private List<Noble> nobles = new ArrayList<>();
  private List<TradingPostSlot> tradingPosts = new ArrayList<>();
  private List<City> cities = new ArrayList<>();

  @BeforeEach
  void setUp() {
    sofia = PlayerWrapper.newPlayerWrapper("Sofia");
    jeff = PlayerWrapper.newPlayerWrapper("Jeff");
    Player player1 = new Player("Sofia", "purple");
    Player player2 = new Player("Jeff", "blue");
    List<Player> playerList = new ArrayList<>();
    playerList.add(player1);
    playerList.add(player2);
    List<PlayerWrapper> players = new ArrayList<>();
    players.add(sofia);
    players.add(jeff);
    SessionInfo sessionInfo = new SessionInfo("SplendorOrientTradingPosts", playerList, players, sofia,"1L");
    SplendorGame game = new SplendorGame(sessionInfo, 1L);
    inventories = game.getBoard().getInventories();
    decks = game.getBoard().getDecks();
    tokenPiles = game.getBoard().getTokenPiles().values().stream().toList();

    //Setting up the gameboard
    cards.add(new Card(0, 1, DIAMOND, BASE1, ONE,
      new CardCost(1, 0, 0, 0, 0)));
    cards.add(new OrientCard(1, 0, null, ORIENT1, ZERO,
      new CardCost(0, 0, 0, 0, 0), true,
      new ArrayList<>(List.of(Action.PAIR_SPICE_CARD))));
    cards.add(new OrientCard(2, 1, DIAMOND, ORIENT2, ONE,
      new CardCost(1, 0, 0, 0, 0), false,
      new ArrayList<>(List.of(Action.RESERVE_NOBLE))));
    cards.add(new OrientCard(3, 0, null, ORIENT2, ZERO,
      new CardCost(1, 0, 0, 0, 1), true,
      new ArrayList<>(List.of(Action.PAIR_SPICE_CARD, Action.CASCADE_LEVEL_1))));
    cards.add(new OrientCard(4, 0, DIAMOND, ORIENT3, ONE,
      new CardCost(1, 0, 0, 0, 0), false,
      new ArrayList<>(List.of(Action.CASCADE_LEVEL_2))));
    cards.add(new OrientCard(5, 3, DIAMOND, ORIENT3, ONE,
      new CardCost(0, 0, 0, 0, 0), false,
      new ArrayList<>(List.of(Action.DISCARD_FIRST_BLACK_CARD,
        Action.DISCARD_SECOND_BLACK_CARD))));
    cards.add(new OrientCard(6, 3, SAPPHIRE, ORIENT3, ONE,
      new CardCost(0, 0, 0, 0, 0), false,
      new ArrayList<>(List.of(Action.DISCARD_FIRST_WHITE_CARD,
        Action.DISCARD_SECOND_WHITE_CARD))));
    cards.add(new OrientCard(7, 3, EMERALD, ORIENT3, ONE,
      new CardCost(0, 0, 0, 0, 0), false,
      new ArrayList<>(List.of(Action.DISCARD_FIRST_BLUE_CARD,
        Action.DISCARD_SECOND_BLUE_CARD))));
    cards.add(new OrientCard(8, 3, RUBY, ORIENT3, ONE,
      new CardCost(0, 0, 0, 0, 0), false,
      new ArrayList<>(List.of(Action.DISCARD_FIRST_GREEN_CARD,
        Action.DISCARD_SECOND_GREEN_CARD))));
    cards.add(new OrientCard(9, 3, ONYX, ORIENT3, ONE,
      new CardCost(0, 0, 0, 0, 0), false,
      new ArrayList<>(List.of(Action.DISCARD_FIRST_RED_CARD,
        Action.DISCARD_SECOND_RED_CARD))));
    cards.add(new OrientCard(10, 0, GOLD, ORIENT1, TWO,
      new CardCost(1, 0, 0, 0, 0), false,
      new ArrayList<>()));
    cards.add(new OrientCard(11, 0, SAPPHIRE, ORIENT2, TWO,
      new CardCost(1, 0, 0, 0, 4), false,
      new ArrayList<>()));
    cards.add(new Card(12, 1, DIAMOND, BASE1, ONE,
      new CardCost(1, 0, 0, 0, 0)));
    cards.add(new Card(13, 1, DIAMOND, BASE1, ONE,
      new CardCost(1, 0, 0, 0, 0)));
    cards.add(new Card(14, 1, EMERALD, BASE1, ONE,
      new CardCost(1, 0, 0, 0, 0)));
    cards.add(new Card(15, 1, EMERALD, BASE1, ONE,
      new CardCost(1, 0, 0, 0, 0)));
    cards.add(new Card(16, 1, RUBY, BASE1, ONE,
      new CardCost(1, 0, 0, 0, 0)));
    cards.add(new Card(17, 1, RUBY, BASE1, ONE,
      new CardCost(1, 0, 0, 0, 0)));
    cards.add(new Card(18, 1, ONYX, BASE1, ONE,
      new CardCost(1, 0, 0, 0, 0)));
    cards.add(new Card(19, 1, ONYX, BASE1, ONE,
      new CardCost(1, 0, 0, 0, 0)));
    cards.add(new Card(20, 1, SAPPHIRE, BASE1, ONE,
      new CardCost(1, 0, 0, 0, 0)));
    cards.add(new Card(21, 1, SAPPHIRE, BASE1, ONE,
      new CardCost(1, 0, 0, 0, 0)));
    cards.add(new OrientCard(22, 0, null, ORIENT1, ZERO,
      new CardCost(0, 0, 0, 0, 0), true,
      new ArrayList<>(List.of(Action.PAIR_SPICE_CARD))));

    Noble noble1 = new Noble(0, new CardCost(0, 0, 0, 2, 0));
    Noble noble2 = new Noble(1, new CardCost(0, 0, 0, 2, 0));
    Noble noble3 = new Noble(2, new CardCost(0, 3, 0, 0, 0));
    nobles.add(noble1);
    nobles.add(noble2);
    nobles.add(noble3);

    TradingPostSlot tradingPost1 = new TradingPostSlot(0, false, Power.PURCHASE_CARD_TAKE_TOKEN,
      new CardCost(0, 0, 0, 0, 2));
    tradingPosts.add(tradingPost1);

    City city1 = new City(0, 2,
      new CardCost(0, 0, 2, 0, 0), 0);
    City city2 = new City(1, 2,
      new CardCost(0, 0, 2, 0, 0), 0);
    City city3 = new City(2, 2,
      new CardCost(0, 0, 1, 1, 1), 0);
    cities.add(city1);
    cities.add(city2);
    cities.add(city3);

    gb = new GameBoard(inventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);
  }

  @Test
  void applyMovePlayerNotInGame() {
    PlayerWrapper playerWrapper = PlayerWrapper.newPlayerWrapper("John");
    Move move = new Move(Action.PURCHASE_DEV, cards.get(0), playerWrapper, null,
      null, null, null, null);
    assertThrows(
      IllegalArgumentException.class,
      () -> gb.applyMove(move, playerWrapper),
      "player (John) wasn't found in this current game board");
  }

  @Test
  void applyTakeTwoTokensMove() {
    Move move = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    Action action1 = gb.applyMove(move, sofia);
    assertEquals(Action.TAKE_TOKEN, action1);
    Action action2 = gb.applyMove(move, sofia);
    assertNull(action2);
    assertEquals(2, gb.getInventoryByPlayerName("Sofia").get().getTokenPiles().get(DIAMOND).getSize());
  }

  @Test
  void applyTakeThreeTokensMove() {
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    Action action1 = gb.applyMove(move1, sofia);
    assertEquals(Action.TAKE_TOKEN, action1);
    Move move2 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, SAPPHIRE, null);
    Action action2 = gb.applyMove(move2, sofia);
    assertEquals(Action.TAKE_TOKEN, action2);
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, EMERALD, null);
    Action action3 = gb.applyMove(move3, sofia);
    assertNull(action3);
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    assertEquals(1, inventory.getTokenPiles().get(DIAMOND).getSize());
    assertEquals(1, inventory.getTokenPiles().get(SAPPHIRE).getSize());
    assertEquals(1, inventory.getTokenPiles().get(EMERALD).getSize());
  }

  @Test
  void applyReserveMove() {
    Card reservedCard = cards.get(0);
    Move move = new Move(Action.RESERVE_DEV, reservedCard, sofia, null,
      null, null, null, null);
    Action action = gb.applyMove(move, sofia);
    assertNull(action);
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    assertTrue(inventory.hasCardReserved(reservedCard));
    assertEquals(1, inventory.getTokenPiles().get(GOLD).getSize());
  }

  @Test
  void applyReserveFromDeckMove() {
    Card reservedCard = decks.get(0).getCards().get(0);
    Move move = new Move(Action.RESERVE_DEV, null, sofia, BASE1,
      null, null, null, null);
    Action action = gb.applyMove(move, sofia);
    assertNull(action);
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    assertTrue(inventory.hasCardReserved(reservedCard));
    assertEquals(1, inventory.getTokenPiles().get(GOLD).getSize());
  }

  @Test
  void applyReserveMoveNoGold() {
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();

    int numGold = gb.getTokenPiles().get(GOLD).getSize();
    for (int i = 0; i < numGold; i++) {
      Move move = new Move(Action.RESERVE_DEV, cards.get(i), sofia, null,
        null, null, null, null);
      gb.applyMove(move, sofia);
    }

    Card reservedCard = cards.get(6);
    Move move = new Move(Action.RESERVE_DEV, reservedCard, jeff, null,
      null, null, null, null);
    Action action = gb.applyMove(move, jeff);
    assertNull(action);
    assertTrue(inventory2.hasCardReserved(reservedCard));
    assertEquals(0, inventory2.getTokenPiles().get(GOLD).getSize());
  }

  @Test
  void applyPurchaseMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    Card purchasedCard = cards.get(0);
    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, purchasedCard, sofia, null,
      null, null, null, null);
    Action action = gb.applyMove(move2, sofia);
    assertNull(action);
    assertTrue(inventory.hasCard(purchasedCard));
    assertEquals(0, inventory.tokenCount());
  }

  @Test
  void applyPurchaseReservedMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    Card purchasedCard = cards.get(0);
    //Reserving the card first
    Move move1 = new Move(Action.RESERVE_DEV, purchasedCard, sofia, null,
      null, null, null, null);
    gb.applyMove(move1, sofia);

    //Taking enough tokens to afford the card
    Move move2 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move2, sofia);

    Move move3 = new Move(Action.PURCHASE_DEV, purchasedCard, sofia, null,
      null, null, null, null);
    Action action = gb.applyMove(move3, sofia);
    assertNull(action);
    assertTrue(inventory.hasCard(purchasedCard));
    assertEquals(1, inventory.tokenCount());
  }
  @Test
  void applyDiscardTwoLoseNoble() {
    UserInventory inventory1 = gb.getInventoryByPlayerName("Sofia").get();
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();
    nobles = new ArrayList<>();
    Noble noble = new Noble(0, new CardCost(0, 0, 0, 0, 2));
    nobles.add(noble);
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(18));
    ((OrientCard) cards.get(1)).pairWithCard(cards.get(18));
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(1));
    inventory1.receiveVisitFrom(noble);

    List<UserInventory> userInventories = new ArrayList<>(List.of(inventory1, inventory2));
    gb = new GameBoard(userInventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);

    Move move1 = new Move(Action.PURCHASE_DEV, cards.get(5), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move1, sofia);
    assertEquals(Action.DISCARD_SECOND_BLACK_CARD, action1);

    Move move2 = new Move(Action.DISCARD_SECOND_BLACK_CARD, cards.get(18), sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move2, sofia);

    assertEquals(0, inventory1.tokenBonusAmountByType(ONYX));
    assertFalse(inventory1.getNobles().contains(noble));
    assertNull(action2);
  }

  @Test
  void applyDiscardOneLoseNoble() {
    UserInventory inventory1 = gb.getInventoryByPlayerName("Sofia").get();
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();
    nobles = new ArrayList<>();
    Noble noble = new Noble(0, new CardCost(0, 0, 0, 0, 1));
    nobles.add(noble);

    List<UserInventory> userInventories = new ArrayList<>(List.of(inventory1, inventory2));
    gb = new GameBoard(userInventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Card card1 = cards.get(18);
    Move move2 = new Move(Action.PURCHASE_DEV, card1, sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    gb.getEndOfTurnActions(move2, inventory1);

    //Taking enough tokens to afford the card
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move3, sofia);

    Card card2 = cards.get(19);
    Move move4 = new Move(Action.PURCHASE_DEV, card2, sofia, null,
      null, null, null, null);
    gb.applyMove(move4, sofia);

    Move move5 = new Move(Action.PURCHASE_DEV, cards.get(5), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move5, sofia);
    assertEquals(Action.DISCARD_FIRST_BLACK_CARD, action1);

    Move move6 = new Move(Action.DISCARD_FIRST_BLACK_CARD, card1, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move6, sofia);
    assertEquals(Action.DISCARD_SECOND_BLACK_CARD, action2);

    Move move7 = new Move(Action.DISCARD_SECOND_BLACK_CARD, card2, sofia, null,
      null, null, null, null);
    Action action3 = gb.applyMove(move7, sofia);
    assertEquals(0, inventory1.tokenBonusAmountByType(ONYX));
    assertNull(action3);
  }

  @Test
  void applyDiscardTwoLosePower() {
    UserInventory inventory1 = gb.getInventoryByPlayerName("Sofia").get();
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();
    tradingPosts = new ArrayList<>();
    TradingPostSlot tradingPost = new TradingPostSlot(0, false, Power.PURCHASE_CARD_TAKE_TOKEN,
      new CardCost(0, 0, 0, 0, 2));
    tradingPosts = new ArrayList<>();
    tradingPosts.add(tradingPost);
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(18));
    ((OrientCard) cards.get(1)).pairWithCard(cards.get(18));
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(1));
    inventory1.addPower(tradingPost.getPower());
    tradingPost.addCoatOfArms(new CoatOfArms(inventory1.getCoatOfArmsPile().getType()));

    List<UserInventory> userInventories = new ArrayList<>(List.of(inventory1, inventory2));
    gb = new GameBoard(userInventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);

    Move move1 = new Move(Action.PURCHASE_DEV, cards.get(5), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move1, sofia);
    assertEquals(Action.DISCARD_SECOND_BLACK_CARD, action1);

    Move move2 = new Move(Action.DISCARD_SECOND_BLACK_CARD, cards.get(18), sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move2, sofia);

    assertEquals(0, inventory1.tokenBonusAmountByType(ONYX));
    assertFalse(inventory1.hasPower(tradingPost.getPower()));
    assertNull(action2);
  }

  @Test
  void applyDiscardOneLosePower() {
    UserInventory inventory1 = gb.getInventoryByPlayerName("Sofia").get();
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();
    tradingPosts = new ArrayList<>();
    TradingPostSlot tradingPost = new TradingPostSlot(0, false, Power.PURCHASE_CARD_TAKE_TOKEN,
      new CardCost(0, 0, 0, 0, 1));
    tradingPosts = new ArrayList<>();
    tradingPosts.add(tradingPost);

    List<UserInventory> userInventories = new ArrayList<>(List.of(inventory1, inventory2));
    gb = new GameBoard(userInventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);

    Card card1 = cards.get(18);
    Card card2 = cards.get(19);

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, card1, sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    gb.getEndOfTurnActions(move2, inventory1);

    //Taking enough tokens to afford the card
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move3, sofia);

    Move move4 = new Move(Action.PURCHASE_DEV, card2, sofia, null,
      null, null, null, null);
    gb.applyMove(move4, sofia);

    Move move5 = new Move(Action.PURCHASE_DEV, cards.get(5), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move5, sofia);
    assertEquals(Action.DISCARD_FIRST_BLACK_CARD, action1);

    Move move6 = new Move(Action.DISCARD_FIRST_BLACK_CARD, card1, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move6, sofia);
    assertEquals(Action.DISCARD_SECOND_BLACK_CARD, action2);

    Move move7 = new Move(Action.DISCARD_SECOND_BLACK_CARD, card2, sofia, null,
      null, null, null, null);
    Action action3 = gb.applyMove(move7, sofia);
    assertEquals(0, inventory1.tokenBonusAmountByType(ONYX));
    assertNull(action3);
  }

  @Test
  void applyDiscardTwoBlackSpiceMove() {
    UserInventory inventory1 = gb.getInventoryByPlayerName("Sofia").get();
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();
    Card card1 = cards.get(18);
    Card card2 = cards.get(1);
    Card card3 = cards.get(22);
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(card1);
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(card2);
    ((OrientCard) card2).pairWithCard(card1);
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(card3);
    ((OrientCard) card3).pairWithCard(card1);
    List<UserInventory> userInventories = new ArrayList<>(List.of(inventory1, inventory2));
    gb = new GameBoard(userInventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);

    Move move = new Move(Action.PURCHASE_DEV, cards.get(5), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move, sofia);
    assertEquals(1, inventory1.tokenBonusAmountByType(ONYX));
    assertNull(action1);
  }

  @Test
  void applyDiscardOneBlackSpiceMove() {
    UserInventory inventory1 = gb.getInventoryByPlayerName("Sofia").get();
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(18));
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(1));
    ((OrientCard) cards.get(1)).pairWithCard(cards.get(18));
    List<UserInventory> userInventories = new ArrayList<>(List.of(inventory1, inventory2));
    gb = new GameBoard(userInventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);

    Move move1 = new Move(Action.PURCHASE_DEV, cards.get(5), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move1, sofia);
    assertEquals(Action.DISCARD_SECOND_BLACK_CARD, action1);

    Move move2 = new Move(Action.DISCARD_SECOND_BLACK_CARD, cards.get(18), sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move2, sofia);

    assertEquals(0, inventory1.tokenBonusAmountByType(ONYX));
    assertNull(action2);
  }

  @Test
  void applyDiscardTwoBlackMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    Card card1 = cards.get(18);
    Card card2 = cards.get(19);

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, card1, sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    //Taking enough tokens to afford the card
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move3, sofia);

    Move move4 = new Move(Action.PURCHASE_DEV, card2, sofia, null,
      null, null, null, null);
    gb.applyMove(move4, sofia);

    Move move5 = new Move(Action.PURCHASE_DEV, cards.get(5), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move5, sofia);
    assertEquals(Action.DISCARD_FIRST_BLACK_CARD, action1);

    Move move6 = new Move(Action.DISCARD_FIRST_BLACK_CARD, card1, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move6, sofia);
    assertEquals(Action.DISCARD_SECOND_BLACK_CARD, action2);

    Move move7 = new Move(Action.DISCARD_SECOND_BLACK_CARD, card2, sofia, null,
      null, null, null, null);
    Action action3 = gb.applyMove(move7, sofia);
    assertEquals(0, inventory.tokenBonusAmountByType(ONYX));
    assertNull(action3);
  }

  @Test
  void applyDiscardTwoBlueSpiceMove() {
    UserInventory inventory1 = gb.getInventoryByPlayerName("Sofia").get();
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(20));
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(1));
    ((OrientCard) cards.get(1)).pairWithCard(cards.get(20));
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(22));
    ((OrientCard) cards.get(22)).pairWithCard(cards.get(20));
    List<UserInventory> userInventories = new ArrayList<>(List.of(inventory1, inventory2));
    gb = new GameBoard(userInventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);

    Move move = new Move(Action.PURCHASE_DEV, cards.get(7), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move, sofia);
    assertEquals(1, inventory1.tokenBonusAmountByType(SAPPHIRE));
    assertNull(action1);
  }

  @Test
  void applyDiscardOneBlueMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Card card1 = cards.get(11);
    Move move2 = new Move(Action.PURCHASE_DEV, card1, sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    Move move3 = new Move(Action.PURCHASE_DEV, cards.get(7), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move3, sofia);
    assertEquals(Action.DISCARD_FIRST_BLUE_CARD, action1);

    Move move6 = new Move(Action.DISCARD_FIRST_BLUE_CARD, card1, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move6, sofia);
    assertEquals(0, inventory.tokenBonusAmountByType(SAPPHIRE));
    assertNull(action2);
  }

  @Test
  void applyDiscardTwoBlueMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Card card1 = cards.get(20);
    Move move2 = new Move(Action.PURCHASE_DEV, card1, sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    //Taking enough tokens to afford the card
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move3, sofia);

    Card card2 = cards.get(21);
    Move move4 = new Move(Action.PURCHASE_DEV, card2, sofia, null,
      null, null, null, null);
    gb.applyMove(move4, sofia);

    Move move5 = new Move(Action.PURCHASE_DEV, cards.get(7), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move5, sofia);
    assertEquals(Action.DISCARD_FIRST_BLUE_CARD, action1);

    Move move6 = new Move(Action.DISCARD_FIRST_BLUE_CARD, card1, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move6, sofia);
    assertEquals(Action.DISCARD_SECOND_BLUE_CARD, action2);

    Move move7 = new Move(Action.DISCARD_SECOND_BLUE_CARD, card2, sofia, null,
      null, null, null, null);
    Action action3 = gb.applyMove(move7, sofia);
    assertEquals(0, inventory.tokenBonusAmountByType(SAPPHIRE));
    assertNull(action3);
  }

  @Test
  void applyDiscardTwoGreenSpiceMove() {
    UserInventory inventory1 = gb.getInventoryByPlayerName("Sofia").get();
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(14));
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(1));
    ((OrientCard) cards.get(1)).pairWithCard(cards.get(14));
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(22));
    ((OrientCard) cards.get(22)).pairWithCard(cards.get(14));
    List<UserInventory> userInventories = new ArrayList<>(List.of(inventory1, inventory2));
    gb = new GameBoard(userInventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);

    Move move = new Move(Action.PURCHASE_DEV, cards.get(8), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move, sofia);
    assertEquals(1, inventory1.tokenBonusAmountByType(EMERALD));
    assertNull(action1);
  }

  @Test
  void applyDiscardTwoGreenMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Card card1 = cards.get(14);
    Move move2 = new Move(Action.PURCHASE_DEV, card1, sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    //Taking enough tokens to afford the card
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move3, sofia);

    Card card2 = cards.get(15);
    Move move4 = new Move(Action.PURCHASE_DEV, card2, sofia, null,
      null, null, null, null);
    gb.applyMove(move4, sofia);

    Move move5 = new Move(Action.PURCHASE_DEV, cards.get(8), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move5, sofia);
    assertEquals(Action.DISCARD_FIRST_GREEN_CARD, action1);

    Move move6 = new Move(Action.DISCARD_FIRST_GREEN_CARD, card1, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move6, sofia);
    assertEquals(Action.DISCARD_SECOND_GREEN_CARD, action2);

    Move move7 = new Move(Action.DISCARD_SECOND_GREEN_CARD, card2, sofia, null,
      null, null, null, null);
    Action action3 = gb.applyMove(move7, sofia);
    assertEquals(0, inventory.tokenBonusAmountByType(EMERALD));
    assertNull(action3);
  }

  @Test
  void applyDiscardTwoRedSpiceMove() {
    UserInventory inventory1 = gb.getInventoryByPlayerName("Sofia").get();
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(16));
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(1));
    ((OrientCard) cards.get(1)).pairWithCard(cards.get(16));
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(22));
    ((OrientCard) cards.get(22)).pairWithCard(cards.get(16));
    List<UserInventory> userInventories = new ArrayList<>(List.of(inventory1, inventory2));
    gb = new GameBoard(userInventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);

    Move move = new Move(Action.PURCHASE_DEV, cards.get(9), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move, sofia);
    assertEquals(1, inventory1.tokenBonusAmountByType(RUBY));
    assertNull(action1);
  }

  @Test
  void applyDiscardTwoRedMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Card card1 = cards.get(16);
    Move move2 = new Move(Action.PURCHASE_DEV, card1, sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    //Taking enough tokens to afford the card
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move3, sofia);

    Card card2 = cards.get(17);
    Move move4 = new Move(Action.PURCHASE_DEV, card2, sofia, null,
      null, null, null, null);
    gb.applyMove(move4, sofia);

    Move move5 = new Move(Action.PURCHASE_DEV, cards.get(9), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move5, sofia);
    assertEquals(Action.DISCARD_FIRST_RED_CARD, action1);

    Move move6 = new Move(Action.DISCARD_FIRST_RED_CARD, card1, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move6, sofia);
    assertEquals(Action.DISCARD_SECOND_RED_CARD, action2);

    Move move7 = new Move(Action.DISCARD_SECOND_RED_CARD, card2, sofia, null,
      null, null, null, null);
    Action action3 = gb.applyMove(move7, sofia);
    assertEquals(0, inventory.tokenBonusAmountByType(RUBY));
    assertNull(action3);
  }

  @Test
  void applyDiscardTwoWhiteSpiceMove() {
    UserInventory inventory1 = gb.getInventoryByPlayerName("Sofia").get();
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(12));
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(1));
    ((OrientCard) cards.get(1)).pairWithCard(cards.get(12));
    inventory1.addToken(new Token(DIAMOND));
    inventory1.purchaseCard(cards.get(22));
    ((OrientCard) cards.get(22)).pairWithCard(cards.get(12));
    List<UserInventory> userInventories = new ArrayList<>(List.of(inventory1, inventory2));
    gb = new GameBoard(userInventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);

    Move move = new Move(Action.PURCHASE_DEV, cards.get(6), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move, sofia);
    assertEquals(1, inventory1.tokenBonusAmountByType(DIAMOND));
    assertNull(action1);
  }

  @Test
  void applyDiscardTwoWhiteMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Card card1 = cards.get(12);
    Move move2 = new Move(Action.PURCHASE_DEV, card1, sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    //Taking enough tokens to afford the card
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move3, sofia);

    Card card2 = cards.get(13);
    Move move4 = new Move(Action.PURCHASE_DEV, card2, sofia, null,
      null, null, null, null);
    gb.applyMove(move4, sofia);

    Move move5 = new Move(Action.PURCHASE_DEV, cards.get(6), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move5, sofia);
    assertEquals(Action.DISCARD_FIRST_WHITE_CARD, action1);

    Move move6 = new Move(Action.DISCARD_FIRST_WHITE_CARD, card1, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move6, sofia);
    assertEquals(Action.DISCARD_SECOND_WHITE_CARD, action2);

    Move move7 = new Move(Action.DISCARD_SECOND_WHITE_CARD, card2, sofia, null,
      null, null, null, null);
    Action action3 = gb.applyMove(move7, sofia);
    assertEquals(0, inventory.tokenBonusAmountByType(DIAMOND));
    assertNull(action3);
  }

  @Test
  void applyPairSpiceMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    Card firstCard = cards.get(0);

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, cards.get(0), sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    Card purchasedCard = cards.get(1);
    Move move3 = new Move(Action.PURCHASE_DEV, purchasedCard, sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move3, sofia);

    assertEquals(Action.PAIR_SPICE_CARD, action1);

    Move move4 = new Move(Action.PAIR_SPICE_CARD, firstCard, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move4, sofia);
    assertEquals(2, inventory.tokenBonusAmountByType(DIAMOND));
    assertNull(action2);
  }

  @Test
  void applyReserveNobleMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, cards.get(2), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move2, sofia);
    assertEquals(Action.RESERVE_NOBLE, action1);

    Noble noble = nobles.get(0);
    Move move3 = new Move(Action.RESERVE_NOBLE, null, sofia, null,
      noble, null, null, null);
    Action action2 = gb.applyMove(move3, sofia);
    assertEquals(noble, inventory.getNobles().get(0));
    assertNull(action2);
  }

  @Test
  void applyPairSpiceAndCascadeMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    Card firstCard = cards.get(0);

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, cards.get(0), sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    Card purchasedCard = cards.get(3);
    Move move3 = new Move(Action.PURCHASE_DEV, purchasedCard, sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move3, sofia);
    assertEquals(Action.PAIR_SPICE_CARD, action1);

    Move move4 = new Move(Action.PAIR_SPICE_CARD, firstCard, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move4, sofia);
    assertEquals(Action.CASCADE_LEVEL_1, action2);

    Card cascadeCard = cards.get(10);
    Move move5 = new Move(Action.CASCADE_LEVEL_1, cascadeCard, sofia, null,
      null, null, null, null);
    Action action3 = gb.applyMove(move5, sofia);
    assertTrue(inventory.hasCard(cascadeCard));
    assertNull(action3);
  }

  @Test
  void applyCascadeMoveAndPairSpiceMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    Card firstCard = cards.get(0);

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, cards.get(0), sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    Card purchasedCard = cards.get(3);
    Move move3 = new Move(Action.PURCHASE_DEV, purchasedCard, sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move3, sofia);
    assertEquals(Action.PAIR_SPICE_CARD, action1);

    Move move4 = new Move(Action.PAIR_SPICE_CARD, firstCard, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move4, sofia);
    assertEquals(Action.CASCADE_LEVEL_1, action2);

    Card cascadeCard = cards.get(1);
    Move move5 = new Move(Action.CASCADE_LEVEL_1, cascadeCard, sofia, null,
      null, null, null, null);
    Action action3 = gb.applyMove(move5, sofia);
    assertEquals(Action.PAIR_SPICE_CARD, action3);

    Move move6 = new Move(Action.PAIR_SPICE_CARD, firstCard, sofia, null,
      null, null, null, null);
    Action action4 = gb.applyMove(move6, sofia);
    assertNull(action4);
  }

  @Test
  void applyCascadeLevelTwoMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, cards.get(0), sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    Card purchasedCard = cards.get(4);
    Move move3 = new Move(Action.PURCHASE_DEV, purchasedCard, sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move3, sofia);
    assertEquals(Action.CASCADE_LEVEL_2, action1);

    Card cascadeLevelTwo = cards.get(11);
    Move move4 = new Move(Action.CASCADE_LEVEL_2, cascadeLevelTwo, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move4, sofia);
    assertTrue(inventory.hasCard(cascadeLevelTwo));
    assertNull(action2);
  }

  @Test
  void applyDoubleCascadeMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    Card firstCard = cards.get(0);

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, cards.get(0), sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    Card purchasedCard = cards.get(4);
    Move move3 = new Move(Action.PURCHASE_DEV, purchasedCard, sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move3, sofia);
    assertEquals(Action.CASCADE_LEVEL_2, action1);

    Card cascadeLevelTwo = cards.get(3);
    Move move4 = new Move(Action.CASCADE_LEVEL_2, cascadeLevelTwo, sofia, null,
      null, null, null, null);
    Action action2 = gb.applyMove(move4, sofia);
    assertEquals(Action.PAIR_SPICE_CARD, action2);

    Move move5 = new Move(Action.PAIR_SPICE_CARD, firstCard, sofia, null,
      null, null, null, null);
    Action action3 = gb.applyMove(move5, sofia);
    assertEquals(Action.CASCADE_LEVEL_1, action3);

    Card cascadeLevelOne = cards.get(10);
    Move move6 = new Move(Action.CASCADE_LEVEL_1, cascadeLevelOne, sofia, null,
      null, null, null, null);
    Action action4 = gb.applyMove(move6, sofia);
    assertTrue(inventory.hasCard(cascadeLevelOne));
    assertNull(action4);
  }

  @Test
  void getAndApplyReturnTokenMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    Move move2 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, SAPPHIRE, null);
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, EMERALD, null);
    for (int i = 0; i < 4; i++) {
      gb.applyMove(move1, sofia);
    }
    for (int i = 0; i < 4; i++) {
      gb.applyMove(move2, sofia);
    }
    for (int i = 0; i < 4; i++) {
      gb.applyMove(move3, sofia);
    }
    Action action1 = gb.getEndOfTurnActions(move3, inventory);
    assertEquals(Action.RET_TOKEN, action1);

    Move move4 = new Move(Action.RET_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    Action action2 = gb.applyMove(move4, sofia);
    assertEquals(Action.RET_TOKEN, action2);

    Action action3 = gb.applyMove(move4, sofia);
    assertEquals(10, inventory.tokenCount());
    assertNull(action3);
  }

  @Test
  void getAndApplyReceiveNobleMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
        null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, cards.get(16), sofia, null,
        null, null, null, null);
    gb.applyMove(move2, sofia);

    //Taking enough tokens to afford the card
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move3, sofia);

    Move move4 = new Move(Action.PURCHASE_DEV, cards.get(17), sofia, null,
      null, null, null, null);
    gb.applyMove(move4, sofia);

    Action action1 = gb.getEndOfTurnActions(move4, inventory);
    assertEquals(Action.RECEIVE_NOBLE, action1);

    Noble noble = nobles.get(0);
    Noble noble2 = nobles.get(1);
    Move move5 = new Move(Action.RECEIVE_NOBLE, null, sofia, null,
      noble, null, null, null);
    Action action2 = gb.applyMove(move5, sofia);
    assertTrue(inventory.getNobles().contains(noble));
    assertNull(action2);

    Action action3 = gb.getEndOfTurnActions(move5, inventory);
    assertFalse(inventory.getNobles().contains(noble2));
    assertNull(action3);
  }

  @Test
  void applyReceiveReservedNobleMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();

    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, cards.get(2), sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    Noble reservedNoble = nobles.get(2);
    Move move3 = new Move(Action.RESERVE_NOBLE, null, sofia, null,
      reservedNoble, null, null, null);
    gb.applyMove(move3, sofia);

    //Taking enough tokens to afford the card
    Move move4 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move4, sofia);

    Move move5 = new Move(Action.PURCHASE_DEV, cards.get(11), sofia, null,
      null, null, null, null);
    gb.applyMove(move5, sofia);

    //Taking enough tokens to afford the card
    Move move6 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move6, sofia);

    Move move7 = new Move(Action.PURCHASE_DEV, cards.get(20), sofia, null,
      null, null, null, null);
    gb.applyMove(move7, sofia);

    Action action1 = gb.getEndOfTurnActions(move7, inventory);
    assertNull(action1);
  }

  @Test
  void getCoatOfArmsMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, cards.get(18), sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    //Taking enough tokens to afford the card
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move3, sofia);

    Move move4 = new Move(Action.PURCHASE_DEV, cards.get(19), sofia, null,
      null, null, null, null);
    gb.applyMove(move4, sofia);

    Action action1 = gb.getEndOfTurnActions(move4, inventory);
    assertNull(action1);
  }

  @Test
  void performTakeExtraTokenAfterTakingTwo() {
    UserInventory inventory1 = gb.getInventoryByPlayerName("Sofia").get();
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();

    inventory1.addPower(Power.TAKE_2_GEMS_SAME_COL_AND_TAKE_1_GEM_DIF_COL);
    List<UserInventory> userInventories = new ArrayList<>(List.of(inventory1, inventory2));
    gb = new GameBoard(userInventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);

    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, SAPPHIRE, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, SAPPHIRE, null);
    Action action1 = gb.applyMove(move2, sofia);
    assertEquals(Action.TAKE_EXTRA_TOKEN, action1);

    Move move3 = new Move(Action.TAKE_EXTRA_TOKEN, null, sofia, null,
      null, null, EMERALD, null);
    Action action2 = gb.applyMove(move3, sofia);
    assertNull(action2);
  }

  @Test
  void performTakeExtraTokenAfterPurchase() {
    UserInventory inventory1 = gb.getInventoryByPlayerName("Sofia").get();
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();

    inventory1.addPower(Power.PURCHASE_CARD_TAKE_TOKEN);
    List<UserInventory> userInventories = new ArrayList<>(List.of(inventory1, inventory2));
    gb = new GameBoard(userInventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);

    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, cards.get(0), sofia, null,
      null, null, null, null);
    Action action1 = gb.applyMove(move2, sofia);
    assertEquals(Action.TAKE_EXTRA_TOKEN, action1);

    Move move3 = new Move(Action.TAKE_EXTRA_TOKEN, null, sofia, null,
      null, null, EMERALD, null);
    Action action2 = gb.applyMove(move3, sofia);
    assertNull(action2);
  }

  @Test
  void getAndApplyReceiveCityMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, cards.get(14), sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    //Taking enough tokens to afford the card
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move3, sofia);

    Move move4 = new Move(Action.PURCHASE_DEV, cards.get(15), sofia, null,
      null, null, null, null);
    gb.applyMove(move4, sofia);

    Action action1 = gb.getEndOfTurnActions(move4, inventory);
    assertEquals(Action.RECEIVE_CITY, action1);

    City city = cities.get(0);
    City city1 = cities.get(1);
    Move move5 = new Move(Action.RECEIVE_CITY, null, sofia, null,
      null, null, null, city);
    Action action2 = gb.applyMove(move5, sofia);
    assertTrue(inventory.hasCity(city));
    assertNull(action2);

    Action action3 = gb.getEndOfTurnActions(move5, inventory);
    assertFalse(inventory.hasCity(city1));
    assertNull(action3);
  }

  @Test
  void applyReceiveCityMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    //Taking enough tokens to afford the card
    Move move1 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move1, sofia);

    Move move2 = new Move(Action.PURCHASE_DEV, cards.get(14), sofia, null,
      null, null, null, null);
    gb.applyMove(move2, sofia);

    //Taking enough tokens to afford the card
    Move move3 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move3, sofia);

    Move move4 = new Move(Action.PURCHASE_DEV, cards.get(16), sofia, null,
      null, null, null, null);
    gb.applyMove(move4, sofia);

    //Taking enough tokens to afford the card
    Move move5 = new Move(Action.TAKE_TOKEN, null, sofia, null,
      null, null, DIAMOND, null);
    gb.applyMove(move5, sofia);

    Move move6 = new Move(Action.PURCHASE_DEV, cards.get(18), sofia, null,
      null, null, null, null);
    gb.applyMove(move6, sofia);

    Action action1 = gb.getEndOfTurnActions(move4, inventory);
    assertNull(action1);
  }

  @Test
  void getPendingAction() {
    assertNull(gb.getPendingAction());
  }

  @Test
  void getInventoryByPlayerName() {
    assertEquals(Optional.ofNullable(inventories.get(0)), gb.getInventoryByPlayerName("Sofia"));

  }

  @Test
  void getInventoryByPlayerNamePlayerNoExist() {
    assertEquals(Optional.empty(), gb.getInventoryByPlayerName("John"));

  }

  @Test
  void getInventories() {
    assertEquals(inventories.size(), gb.getInventories().size());
  }

  @Test
  void getDecks() {
    assertEquals(decks, gb.getDecks());
  }

  @Test
  void getCards() {
    assertEquals(cards, gb.getCards());
  }

  @Test
  void getTokenPilesNoGold() {
    assertEquals(5, gb.getTokenPilesNoGold().size());
  }

  @Test
  void getTokenPiles() {
    assertEquals(tokenPiles.size(), gb.getTokenPiles().size());
  }

  @Test
  void noGoldTokens() {
    assertFalse(gb.noGoldTokens());
  }

  @Test
  void getNobles() {
    assertEquals(nobles, gb.getNobles());
  }

  @Test
  void testEquals() {
    GameBoard gb2 = new GameBoard(inventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);
    assertEquals(gb, gb2);
  }

  @Test
  void testNotEquals() {
    assertFalse(gb.equals(null));
  }

  @Test
  void testHashCode() {
    GameBoard gb2 = new GameBoard(inventories, decks, cards, tokenPiles, nobles, tradingPosts, cities);
    assertEquals(gb.hashCode(), gb2.hashCode());
  }

  @Test
  void testEqualsSameBoard() {
    assertEquals(gb, gb);
  }

  @Test
  void testNotEqualsNull() {
    assertNotEquals(null, gb);
  }

  @Test
  void getTradingPostSlots() {
    assertEquals(tradingPosts, gb.getTradingPostSlots());
  }

  @Test
  void getCities() {
    assertEquals(cities, gb.getCities());
  }

  @Test
  void getMoveCache() {
    assertEquals(0, gb.getMoveCache().size());
  }

  @Test
  void endTurn() {
    gb.endTurn();
    assertNull(gb.getPendingAction());
    assertEquals(0, gb.getMoveCache().size());
  }
}
