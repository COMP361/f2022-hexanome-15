package ca.mcgill.splendorserver.gameio;


import ca.mcgill.splendorserver.control.LocalGameStorage;
import ca.mcgill.splendorserver.control.SessionInfo;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class GameRestControllerTest {

  private GameRestController controller = new GameRestController();
  SessionInfo sessionInfo;

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
    sessionInfo = new SessionInfo("12345", playerList, players, sofia,"1L");
  }

  @Test
  void launchRequest() {
    String sessionInfoJson = new Gson().toJson(sessionInfo);
    assertEquals(ResponseEntity.status(HttpStatus.OK).build(), controller.launchRequest(1L, sessionInfoJson));
  }

  @Test
  void quitRequest() {
    String sessionInfoJson = new Gson().toJson(sessionInfo);
    controller.launchRequest(1L, sessionInfoJson);
    controller.quitRequest(1L);
    assertEquals(Optional.empty(), LocalGameStorage.getActiveGame(1L));
  }

  @Test
  void getGameBoard() {
  }

  @Test
  void getGameBoardNotFound() {
    assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .build(), controller.getGameBoard(1L));
  }

  @Test
  void knock() {
    assertEquals("SOMEONE'S KNOCKING", controller.knock());
  }
}


