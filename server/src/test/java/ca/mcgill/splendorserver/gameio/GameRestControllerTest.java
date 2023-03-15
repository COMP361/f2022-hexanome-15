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

  /*@Test
  void launchRequest()
  {
    pws.add(pw);
    pws.add(pw2);
    pws.add(pw3);
    assertEquals(ResponseEntity.status(HttpStatus.OK).build(),
      grc.launchRequest(0L,si.toString()),"");
    assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()),
      grc.launchRequest(0L,null),"");
  }

  @Test
  void quitRequest() {
    grc.launchRequest(0L,si.toString());
    grc.quitRequest(0L);
    assertTrue(LocalGameStorage.getActiveGame(0L).isEmpty());
  }*/

  @Test
  void getGameBoard() {
    assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
      grc.getGameBoard(0L),"No GameId registered on SplendorGame");


    grc.launchRequest(0L,si);
    Optional<SplendorGame> manager = LocalGameStorage.getActiveGame(0L);


    //ResponseEntity<String> aRep = ResponseEntity.status(HttpStatus.OK).body();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    assertEquals(ResponseEntity.status(HttpStatus.OK),
      grc.getGameBoard(0L),
      "GameId 0L registered on SplendorGame");

  }

  /*@Test
  void knockTest() {
    assertEquals("SOMEONE'S KNOCKING", grc.knock(), "");
  }*/
}

/*
import org.junit.jupiter.api.BeforeEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class GameRestControllerTest {
  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @Test
  @PutMapping
  public void testlaunchRequest() {
    String sg = "";
    long id = 270189000;
    List<PlayerWrapper> players = new ArrayList<PlayerWrapper>();
    players.add(PlayerWrapper.newPlayerWrapper("Zack"));
    players.add(PlayerWrapper.newPlayerWrapper("Jeff"));
    players.add(PlayerWrapper.newPlayerWrapper("Larry"));
    GameBoard gb = new GameBoard();
    Game game = new Game(sg, id, players, gb);
    Long num = 123456789L;
    JpaRepository<Game, Long> repo = new SimpleJpaRepository<Game, Long>(game,);
    Map<String, JsonNode> myMap =
      new HashMap<String, JsonNode>(0, 0.34f);
    JsonNodeFactory myJson = new JsonNodeFactory(true);
    ObjectNode sessionInfo = new ObjectNode(myJson,myMap);
    Long gameid = 270909000L;
    GameRepository repo = null;
    GameRestController gc = new GameRestController();
    gc.launchRequest(sessionInfo, gameid);
    assertEquals(null + "\n" + gameid + "\n",
      outputStreamCaptor.toString().trim(),"");
  }
  @AfterEach
  public void tearDown() {
    System.setOut(standardOut);
  }
  @Test
  void quitRequest() {
  }
  @Test
  void endTurn() {
  }
  @Test
  void currentGameBoard() {
  }
  @Test
  void knock() {
  }
}*/


