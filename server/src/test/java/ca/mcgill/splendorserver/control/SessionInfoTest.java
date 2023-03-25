package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SessionInfoTest {

  private SessionInfo sessionInfo;
  private List<Player> playerList;

  @BeforeEach
  void setUp() {
    PlayerWrapper sofia = PlayerWrapper.newPlayerWrapper("Sofia");
    PlayerWrapper jeff = PlayerWrapper.newPlayerWrapper("Jeff");
    Player player1 = new Player("Sofia", "purple");
    Player player2 = new Player("Jeff", "blue");
    playerList = new ArrayList<>();
    playerList.add(player1);
    playerList.add(player2);
    List<PlayerWrapper> players = new ArrayList<>();
    players.add(sofia);
    players.add(jeff);
    sessionInfo = new SessionInfo("12345", playerList, players, sofia,"1L");
  }

  @Test
  void getPlayers() {
  }

  @Test
  void getGameServer() {
  }

  @Test
  void getGameCreator() {
  }

  @Test
  void getNumPlayers() {
  }

  @Test
  void getPlayerByName() {
  }

  @Test
  void iterator() {
  }

  @Test
  void testEquals() {
  }

  @Test
  void testHashCode() {
  }
}