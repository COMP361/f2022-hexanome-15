/**
 * Nov 22, 2022.
 * TODO
 */

package ca.mcgill.splendorserver.games;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.lobbyserviceio.Parsejson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * GameController.
 *
 * @author zacharyhayden
 */
@RestController
public class GameController {
  private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
  private final GameRepository repository;
  // 4 threads for the max 4 players
  private ExecutorService updaters = Executors.newFixedThreadPool(4);
  private boolean updateGameBoard = false;
  private boolean updateAction = false;
  private final String gameServiceLocation = "http://127.0.0.1:8080";//should probably be final
  
  JSONObject adminAuth = LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR.auth_token("maex", "abc123_ABC123");
  String accessToken = (String) Parsejson.PARSE_JSON.getFromKey(adminAuth, "access_token");
  String refreshToken = (String) Parsejson.PARSE_JSON.getFromKey(adminAuth, "refresh_token");

  
  public GameController(GameRepository repository) {
    //accessToken = accessToken.substring(0, accessToken.length()-1);
    register_gameservice(accessToken, gameServiceLocation, 4, 2, "splendorBase1", "Splendor", true);
    //create_session(accessToken, "maex", "splendorBase1", "");
    //get_session("136528857923959438");
    //delete_session("8657865879198511197", accessToken);
    //launch_session("136528857923959438", accessToken);
    //get_session("136528857923959438");
    this.repository = repository;
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
  private final Object register_gameservice(String accessToken, String gameLocation, int maxSessionPlayers,
      int minSessionPlayers, String gameName, String displayName, boolean webSupport) {
    checkNotNullNotEmpty(accessToken, gameLocation, gameName, displayName);
    
    GameServiceAccountJson acc = new GameServiceAccountJson(
          gameName,
          "Antichrist1!",
          "#000000"
        );
    
    String newUserJSon = new Gson().toJson(acc);
    
    HttpResponse<String> response1 = Unirest.put(
          "http://127.0.0.1:4242/api/users/"
          + gameName
          + "?access_token="
          + accessToken
        )
        .header("Content-Type", "application/json")
        .body(newUserJSon)
        .asString();
    
    

    adminAuth = LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR.auth_token(gameName, "Antichrist1!");
      accessToken = (String) Parsejson.PARSE_JSON.getFromKey(adminAuth, "access_token");
      refreshToken = (String) Parsejson.PARSE_JSON.getFromKey(adminAuth, "refresh_token");
      GameServiceJson gs = new GameServiceJson(
            gameName,
            "Splendor",
            "http://127.0.0.1:8080",
            "2",
            "4",
            "true"
          );

      System.out.println(response1.getBody());
      String newServiceJSon = new Gson().toJson(gs);
      
      HttpResponse<String> response2 = Unirest.put(
        "http://127.0.0.1:4242/api/gameservices/"
        + gameName
        + "?access_token="
        + accessToken
      )
      .header("Content-Type", "application/json")
      .body(newServiceJSon)
        .asString();
      System.out.println(response2.getBody());
    return null;
    
    /*accessToken = accessToken.replaceAll("\\+", "\\\\+");
    //registers a user with ROLE_SERVICE
    String command = String.format(
        "curl -X PUT --header \"Content-Type:application/json\" --data "
            + "\"{ \\\"name\\\": \\\"%s\\\",\\\"password\\\": \\\"Antichrist1!\\\",\\\"preferredColour\\\": \\\"#000000\\\","
            + "\\\"role\\\": \\\"ROLE_SERVICE\\\"}\" "
            + "\"%s/api/users/%s?access_token=%s\"",
        gameName, "http://127.0.0.1:4242", gameName, accessToken);
    run(command, ParseText.PARSE_TEXT);
    adminAuth = LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR.auth_token(gameName, "Antichrist1!");
      accessToken = (String) Parsejson.PARSE_JSON.getFromKey(adminAuth, "access_token");
      refreshToken = (String) Parsejson.PARSE_JSON.getFromKey(adminAuth, "refresh_token");
    
    //System.out.println(command);
    //actually registers the gameservice
    command = String.format(
        "curl -X PUT --header \"Content-Type:application/json\" --data "
            + "\"{ \\\"name\\\": \\\"%s\\\",\\\"displayName\\\": \\\"%s\\\",\\\"location\\\": \\\"%s\\\","
            + "\\\"minSessionPlayers\\\": %s,\\\"maxSessionPlayers\\\": %s, \\\"webSupport\\\": \\\"%s\\\" }\" "
            + "\"%s/api/gameservices/%s?access_token=%s\"",
        gameName, displayName, gameLocation, String.valueOf(minSessionPlayers),
        String.valueOf(maxSessionPlayers), String.valueOf(webSupport), "http://127.0.0.1:4242", gameName, accessToken);
    //System.out.println(command);
    return run(command, ParseText.PARSE_TEXT);*/
  }

  @PutMapping("/api/games/{gameid}")
  void launchRequest(@RequestBody ObjectNode sessionInfo, @PathVariable Long gameid) {

    // TODO: parse the players argument then change the game constructor not to
    // accept null
    System.out.println(sessionInfo);
    // TODO: parse the players to get their names
    System.out.println("players: " + sessionInfo.get("players").get(0));
    // Game newGame = new Game(creator, savegame, gameid, players);
    // save created game object to repository
    // repository.save(newGame);
    // LOGGER.info("CREATED: " + newGame); // sending log info
  }

  /**
   * Quit request.
   *
   * @param gameid The game id
   */
  @DeleteMapping("/api/games/{gameid}")
  public void quitRequest(@PathVariable Long gameid) {
    Game game = repository.findById(gameid).orElseThrow(() -> new GameNotFoundException(gameid));
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
   * @param gameid The game id
   * @param gameBoard The current gameboard
   */
  @PutMapping("api/games/{gameid}/gameboard")
  public void setGameBoard(@PathVariable(name = "gameid") Long gameid,
                           @RequestBody String gameBoard) {
    // access the game object from database and then update the reference to the
    // game-board
    Game game = repository.findById(gameid).orElseThrow(() -> new GameNotFoundException(gameid));
    game.updateGameBoard(gameBoard);
    repository.saveAndFlush(game);
    updateGameBoard = true;
  }

  /**
   * Updates the last action of the specified game, flags for game update to be sent.
   *
   * @param gameid The game id
   * @param action The last action that was made
   */
  @PutMapping("api/games/{gameid}/endturn")
  public void endTurn(@PathVariable(name = "gameid") Long gameid, @RequestBody String action) {
    Game game = repository.findById(gameid).orElseThrow(() -> new GameNotFoundException(gameid));
    game.setLastAction(action);
    repository.saveAndFlush(game);
    updateAction = true;
  }

  /**
   * Gets the last action that a player made.
   *
   * @param gameid The game id
   * @return The last action
   */
  @GetMapping("api/games/{gameid}/endturn")
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
      Game game = repository.findById(gameid).orElseThrow(() -> new GameNotFoundException(gameid));
      updatedAction.setResult(game.getLastAction());
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

  /**
   * Returns the current gameboard.
   *
   * @param gameid The game id
   * @return the current gameboard
   */
  @GetMapping("api/games/{gameid}/gameboard")
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
      Game game = repository.findById(gameid).orElseThrow(() -> new GameNotFoundException(gameid));
      updatedGameBoard.setResult(game.getGameBoard());
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
