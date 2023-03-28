package ca.mcgill.splendorserver.model;
import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.*;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class GameBoardTest {
  private SplendorGame game;
  private GameBoard gb;
  private PlayerWrapper sofia;
  private PlayerWrapper jeff;

  private List<UserInventory> inventories;
  private List<Deck> decks;
  private List<Card> cards;
  private List <TokenPile> tokenPiles;
  private List<Noble> nobles;
  private List<TradingPostSlot> tradingPosts;
  private List<City> cities;

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
    SessionInfo sessionInfo = new SessionInfo("12345", playerList, players, sofia,"1L");
    game = new SplendorGame(sessionInfo,1L);
    inventories = game.getBoard().getInventories();
    decks = game.getBoard().getDecks();
    cards = game.getBoard().getCards();
    tokenPiles = game.getBoard().getTokenPiles().values().stream().toList();
    nobles = game.getBoard().getNobles();
    tradingPosts = game.getBoard().getTradingPostSlots();
    cities = game.getBoard().getCities();
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
    List<Card> reservedCards = new ArrayList<>();
    for (Card card : inventory.getCards()) {
      if (card.isReserved()) {
        reservedCards.add(card);
      }
    }
    assertEquals(reservedCard, reservedCards.get(0));
    assertEquals(1, inventory.getTokenPiles().get(GOLD).getSize());
  }

  @Test
  void applyReserveMoveNoGold() {
    UserInventory inventory2 = gb.getInventoryByPlayerName("Jeff").get();

    int numGold = gb.getTokenPiles().get(GOLD).getSize();
    for (int i = 0; i < numGold; i++) {
      Move move = new Move(Action.RESERVE_DEV, cards.get(0), sofia, null,
        null, null, null, null);
      gb.applyMove(move, sofia);
    }

    Card reservedCard = cards.get(0);
    Move move = new Move(Action.RESERVE_DEV, reservedCard, jeff, null,
      null, null, null, null);
    Action action = gb.applyMove(move, jeff);
    assertNull(action);
    List<Card> reservedCards = new ArrayList<>();
    for (Card card : inventory2.getCards()) {
      if (card.isReserved()) {
        reservedCards.add(card);
      }
    }

    assertEquals(reservedCard, reservedCards.get(0));
    assertEquals(0, inventory2.getTokenPiles().get(GOLD).getSize());
  }

  @Test
  void applyPurchaseMove() {
    UserInventory inventory = gb.getInventoryByPlayerName("Sofia").get();
    Card purchasedCard = cards.get(0);
    //Taking enough tokens to afford the card
    for (Map.Entry<TokenType, Integer> entry : purchasedCard.getCardCost().entrySet()) {
        for (int i = 0; i < entry.getValue(); i++) {
          Move move = new Move(Action.TAKE_TOKEN, null, sofia, null,
            null, null, entry.getKey(), null);
          gb.applyMove(move, sofia);
        }
    }
    Move move = new Move(Action.PURCHASE_DEV, purchasedCard, sofia, null,
      null, null, null, null);
    Action action = gb.applyMove(move, sofia);
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
    for (Map.Entry<TokenType, Integer> entry : purchasedCard.getCardCost().entrySet()) {
      for (int i = 0; i < entry.getValue(); i++) {
        Move move2 = new Move(Action.TAKE_TOKEN, null, sofia, null,
          null, null, entry.getKey(), null);
        gb.applyMove(move2, sofia);
      }
    }
    Move move3 = new Move(Action.PURCHASE_DEV, purchasedCard, sofia, null,
      null, null, null, null);
    Action action = gb.applyMove(move3, sofia);
    assertNull(action);
    assertTrue(inventory.hasCard(purchasedCard));
    assertEquals(1, inventory.tokenCount());
  }

  @Test
  void getPendingAction() {
    assertEquals(null, gb.getPendingAction());

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
    assertFalse(gb.equals(null));
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
  void getEndOfTurnActions() {

  }

  @Test
  void endTurn() {
    gb.endTurn();
    assertEquals(null, gb.getPendingAction());
    assertEquals(0, gb.getMoveCache().size());
  }
}
