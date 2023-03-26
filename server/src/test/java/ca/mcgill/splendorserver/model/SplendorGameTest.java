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

  private SplendorGame game;
  SessionInfo sessionInfo;
  private PlayerWrapper sofia;
  PlayerWrapper jeff;

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
    sessionInfo = new SessionInfo("12345", playerList, players, sofia,"1L");
    game = new SplendorGame(sessionInfo,1L);
  }

  @Test
  void setRequiresUpdate() {
    game.setRequiresUpdate(false);
    assertFalse(game.getRequiresUpdate());
  }

  @Test
  void setFinished() {
    game.setFinished();
    assertTrue(game.isFinished());
  }

  @Test
  void whoseTurn() {
    assertEquals(sofia, game.whoseTurn());
  }

  @Test
  void isStartingPlayer() {
    assertTrue(game.isStartingPlayer(sofia));
  }

  @Test
  void endTurn() {
    assertEquals(jeff, game.endTurn(sofia));
  }

  @Test
  void endTurnNotYourTurn() {
    assertThrows(
      IllegalGameStateException.class,
      () -> game.endTurn(jeff), jeff + " cannot end their turn while " + sofia + " is the current player");
  }

  @Test
  void getPlayerByName() {
    assertEquals(Optional.ofNullable(sofia), game.getPlayerByName("Sofia"));
  }

  @Test
  void getGameId() {
    assertEquals(1L, game.getGameId());
  }

  @Test
  void getBoard() {

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
    SplendorGame game1 = new SplendorGame(sessionInfo,1L);
    assertEquals(game, game1);
  }

  @Test
  void testEqualsSameGame() {
    assertEquals(game, game);
  }

  @Test
  void testNotEqualsNull() {
    assertFalse(game.equals(null));
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
    SplendorGame game1 = new SplendorGame(sessionInfo,1L);
    assertEquals(game.hashCode(), game1.hashCode());
  }
}