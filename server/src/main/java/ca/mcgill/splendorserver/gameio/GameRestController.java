package ca.mcgill.splendorserver.gameio;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.lobbyserviceio.Parsejson;
import ca.mcgill.splendorserver.control.LocalGameStorage;
import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.GameBoardJson;
import ca.mcgill.splendorserver.model.InventoryJson;
import ca.mcgill.splendorserver.model.SplendorGame;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * GameRestController.
 *
 * @author zacharyhayden
 */
@RestController
public class GameRestController {
  private static final Logger LOGGER = LoggerFactory.getLogger(GameRestController.class);
  private static final String gameServiceLocation = "http://127.0.0.1:8080";
  // 4 threads for the max 4 players
  private final ExecutorService updaters = Executors.newFixedThreadPool(4);
  private String gameName;
  private JSONObject adminAuth = LobbyServiceExecutor
                                   .LOBBY_SERVICE_EXECUTOR.auth_token("maex", "abc123_ABC123");
  private String refreshToken = (String) Parsejson
                                           .PARSE_JSON.getFromKey(adminAuth, "refresh_token");

  /**
   * Creates a GameRestController.
   *
   */
  public GameRestController() {
    String accessToken = (String) Parsejson.PARSE_JSON.getFromKey(adminAuth, "access_token");
    register_gameservice(accessToken, gameServiceLocation, 4, 2,
        "SplendorOrient", "Splendor", true);
    //register_gameservice(accessToken, gameServiceLocation, 4, 2,
    //   "SplendorOrient+TradingPosts", "Splendor", true);
    //register_gameservice(accessToken, gameServiceLocation, 4, 2,
    //"SplendorOrient+Cities", "Splendor", true);    
    /*debugging
    List<PlayerWrapper> wrappers =
        Arrays.asList(new PlayerWrapper[] {new PlayerWrapper("foo"), new PlayerWrapper("baz")});
    SplendorGame splendorGame = new SplendorGame(new SessionInfo(wrappers), 0);
    LocalGameStorage.addActiveGame(splendorGame);
    Optional<SplendorGame> manager = LocalGameStorage.getActiveGame(0);
    String json = buildGameBoardJson(manager.get().getBoard());
    System.out.println(json);*/
  }
  
  private String buildGameBoardJson(String whoseTurn, GameBoard gameboard) {
    List<InventoryJson> inventories = new ArrayList<InventoryJson>();
    for (UserInventory inventory : gameboard.getInventories()) {
      InventoryJson inventoryJson = new InventoryJson(inventory.getCards(), 
          inventory.getTokenPiles(), inventory.getPlayer().getName(), 
          inventory.getPrestigeWon(), inventory.getNobles(), 
          inventory.getPowers(), inventory.getCoatOfArmsPile());
      inventories.add(inventoryJson);
    }
    GameBoardJson gameBoardJson = new GameBoardJson(whoseTurn, inventories, 
        gameboard.getDecks(), gameboard.getNobles(), 
        gameboard.getCards(), gameboard.getTokenPiles());
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(gameBoardJson);
  }

  private HttpResponse<JsonNode> getRegisteredGameServices() {
    return Unirest.get("http://127.0.0.1:4242/api/gameservices")
                  .header("accept", "application/json")
                  .asJson();
  }

  /**
   * Registers a game service. This should not be called by client and is kept here
   * for reference while changes are made. Servers should register themselves
   * as per LS diagram.
   *
   * @param accessToken       the accessToken of the user
   * @param gameLocation      the location of the game
   * @param maxSessionPlayers the max amount of players that can be in a session
   * @param minSessionPlayers the min amount of players that can be in a session
   * @param gameName          the name of the game
   * @param displayName       the name of the display
   * @param webSupport        boolean value for webSupport
   */
  private final void register_gameservice(String accessToken, String gameLocation,
                                            int maxSessionPlayers,
                                            int minSessionPlayers, String gameName,
                                            String displayName,
                                            boolean webSupport
  ) {
    checkNotNullNotEmpty(accessToken, gameLocation, gameName, displayName);

    System.out.println(getRegisteredGameServices().getBody()
                                                  .toPrettyString());

    GameServiceAccountJson
        acc = new GameServiceAccountJson(
        gameName,
        "Antichrist1!",
        "#000000"
    );

    String newUserjSon = new Gson().toJson(acc);

    final HttpResponse<String> response1 = Unirest.put(
                                                "http://127.0.0.1:4242/api/users/"
                                                    + gameName
                                                    + "?access_token="
                                                    + accessToken.replace("+", "%2B")
                                            )
                                            .header("Content-Type", "application/json")
                                            .body(newUserjSon)
                                            .asString();


    adminAuth    = LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR.auth_token(gameName, "Antichrist1!");
    accessToken  = (String) Parsejson.PARSE_JSON.getFromKey(adminAuth, "access_token");
    refreshToken = (String) Parsejson.PARSE_JSON.getFromKey(adminAuth, "refresh_token");
    GameServiceJson
        gs = new GameServiceJson(
        gameName,
        "Splendor",
        "http://127.0.0.1:8080",
        "2",
        "4",
        "true"
    );

    System.out.println("Response from service user registration: " + response1.getBody());
    String newServicejSon = new Gson().toJson(gs);

    HttpResponse<String> response2 = Unirest.put(
                                                "http://127.0.0.1:4242/api/gameservices/"
                                                    + gameName
                                                    + "?access_token="
                                                    + accessToken.replace("+", "%2B")
                                            )
                                            .header("Content-Type", "application/json")
                                            .body(newServicejSon)
                                            .asString();
    System.out.println("Response from registration request: " + response2.getBody());
    this.gameName = gameName;
  }

  /**
   * Sends a launch request to the server and launches the session.
   *
   * @param gameId The game id of the session to be launched
   * @param sessionInfo The session info of the game to be launched
   * @return a response entity determining if the request was successful
   */
  @PutMapping(value = "/api/games/{gameId}", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> launchRequest(@PathVariable long gameId,
                                              @RequestBody String sessionInfoJson
  ) {

    SessionInfo sessionInfo = new Gson().fromJson(sessionInfoJson, SessionInfo.class);
    sessionInfo.populatePlayerWrappers();
    try {
      if (sessionInfo == null || sessionInfo.getGameServer() == null) {
        throw new Exception();
      }
      // Getting the players in the session
      // gameManager.addGame(gameId, sessionInfo.getPlayers()
      // .toArray(new Player[launcherInfo.getPlayers().size()]));
      SplendorGame splendorGame = new SplendorGame(sessionInfo, gameId);
      LocalGameStorage.addActiveGame(splendorGame);
      return ResponseEntity.status(HttpStatus.OK)
                           .build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                           .body(e.getMessage());
    }
  }

  /**
   * Quit request.
   *
   * @param gameid The game id
   */
  @DeleteMapping("/api/games/{gameid}")
  public void quitRequest(@PathVariable Long gameid) {
    LocalGameStorage.removeActiveGame(LocalGameStorage.getActiveGame(gameid).get());
    LOGGER.info("DELETED GAME ID: " + gameid);
  }


  //copied from LobbyServiceExecutor, to be refactored later
  private void checkNotNullNotEmpty(String... args) {
    for (String arg : args) {
      assert arg != null && arg.length() != 0 : "Arguments cannot be empty nor null.";
    }
  }

  /**
   * Retrieves the game board from the game with the given game id from the server.
   *
   * @param gameid the game id of the game being played
   * @return a response entity determining if the request was successful
   */
  @GetMapping("/api/games/{gameid}/board")
  public ResponseEntity<String> getGameBoard(@PathVariable long gameid) {
    Optional<SplendorGame> manager = LocalGameStorage.getActiveGame(gameid);
    if (manager.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
                           .build();
    } else {
      String json = buildGameBoardJson(manager.get().whoseTurn().getName(), 
          manager.get().getBoard());
      return ResponseEntity.status(HttpStatus.OK)
      .body(json);
    }
  }


  @GetMapping("/api/knock")
  String knock() {
    LOGGER.info("SOMEONE'S KNOCKING");
    return "SOMEONE'S KNOCKING";
  }

}
