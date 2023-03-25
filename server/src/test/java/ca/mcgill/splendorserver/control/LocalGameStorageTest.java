package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.GameNotFoundException;
import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.SplendorGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class LocalGameStorageTest {
  private SplendorGame sg;

  @BeforeEach
  void setUp() {
    PlayerWrapper sofia = PlayerWrapper.newPlayerWrapper("Sofia");
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
    sg = new SplendorGame(sessionInfo,1L);
  }

  @Test
  void addActiveGame() {
    LocalGameStorage.addActiveGame(sg);
    assertEquals(Optional.ofNullable(sg), LocalGameStorage.getActiveGame(1L));
  }

  @Test
  void removeActiveGame() {
    LocalGameStorage.addActiveGame(sg);
    LocalGameStorage.removeActiveGame(sg);
    assertFalse(LocalGameStorage.exists(1L));
  }

  @Test
  void requiresUpdate() {
    LocalGameStorage.addActiveGame(sg);
    assertTrue(LocalGameStorage.requiresUpdate(1L));
  }

  @Test
  void notRequiresUpdate() {
    try {
      LocalGameStorage.requiresUpdate(1L);
    }
    catch (GameNotFoundException e) {
      assertEquals("Could not find game ID: 1", e.getMessage());
    }
  }

  @Test
  void getActiveGameEmpty() {
    assertEquals(Optional.empty(), LocalGameStorage.getActiveGame(1L));
  }

}