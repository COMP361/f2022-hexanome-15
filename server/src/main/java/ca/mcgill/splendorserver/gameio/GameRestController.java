package ca.mcgill.splendorserver.gameio;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.lobbyserviceio.Parsejson;
import ca.mcgill.splendorserver.control.LocalGameStorage;
import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.model.SplendorGame;
import com.google.gson.Gson;
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
import org.springframework.web.context.request.async.DeferredResult;

/**
 * GameRestController.
 *
 * @author zacharyhayden
 */
@RestController
public class GameRestController {
  private static final Logger                                         LOGGER
                                                                                      = LoggerFactory.getLogger(
      GameRestController.class);
  private static final String                                         gameServiceLocation
                                                                                      = "http://127.0.0.1:8080";
  //should probably be final
  private final        ca.mcgill.splendorserver.gameio.GameRepository repository;
  // 4 threads for the max 4 players
  private final        ExecutorService                                updaters
                                                                                      = Executors.newFixedThreadPool(
      4);
  private              String                                         gameName;
  private              boolean                                        updateGameBoard = false;
  private              boolean                                        updateAction    = false;
  private              JSONObject                                     adminAuth
                                                                                      = LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR.auth_token(
      "maex", "abc123_ABC123");
  private              String                                         refreshToken
                                                                                      = (String) Parsejson.PARSE_JSON.getFromKey(
      adminAuth, "refresh_token");

  public GameRestController(GameRepository repository) {
    String accessToken = (String) Parsejson.PARSE_JSON.getFromKey(adminAuth, "access_token");
    register_gameservice(accessToken, gameServiceLocation, 4, 2, "splendorBase1", "Splendor", true);
    this.repository = repository;
    System.out.println("in here");
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
   * @return
   */
  private final Object register_gameservice(String accessToken, String gameLocation,
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

    String newUserJSon = new Gson().toJson(acc);

    HttpResponse<String> response1 = Unirest.put(
                                                "http://127.0.0.1:4242/api/users/"
                                                    + gameName
                                                    + "?access_token="
                                                    + accessToken.replace("+", "%2B")
                                            )
                                            .header("Content-Type", "application/json")
                                            .body(newUserJSon)
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
    String newServiceJSon = new Gson().toJson(gs);

    HttpResponse<String> response2 = Unirest.put(
                                                "http://127.0.0.1:4242/api/gameservices/"
                                                    + gameName
                                                    + "?access_token="
                                                    + accessToken.replace("+", "%2B")
                                            )
                                            .header("Content-Type", "application/json")
                                            .body(newServiceJSon)
                                            .asString();
    System.out.println("Response from registration request: " + response2.getBody());
    this.gameName = gameName;
    return null;
  }

  @PutMapping(value = "/api/games/{gameId}", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> launchRequest(@PathVariable long gameId,
                                              @RequestBody SessionInfo sessionInfo
  ) {

    try {
      if (sessionInfo == null || sessionInfo.getGameServer() == null) {
        throw new Exception();
      }
      if (!sessionInfo.getGameServer()
                      .equals(gameName)) {
        throw new Exception();
      }
      //Getting the players in the session
      //gameManager.addGame(gameId, sessionInfo.getPlayers().toArray(new Player[launcherInfo.getPlayers().size()]));
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
    SplendorGame game = repository.findById(gameid)
                                  .orElseThrow(
                                      () -> new GameNotFoundException(
                                          gameid));
    repository.deleteById(gameid);
    LOGGER.info("DELETED GAME ID: " + gameid);
  }

  /*
   * TODO: encoding for the game state. Create initial for when the LS contacts
   * server -> send to all users in the session via get
   */

  /**
   * Sends the gameboard to the server.
   * This is called by LobbyServiceExecutor.
   *
   * @param gameid    The game id
   * @param gameBoard The current gameboard
   */
  @PutMapping("/api/games/{gameid}/gameboard")
  public void setGameBoard(@PathVariable(name = "gameid") Long gameid,
                           @RequestBody String gameBoard
  ) {
    // access the game object from database and then update the reference to the
    // game-board
    SplendorGame game = repository.findById(gameid)
                                  .orElseThrow(
                                      () -> new GameNotFoundException(
                                          gameid));
    //game.updateGameBoard(gameBoard);
    repository.saveAndFlush(game);
    updateGameBoard = true;
  }

  /**
   * Updates the last action of the specified game, flags for game update to be sent.
   *
   * @param gameid The game id
   * @param action The last action that was made
   */
  @PutMapping("/api/games/{gameid}/endturn")
  public void endTurn(@PathVariable(name = "gameid") Long gameid, @RequestBody String action) {
    SplendorGame game = repository.findById(gameid)
                                  .orElseThrow(
                                      () -> new GameNotFoundException(
                                          gameid));
    //game.setLastAction(action);
    repository.saveAndFlush(game);
    updateAction = true;
  }

  /**
   * Gets the last action that a player made.
   *
   * @param gameid The game id
   * @return The last action
   */
  @GetMapping("/api/games/{gameid}/endturn")
  public DeferredResult<String> getLastAction(@PathVariable(name = "gameid") Long gameid) {
    DeferredResult<String> updatedAction = new DeferredResult<>();
    updaters.execute(() -> {
      while (!updateAction) {
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      SplendorGame game = repository.findById(gameid)
                                    .orElseThrow(
                                        () -> new GameNotFoundException(
                                            gameid));
      //updatedAction.setResult(game.getLastAction());
      updateAction = false;
    });
    return updatedAction;
  }

  //copied from LobbyServiceExecutor, to be refactored later
  private void checkNotNullNotEmpty(String... args) {
    for (String arg : args) {
      assert arg != null && arg.length() != 0 : "Arguments cannot be empty nor null.";
    }
  }

  @GetMapping("/api/games/{gameid}/board")
  public ResponseEntity<String> getGameBoard(@PathVariable long gameid) {
    Optional<SplendorGame> manager = LocalGameStorage.getActiveGame(gameid);
    if (manager.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
                           .build();
    } else {
      String json = new Gson().toJson(manager.get()
                                             .getBoard());
      return ResponseEntity.status(HttpStatus.OK)
                           .body(json);
    }
  }

  /**
   * Returns the current gameboard.
   *
   * @param gameid The game id
   * @return the current gameboard
   */
  @GetMapping("/api/games/{gameid}/gameboard")
  public DeferredResult<String> currentGameBoard(@PathVariable Long gameid) {
    DeferredResult<String> updatedGameBoard = new DeferredResult<>();
    updaters.execute(() -> {
      while (!updateGameBoard) {
        // just wait
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      // TODO: implement
      SplendorGame game = repository.findById(gameid)
                                    .orElseThrow(
                                        () -> new GameNotFoundException(
                                            gameid));
      //updatedGameBoard.setResult(game.getGameBoard());
      updateGameBoard = false;
    });

    return updatedGameBoard;

  }


  @GetMapping("/api/knock")
  String knock() {
    LOGGER.info("SOMEONE'S KNOCKING");
    return "SOMEONE'S KNOCKING";
  }

}
