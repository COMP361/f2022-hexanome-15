package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class TurnManagerTest {
  PlayerWrapper slava;
  PlayerWrapper ojas;

  List<PlayerWrapper> players;
  TurnManager tm;

  @BeforeEach
  void setUp() {
    slava = PlayerWrapper.newPlayerWrapper("Slava");
    ojas = PlayerWrapper.newPlayerWrapper("Ojas");
    players = new ArrayList<>();
    players.add(slava); players.add(ojas);
    tm = new TurnManager(players);
    TurnManager turnManager = new TurnManager();
  }


  @Test
  void removePlayer() {
    tm.removePlayer(slava);
    assertEquals(ojas,tm.whoseTurn());
  }

  @Test
  void whoseTurn() {
    tm.removePlayer(ojas);
    assertEquals(slava,tm.whoseTurn());
  }

  @Test
  void endTurn() {
    assertEquals(ojas,tm.endTurn());
  }

  @Test
  void iterator() {
    assertTrue(tm.iterator().hasNext());
  }

  @Test
  void testToString() {
    assertEquals(
      "TurnManager [aTurns=" + players + "]",
      tm.toString(),
      ""
    );
  }
}