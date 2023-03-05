package ca.mcgill.splendorserver.gameio;

import ca.mcgill.splendorserver.control.LocalGameStorage;
import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.model.SplendorGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ca.mcgill.splendorserver.gameio.GameRestController.*;
import org.springframework.web.bind.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class GameRestControllerTest {
  PlayerWrapper pw = PlayerWrapper.newPlayerWrapper("Jeff");
  PlayerWrapper pw2 = PlayerWrapper.newPlayerWrapper("Larry");
  PlayerWrapper pw3 = PlayerWrapper.newPlayerWrapper("Sofia");
  List<PlayerWrapper> pws = new ArrayList<>();
  SessionInfo si = new SessionInfo("",pws,pw,"");
  GameRestController grc = new GameRestController();
  Exception e = new Exception();

  @Test
  void launchRequest()
  {
    pws.add(pw);
    pws.add(pw2);
    pws.add(pw3);
    assertEquals(ResponseEntity.status(HttpStatus.OK).build(),
      grc.launchRequest(0L,si),"");
    assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()),
      grc.launchRequest(0L,null),"");
  }

  @Test
  void quitRequest() {
    grc.launchRequest(0L,si);
    grc.quitRequest(0L);
    assertTrue(LocalGameStorage.getActiveGame(0L).isEmpty());
  }

  @Test
  void getGameBoard() {
    assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
      grc.getGameBoard(0L),"No GameId registered on SplendorGame");

    /*
    grc.launchRequest(0L,si);
    Optional<SplendorGame> manager = LocalGameStorage.getActiveGame(0L);

    //ResponseEntity<String> aRep = ResponseEntity.status(HttpStatus.OK).body();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    assertEquals(ResponseEntity.status(HttpStatus.OK),
      grc.getGameBoard(0L),
      "GameId 0L registered on SplendorGame");
  */
  }

  @Test
  void knockTest() {
    assertEquals("SOMEONE'S KNOCKING", grc.knock(), "");
  }
}