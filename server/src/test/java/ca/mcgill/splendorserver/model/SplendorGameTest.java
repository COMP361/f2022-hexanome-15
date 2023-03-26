package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    sessionInfo1 = new SessionInfo("SplendorOrient", playerList, players, sofia,"1L");
    sessionInfo2 = new SessionInfo("SplendorOrientTradingPosts", playerList, players, sofia,"2L");
    sessionInfo3 = new SessionInfo("SplendorOrientCities", playerList, players, sofia,"3L");
    game1 = new SplendorGame(sessionInfo1,1L);
    game2 = new SplendorGame(sessionInfo2,2L);
    game3 = new SplendorGame(sessionInfo3,3L);
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
    assertEquals(2, game3.getBoard().getCities().size());
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
    SessionInfo sessionInfo = new SessionInfo("12345", playerList, players, sofia,"1L");
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
    SessionInfo sessionInfo = new SessionInfo("12345", playerList, players, sofia,"1L");
    SplendorGame game4 = new SplendorGame(sessionInfo,1L);
    assertEquals(game1.hashCode(), game4.hashCode());
  }
}