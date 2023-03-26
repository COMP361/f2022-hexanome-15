package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SessionInfoTest {

  private SessionInfo sessionInfo;
  private List<Player> playerList;
  private List<PlayerWrapper> players;
  PlayerWrapper sofia;

  @BeforeEach
  void setUp() {
    sofia = PlayerWrapper.newPlayerWrapper("Sofia");
    PlayerWrapper jeff = PlayerWrapper.newPlayerWrapper("Jeff");
    Player player1 = new Player("Sofia", "purple");
    Player player2 = new Player("Jeff", "blue");
    playerList = new ArrayList<>();
    playerList.add(player1);
    playerList.add(player2);
    players = new ArrayList<>();
    players.add(sofia);
    players.add(jeff);
    sessionInfo = new SessionInfo("12345", playerList, players, sofia,"1L");
  }

  @Test
  void getPlayers() {
    assertEquals(players, sessionInfo.getPlayers());
  }

  @Test
  void getGameServer() {
    assertEquals("12345", sessionInfo.getGameServer());
  }

  @Test
  void getGameCreator() {
    assertEquals(sofia, sessionInfo.getGameCreator());
  }

  @Test
  void getNumPlayers() {
    assertEquals(2, sessionInfo.getNumPlayers());
  }

  @Test
  void getPlayerByName() {
    assertEquals(Optional.ofNullable(sofia), sessionInfo.getPlayerByName("Sofia"));
  }

  @Test
  void getPlayerByNamePlayerNoExist() {
    assertEquals(Optional.empty(), sessionInfo.getPlayerByName("Zach"));
  }

  @Test
  void iterator() {
    assertTrue(sessionInfo.iterator().hasNext());
  }

  @Test
  void testEquals() {
    SessionInfo sessionInfo1 = new SessionInfo("12345", playerList, players, sofia,"1L");
    assertEquals(sessionInfo, sessionInfo1);
  }

  @Test
  void testEqualsSameObject() {
    assertEquals(sessionInfo, sessionInfo);
  }

  @Test
  void testNotEquals() {
    assertFalse(sessionInfo.equals(null));
  }

  @Test
  void testHashCode() {
    SessionInfo sessionInfo1 = new SessionInfo("12345", playerList, players, sofia,"1L");
    assertEquals(sessionInfo.hashCode(), sessionInfo1.hashCode());
  }
}