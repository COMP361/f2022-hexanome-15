package ca.mcgill.splendorserver.model;
import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.*;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class GameBoardTest {
  private SplendorGame game;
  private GameBoard gb;
  PlayerWrapper sofia;

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
    PlayerWrapper jeff = PlayerWrapper.newPlayerWrapper("Jeff");
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
  void applyMove() {

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
    List<Move> moves = new ArrayList<>();
    assertEquals(moves, gb.getMoveCache());
  }

  @Test
  void getEndOfTurnActions() {

  }

  @Test
  void endTurn() {
    gb.endTurn();
    assertEquals(null, gb.getPendingAction());
  }
}
