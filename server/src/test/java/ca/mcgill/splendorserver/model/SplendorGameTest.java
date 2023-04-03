package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.control.SaveGameStorage;
import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.control.TerminalGameStateManager;
import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.savegame.DeckJson;
import ca.mcgill.splendorserver.model.savegame.GameBoardJson;
import ca.mcgill.splendorserver.model.savegame.SaveGame;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArms;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
import ca.mcgill.splendorserver.model.tradingposts.Power;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static ca.mcgill.splendorserver.model.cards.DeckType.BASE1;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

class SplendorGameTest {

  private SplendorGame game1;
  private SplendorGame game2;
  private SplendorGame game3;
  private SessionInfo sessionInfo1;
  private SessionInfo sessionInfo2;
  private SessionInfo sessionInfo3;
  private PlayerWrapper sofia;
  private PlayerWrapper jeff;
  private List<Player> playerList;
  private List<PlayerWrapper> players;

  @BeforeEach
  void setUp() {
    sofia = PlayerWrapper.newPlayerWrapper("Sofia");
    jeff = PlayerWrapper.newPlayerWrapper("Jeff");
    Player player1 = new Player("Sofia", "purple");
    Player player2 = new Player("Jeff", "blue");
    playerList = new ArrayList<>();
    playerList.add(player1);
    playerList.add(player2);
    players = new ArrayList<>();
    players.add(sofia);
    players.add(jeff);
    sessionInfo1 = new SessionInfo("SplendorOrient", playerList, players, sofia,"");
    sessionInfo2 = new SessionInfo("SplendorOrientTradingPosts", playerList, players, sofia,"");
    sessionInfo3 = new SessionInfo("SplendorOrientCities", playerList, players, sofia,"");
    game1 = new SplendorGame(sessionInfo1,1L);
    game2 = new SplendorGame(sessionInfo2,2L);
    game3 = new SplendorGame(sessionInfo3,3L);
  }
  @Test
  void instantiateOrientGameBoardFromSaveGame() {
    List<InventoryJson> inventoriesJson = new ArrayList<>();
    GameBoard gameboard = game2.getBoard();
    for (UserInventory inventory : gameboard.getInventories()) {
      Card card1 = new Card(0, 1, DIAMOND, BASE1, ONE,
        new CardCost(1, 0, 0, 0, 0));
      Card card2 = new Card(1, 0, DIAMOND, BASE1, ONE,
        new CardCost(1, 0, 0, 0, 0));
      inventory.addToken(new Token(DIAMOND));
      inventory.addToken(new Token(SAPPHIRE));
      inventory.addToken(new Token(EMERALD));
      inventory.purchaseCard(card1);
      inventory.addReservedCard(card2);
      Noble noble1 = new Noble(0, new CardCost(0, 0, 0, 1, 0));
      Noble noble2 = new Noble(1, new CardCost(0, 0, 0, 2, 0));
      inventory.receiveVisitFrom(noble1);
      inventory.addReservedNoble(noble2);
      InventoryJson inventoryJson = new InventoryJson(inventory.getCards(),
        inventory.getTokenPiles(), inventory.getPlayer().getName(),
        inventory.getPrestigeWon(), inventory.getNobles(),
        inventory.getPowers(), inventory.getCoatOfArmsPile(),
        inventory.getCities(), null);
      inventoriesJson.add(inventoryJson);
    }
    List<ca.mcgill.splendorserver.model.savegame.DeckJson> decksJson = new ArrayList<>();
    for (Deck deck : gameboard.getDecks()) {
      decksJson.add(new DeckJson(deck));
    }
    List<Integer> nobles = new ArrayList<>();
    for (Noble noble : gameboard.getNobles()) {
      nobles.add(noble.getId());
    }
    List<Integer> cardField = new ArrayList<>();
    for (Card card : gameboard.getCards()) {
      cardField.add(card.getId());
    }
    List<TradingPostJson> tradingPosts = new ArrayList<>();
    for (TradingPostSlot tradingPostSlot : gameboard.getTradingPostSlots()) {
      List<CoatOfArmsType> coatOfArmsTypes = new ArrayList<>();
      for (CoatOfArms coatOfArms : tradingPostSlot.getAcquiredCoatOfArmsList()) {
        coatOfArmsTypes.add(coatOfArms.getType());
      }
      tradingPosts.add(new TradingPostJson(tradingPostSlot.getId(), coatOfArmsTypes));
    }
    List<Integer> cities = new ArrayList<>();
    for (City city : gameboard.getCities()) {
      cities.add(city.getId());
    }
    List<String> winningPlayers = new ArrayList<>();
    for (PlayerWrapper player : game2.getWinningPlayers()) {
      winningPlayers.add(player.getName());
    }
    ca.mcgill.splendorserver.model.savegame.GameBoardJson gameBoardJson = new GameBoardJson(game2.whoseTurn().getName(), inventoriesJson, decksJson,
      nobles, cardField, gameboard.getTokenPiles(), tradingPosts, cities, winningPlayers);
    String id = String.valueOf(new Random().nextInt() & Integer.MAX_VALUE);
    SaveGame savegame = new SaveGame(id, new Gson().toJson(gameBoardJson));
    SaveGameStorage.addSaveGame(savegame);

    SessionInfo sessionInfo4 = new SessionInfo("SplendorOrient", playerList, players, sofia, id);
    SplendorGame game4 = new SplendorGame(sessionInfo4, 1L);
    assertNotNull(game4);
  }

  @Test
  void instantiateTradingPostsGameBoardFromSaveGame() {
    List<InventoryJson> inventoriesJson = new ArrayList<>();
    GameBoard gameboard = game2.getBoard();
    for (UserInventory inventory : gameboard.getInventories()) {
      inventory.addPower(Power.GAIN_5_PRESTIGE);
      InventoryJson inventoryJson = new InventoryJson(inventory.getCards(),
        inventory.getTokenPiles(), inventory.getPlayer().getName(),
        inventory.getPrestigeWon(), inventory.getNobles(),
        inventory.getPowers(), inventory.getCoatOfArmsPile(),
        inventory.getCities(), null);
      inventoriesJson.add(inventoryJson);
    }
    List<ca.mcgill.splendorserver.model.savegame.DeckJson> decksJson = new ArrayList<>();
    for (Deck deck : gameboard.getDecks()) {
      decksJson.add(new DeckJson(deck));
    }
    List<Integer> nobles = new ArrayList<>();
    for (Noble noble : gameboard.getNobles()) {
      nobles.add(noble.getId());
    }
    List<Integer> cardField = new ArrayList<>();
    for (Card card : gameboard.getCards()) {
      cardField.add(card.getId());
    }
    List<TradingPostJson> tradingPosts = new ArrayList<>();
    for (TradingPostSlot tradingPostSlot : gameboard.getTradingPostSlots()) {
      List<CoatOfArmsType> coatOfArmsTypes = new ArrayList<>();
      for (CoatOfArms coatOfArms : tradingPostSlot.getAcquiredCoatOfArmsList()) {
        coatOfArmsTypes.add(coatOfArms.getType());
      }
      tradingPosts.add(new TradingPostJson(tradingPostSlot.getId(), coatOfArmsTypes));
    }
    List<Integer> cities = new ArrayList<>();
    for (City city : gameboard.getCities()) {
      cities.add(city.getId());
    }
    List<String> winningPlayers = new ArrayList<>();
    for (PlayerWrapper player : game2.getWinningPlayers()) {
      winningPlayers.add(player.getName());
    }
    ca.mcgill.splendorserver.model.savegame.GameBoardJson gameBoardJson = new GameBoardJson(game1.whoseTurn().getName(), inventoriesJson, decksJson,
      nobles, cardField, gameboard.getTokenPiles(), tradingPosts, cities, winningPlayers);
    String id = String.valueOf(new Random().nextInt() & Integer.MAX_VALUE);
    SaveGame savegame = new SaveGame(id, new Gson().toJson(gameBoardJson));
    SaveGameStorage.addSaveGame(savegame);

    SessionInfo sessionInfo4 = new SessionInfo("SplendorOrientTradingPosts", playerList, players, sofia, id);
    SplendorGame game4 = new SplendorGame(sessionInfo4, 2L);
    assertNotNull(game4);
  }

  @Test
  void instantiateCitiesGameBoardFromSaveGame() {
    List<InventoryJson> inventoriesJson = new ArrayList<>();
    GameBoard gameboard = game3.getBoard();
    for (UserInventory inventory : gameboard.getInventories()) {
      Card card1 = new Card(0, 1, DIAMOND, BASE1, ONE,
        new CardCost(1, 0, 0, 0, 0));
      inventory.addToken(new Token(DIAMOND));
      inventory.purchaseCard(card1);
      City city1 = new City(0, 1,
        new CardCost(0, 0, 0, 0, 0), 0);
      inventory.addCity(city1);
      InventoryJson inventoryJson = new InventoryJson(inventory.getCards(),
        inventory.getTokenPiles(), inventory.getPlayer().getName(),
        inventory.getPrestigeWon(), inventory.getNobles(),
        inventory.getPowers(), inventory.getCoatOfArmsPile(),
        inventory.getCities(), null);
      inventoriesJson.add(inventoryJson);
    }
    List<ca.mcgill.splendorserver.model.savegame.DeckJson> decksJson = new ArrayList<>();
    for (Deck deck : gameboard.getDecks()) {
      decksJson.add(new DeckJson(deck));
    }
    List<Integer> nobles = new ArrayList<>();
    for (Noble noble : gameboard.getNobles()) {
      nobles.add(noble.getId());
    }
    List<Integer> cardField = new ArrayList<>();
    for (Card card : gameboard.getCards()) {
      cardField.add(card.getId());
    }
    List<TradingPostJson> tradingPosts = new ArrayList<>();
    for (TradingPostSlot tradingPostSlot : gameboard.getTradingPostSlots()) {
      List<CoatOfArmsType> coatOfArmsTypes = new ArrayList<>();
      for (CoatOfArms coatOfArms : tradingPostSlot.getAcquiredCoatOfArmsList()) {
        coatOfArmsTypes.add(coatOfArms.getType());
      }
      tradingPosts.add(new TradingPostJson(tradingPostSlot.getId(), coatOfArmsTypes));
    }
    List<Integer> cities = new ArrayList<>();
    for (City city : gameboard.getCities()) {
      cities.add(city.getId());
    }
    List<String> winningPlayers = new ArrayList<>();
    for (PlayerWrapper player : game2.getWinningPlayers()) {
      winningPlayers.add(player.getName());
    }
    ca.mcgill.splendorserver.model.savegame.GameBoardJson gameBoardJson = new GameBoardJson(game1.whoseTurn().getName(), inventoriesJson, decksJson,
      nobles, cardField, gameboard.getTokenPiles(), tradingPosts, cities, winningPlayers);
    String id = String.valueOf(new Random().nextInt() & Integer.MAX_VALUE);
    SaveGame savegame = new SaveGame(id, new Gson().toJson(gameBoardJson));
    SaveGameStorage.addSaveGame(savegame);

    SessionInfo sessionInfo4 = new SessionInfo("SplendorOrientCities", playerList, players, sofia, id);
    SplendorGame game4 = new SplendorGame(sessionInfo4, 3L);
    assertNotNull(game4);
  }

  @Test
  void getLastPlayer() {
    assertEquals(jeff, game1.getLastPlayer());
  }

  @Test
  void getWinningPlayer() {
    UserInventory inventory = game1.getBoard().getInventoryByPlayerName("Sofia").get();
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card = new Card(0,15, DIAMOND, BASE1, ONE, cost);
    Token token = new Token(DIAMOND);
    inventory.addToken(token);
    inventory.purchaseCard(card);
    game1.endTurn(sofia);
    TerminalGameStateManager.isTerminalGameState(game1);
    assertEquals(sofia, game1.getWinningPlayers().get(0));
    assertEquals(1, game1.getWinningPlayers().size());
  }

  @Test
  void getWinningPlayerTieBreak() {
    UserInventory inventory1 = game1.getBoard().getInventoryByPlayerName("Sofia").get();
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card1 = new Card(0,7, DIAMOND, BASE1, ONE, cost);
    Card card2 = new Card(1,8, DIAMOND, BASE1, ONE, cost);
    Card card3 = new Card(2,15, DIAMOND, BASE1, ONE, cost);
    Token token = new Token(DIAMOND);
    inventory1.addToken(token);
    inventory1.purchaseCard(card1);
    inventory1.addToken(token);
    inventory1.purchaseCard(card2);
    game1.endTurn(sofia);
    UserInventory inventory2 = game1.getBoard().getInventoryByPlayerName("Jeff").get();
    inventory2.addToken(token);
    inventory2.purchaseCard(card3);
    TerminalGameStateManager.isTerminalGameState(game1);
    assertEquals(jeff, game1.getWinningPlayers().get(0));
    assertEquals(1, game1.getWinningPlayers().size());
  }

  @Test
  void getWinningPlayerTie() {
    UserInventory inventory1 = game1.getBoard().getInventoryByPlayerName("Sofia").get();
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card = new Card(0,15, DIAMOND, BASE1, ONE, cost);
    Token token = new Token(DIAMOND);
    inventory1.addToken(token);
    inventory1.purchaseCard(card);
    game1.endTurn(sofia);
    UserInventory inventory2 = game1.getBoard().getInventoryByPlayerName("Jeff").get();
    inventory2.addToken(token);
    Card card1 = new Card(1,15, DIAMOND, BASE1, ONE, cost);
    inventory2.purchaseCard(card1);
    TerminalGameStateManager.isTerminalGameState(game1);
    assertEquals(sofia, game1.getWinningPlayers().get(0));
    assertEquals(jeff, game1.getWinningPlayers().get(1));
  }

  @Test
  void getWinningPlayerCities() {
    UserInventory inventory = game3.getBoard().getInventoryByPlayerName("Sofia").get();
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card = new Card(0,10, DIAMOND, BASE1, ONE, cost);
    Token token = new Token(DIAMOND);
    inventory.addToken(token);
    inventory.purchaseCard(card);
    City city1 = new City(0, 10,
      new CardCost(1, 0, 0, 0, 0), 0);
    inventory.addCity(city1);
    game3.endTurn(sofia);
    TerminalGameStateManager.isTerminalGameState(game3);
    assertEquals(sofia, game3.getWinningPlayers().get(0));
    assertEquals(1, game3.getWinningPlayers().size());
  }

  @Test
  void getWinningPlayerCitiesTie() {
    UserInventory inventory1 = game3.getBoard().getInventoryByPlayerName("Sofia").get();
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card = new Card(0,10, DIAMOND, BASE1, ONE, cost);
    Token token = new Token(DIAMOND);
    inventory1.addToken(token);
    inventory1.purchaseCard(card);
    City city1 = new City(0, 10,
      new CardCost(1, 0, 0, 0, 0), 0);
    inventory1.addCity(city1);
    game3.endTurn(sofia);
    UserInventory inventory2 = game3.getBoard().getInventoryByPlayerName("Jeff").get();
    inventory2.addToken(token);
    Card card1 = new Card(1,10, DIAMOND, BASE1, ONE, cost);
    inventory2.purchaseCard(card1);
    City city2 = new City(1, 10,
      new CardCost(1, 0, 0, 0, 0), 0);
    inventory2.addCity(city2);
    TerminalGameStateManager.isTerminalGameState(game3);
    assertEquals(sofia, game3.getWinningPlayers().get(0));
    assertEquals(jeff, game3.getWinningPlayers().get(1));
  }

  @Test
  void getWinningPlayerCitiesTieBreak() {
    UserInventory inventory1 = game3.getBoard().getInventoryByPlayerName("Sofia").get();
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card1 = new Card(0,7, DIAMOND, BASE1, ONE, cost);
    Card card2 = new Card(1,8, DIAMOND, BASE1, ONE, cost);
    Card card3 = new Card(2,15, DIAMOND, BASE1, ONE, cost);
    Token token = new Token(DIAMOND);
    inventory1.addToken(token);
    inventory1.purchaseCard(card1);
    inventory1.addToken(token);
    inventory1.purchaseCard(card2);
    City city1 = new City(0, 10,
      new CardCost(1, 0, 0, 0, 0), 0);
    inventory1.addCity(city1);
    game3.endTurn(sofia);
    UserInventory inventory2 = game3.getBoard().getInventoryByPlayerName("Jeff").get();
    inventory2.addToken(token);
    inventory2.purchaseCard(card3);
    City city2 = new City(1, 10,
      new CardCost(1, 0, 0, 0, 0), 0);
    inventory2.addCity(city2);
    TerminalGameStateManager.isTerminalGameState(game3);
    assertEquals(jeff, game3.getWinningPlayers().get(0));
    assertEquals(1, game3.getWinningPlayers().size());
  }

  @Test
  void getWinningPlayerCitiesTiePrestige() {
    UserInventory inventory1 = game3.getBoard().getInventoryByPlayerName("Sofia").get();
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card1 = new Card(0,9, DIAMOND, BASE1, ONE, cost);
    Card card2 = new Card(1,8, DIAMOND, BASE1, ONE, cost);
    Card card3 = new Card(2,15, DIAMOND, BASE1, ONE, cost);
    Token token = new Token(DIAMOND);
    inventory1.addToken(token);
    inventory1.purchaseCard(card1);
    inventory1.addToken(token);
    inventory1.purchaseCard(card2);
    City city1 = new City(0, 10,
      new CardCost(1, 0, 0, 0, 0), 0);
    inventory1.addCity(city1);
    game3.endTurn(sofia);
    UserInventory inventory2 = game3.getBoard().getInventoryByPlayerName("Jeff").get();
    inventory2.addToken(token);
    inventory2.purchaseCard(card3);
    City city2 = new City(1, 10,
      new CardCost(1, 0, 0, 0, 0), 0);
    inventory2.addCity(city2);
    TerminalGameStateManager.isTerminalGameState(game3);
    assertEquals(sofia, game3.getWinningPlayers().get(0));
    assertEquals(1, game3.getWinningPlayers().size());
  }

  @Test
  void setRequiresUpdate() {
    game1.setRequiresUpdate(false);
    assertFalse(game1.getRequiresUpdate());
  }

  @Test
  void setFinished() {
    game1.setFinished();
    assertTrue(game1.isFinished());
  }

  @Test
  void whoseTurn() {
    assertEquals(sofia, game1.whoseTurn());
  }

  @Test
  void isStartingPlayer() {
    assertTrue(game1.isStartingPlayer(sofia));
  }

  @Test
  void endTurn() {
    assertEquals(jeff, game1.endTurn(sofia));
  }

  @Test
  void endTurnNotYourTurn() {
    assertThrows(
      IllegalGameStateException.class,
      () -> game1.endTurn(jeff), jeff + " cannot end their turn while " + sofia + " is the current player");
  }

  @Test
  void getPlayerByName() {
    assertEquals(Optional.ofNullable(sofia), game1.getPlayerByName("Sofia"));
  }

  @Test
  void getGameId() {
    assertEquals(1L, game1.getGameId());
  }

  @Test
  void getSessionInfo() {
    assertEquals(sessionInfo1, game1.getSessionInfo());
  }

  @Test
  void getBoard() {
    assertEquals(3, game1.getBoard().getNobles().size());
    assertEquals(18, game1.getBoard().getCards().size());
    assertEquals(6, game1.getBoard().getDecks().size());
    assertEquals(6, game1.getBoard().getTokenPiles().size());
    assertEquals(2, game1.getBoard().getInventories().size());
    assertEquals(0, game1.getBoard().getTradingPostSlots().size());
    assertEquals(0, game1.getBoard().getCities().size());
  }

  @Test
  void getBoardTradingPosts() {
    assertEquals(3, game2.getBoard().getNobles().size());
    assertEquals(18, game2.getBoard().getCards().size());
    assertEquals(6, game2.getBoard().getDecks().size());
    assertEquals(6, game2.getBoard().getTokenPiles().size());
    assertEquals(2, game2.getBoard().getInventories().size());
    assertEquals(5, game2.getBoard().getTradingPostSlots().size());
    assertEquals(0, game2.getBoard().getCities().size());
  }

  @Test
  void getBoardCities() {
    assertEquals(3, game3.getBoard().getNobles().size());
    assertEquals(18, game3.getBoard().getCards().size());
    assertEquals(6, game3.getBoard().getDecks().size());
    assertEquals(6, game3.getBoard().getTokenPiles().size());
    assertEquals(2, game3.getBoard().getInventories().size());
    assertEquals(0, game3.getBoard().getTradingPostSlots().size());
    assertEquals(3, game3.getBoard().getCities().size());
  }

  @Test
  void testEquals() {
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
    SessionInfo sessionInfo = new SessionInfo("12345", playerList, players, sofia,"");
    SplendorGame game4 = new SplendorGame(sessionInfo,1L);
    assertEquals(game1, game4);
  }

  @Test
  void testEqualsSameGame() {
    assertEquals(game1, game1);
  }

  @Test
  void testNotEqualsNull() {
    assertFalse(game1.equals(null));
  }

  @Test
  void testHashCode() {
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
    SessionInfo sessionInfo = new SessionInfo("12345", playerList, players, sofia,"");
    SplendorGame game4 = new SplendorGame(sessionInfo,1L);
    assertEquals(game1.hashCode(), game4.hashCode());
  }
}