package ca.mcgill.splendorserver.control;

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
  PlayerWrapper sofia;
  PlayerWrapper jeff;
  List<PlayerWrapper> players;


  @BeforeEach
  void setUp() {
    sofia = PlayerWrapper.newPlayerWrapper("Sofia");
    jeff = PlayerWrapper.newPlayerWrapper("Jeff");
    players = new ArrayList<>();
    players.add(sofia); players.add(jeff);

    si = new SessionInfo("12345",players,sofia,"1L");
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
    assertTrue(LocalGameStorage.requiresUpdate(1L),"");
  }

  @Test
  void exists() {
  }
}