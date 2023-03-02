package ca.mcgill.splendorserver.games;

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

  /*@Test
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
  }*/
}
