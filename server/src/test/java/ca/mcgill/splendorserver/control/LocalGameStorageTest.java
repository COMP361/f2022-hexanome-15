package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.SplendorGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class LocalGameStorageTest {
  SplendorGame sg;
  SessionInfo si;
  List<PlayerWrapper> players;
  List<Player> playerList;


  @BeforeEach
  void setUp() {
    PlayerWrapper sofia = PlayerWrapper.newPlayerWrapper("Sofia");
    PlayerWrapper jeff = PlayerWrapper.newPlayerWrapper("Jeff");
    Player player1 = new Player("Sofia", "purple");
    Player player2 = new Player("Jeff", "blue");
    playerList = new ArrayList<>();
    playerList.add(player1);
    playerList.add(player2);
    players = new ArrayList<>();
    players.add(sofia);
    players.add(jeff);

    si = new SessionInfo("12345", playerList, players, sofia,"1L");
    sg = new SplendorGame(si,1L);

  }

  @Test
  void addActiveGame() {

  }

  @Test
  void removeActiveGame() {
  }

  @Test
  void getActiveGame() {

  }

  @Test
  void requiresUpdate() {
    LocalGameStorage.addActiveGame(sg);
    assertTrue(LocalGameStorage.requiresUpdate(1L));
  }

  @Test
  void exists() {
  }
}