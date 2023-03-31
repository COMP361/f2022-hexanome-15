package ca.mcgill.splendorserver.gameio;

import ca.mcgill.splendorserver.control.LocalGameStorage;
import ca.mcgill.splendorserver.control.SaveGameStorage;
import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.GameBoardJson;
import ca.mcgill.splendorserver.model.InventoryJson;
import ca.mcgill.splendorserver.model.SplendorGame;
import ca.mcgill.splendorserver.model.TradingPostJson;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.savegame.DeckJson;
import ca.mcgill.splendorserver.model.savegame.SaveGame;
import ca.mcgill.splendorserver.model.savegame.SaveGameJson;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArms;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("{lobbyservice.location}")
  private        String lobbyServiceLocation = "http://172.17.0.1:4242";
  private static String gameServiceLocation = "http://10.121.47.57:8080"; //"http://splendor_server:4244";

  /**
   * Setter for static field, value is injected from properties file.
   *
   * @param address address of game service.
   */
  @Value("{gameservice.location}")
  public void setGameServiceLocation(String address) {
    GameRestController.gameServiceLocation = address;
  }

  // 4 threads for the max 4 players
  private final ExecutorService updaters     = Executors.newFixedThreadPool(4);
  private       String          gameName;
  private       JSONObject      adminAuth    = auth_token("maex", "abc123_ABC123");
  private       String          refreshToken = (String) getFromKey(adminAuth, "refresh_token");

  /**
   * Creates a GameRestController.
   */
  public GameRestController() {
    String accessToken = (String) getFromKey(adminAuth, "access_token");
    register_gameservice(accessToken, gameServiceLocation, 4, 2,
                         "SplendorOrient", "SplendorOrient", true
    );
    register_gameservice(accessToken, gameServiceLocation, 4, 2,
                         "SplendorOrientTradingPosts", "SplendorOrientTradingPosts", true
    );
    register_gameservice(accessToken, gameServiceLocation, 4, 2,
                         "SplendorOrientCities", "SplendorOrientCities", true
    );
    System.out.println("in here");

  }

  /**
   * Gets auth token from the lobby service.
   *
   * @param username username
   * @param password password
   * @return the json containing object with response
   */
  public JSONObject auth_token(String username, String password) {
    String command = String.format(
        "curl -X POST " + "--user bgp-client-name:bgp-client-pw "
            + "%s/oauth/token?grant_type=password&username=%s&password=%s",
        lobbyServiceLocation, username, password);

    try {
      // execute the command
      Process process = Runtime.getRuntime().exec(command);

      // handle exit code
      int exitCode = process.waitFor();

      java.util.logging.Logger.getAnonymousLogger()
                              .log(Level.INFO, command + " exit code: " + exitCode);

      if (exitCode != 0) {
        // get error message
        BufferedReader errorStream = new BufferedReader(
            new InputStreamReader(process.getErrorStream()));
        String line;
        System.out.println("Error Stream: \n");
        while ((line = errorStream.readLine()) != null) {
          System.out.println(line);
        }
        errorStream.close();
        // throw exception if error with the script
        throw new RuntimeException("[WARNING] Process: " + command
                                       + " resulted in exit code: " + exitCode);
      }

      // get parsed output; output will be "NULLPARSER" if the parser if NULLParser
      // assign global variable
      JSONObject output = (JSONObject) parseJsonOutput(process.getInputStream());

      // kill process
      process.destroy();

      // return parsed output or null

      return output;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      System.exit(1);
    }

    return null;
  }

  /**
   * Gets the specified key from json.
   *
   * @param json json to parse
   * @param key key to get
   * @return the specified key if exists
   */
  public Object getFromKey(JSONObject json, String key) {
    assert json != null && key != null;
    return json.get(key);
  }

  private Object parseJsonOutput(InputStream scriptOutput) {
    assert scriptOutput != null;

    StringBuilder output = new StringBuilder();
    BufferedReader reader = new BufferedReader(new InputStreamReader(scriptOutput));

    String line;
    try {
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return new JSONObject(output.toString());
  }

  private String buildGameBoardJson(String gameName, String whoseTurn, GameBoard gameboard) {
    List<InventoryJson> inventories = new ArrayList<InventoryJson>();
    for (UserInventory inventory : gameboard.getInventories()) {
      Map<TokenType, Integer> purchasedCardCount = new HashMap<TokenType, Integer>();
      for (Map.Entry<TokenType, TokenPile> entry : inventory.getTokenPiles()
                                                            .entrySet()) {
        purchasedCardCount.put(entry.getKey(), inventory.tokenBonusAmountByType(entry.getKey()));
      }
      InventoryJson inventoryJson = new InventoryJson(inventory.getCards(),
                                                      inventory.getTokenPiles(),
                                                      inventory.getPlayer()
                                                               .getName(),
                                                      inventory.getPrestigeWon(),
                                                      inventory.getNobles(),
                                                      inventory.getPowers(),
                                                      inventory.getCoatOfArmsPile(),
                                                      inventory.getCities(), purchasedCardCount
      );
      inventories.add(inventoryJson);
    }
    GameBoardJson gameBoardJson = new GameBoardJson(gameName, whoseTurn, inventories,
                                                    gameboard.getDecks(), gameboard.getNobles(),
                                                    gameboard.getCards(), gameboard.getTokenPiles(),
                                                    gameboard.getTradingPostSlots(),
                                                    gameboard.getCities()
    );
    Gson gson = new GsonBuilder().setPrettyPrinting()
                                 .create();
    return gson.toJson(gameBoardJson);
  }

  private HttpResponse<JsonNode> getRegisteredGameServices() {
    return Unirest.get(lobbyServiceLocation + "/api/gameservices")
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
                                                      lobbyServiceLocation + "/api/users/"
                                                          + gameName
                                                          + "?access_token="
                                                          + accessToken.replace("+", "%2B")
                                                  )
                                                  .header("Content-Type", "application/json")
                                                  .body(newUserjSon)
                                                  .asString();


    adminAuth    = auth_token(gameName, "Antichrist1!");
    accessToken  = (String) getFromKey(adminAuth, "access_token");
    refreshToken = (String) getFromKey(adminAuth, "refresh_token");
    GameServiceJson
        gs = new GameServiceJson(
        gameName,
        displayName,
        gameServiceLocation,
        "2",
        "4",
        "true"
    );

    System.out.println("Response from service user registration: " + response1.getBody());
    String newServicejSon = new Gson().toJson(gs);

    HttpResponse<String> response2 = Unirest.put(
                                                lobbyServiceLocation + "/api/gameservices/"
                                                    + gameName
                                                    + "?access_token="
                                                    + accessToken.replace("+", "%2B")
                                            )
                                            .header("Content-Type", "application/json")
                                            .body(newServicejSon)
                                            .asString();
    System.out.println("Response from registration request: " + response2.getBody());
  }

  /**
   * Sends a launch request to the server and launches the session.
   *
   * @param gameId          The game id of the session to be launched
   * @param sessionInfoJson The session info of the session to be launched
   * @return a response entity determining if the request was successful
   */
  @PutMapping(value = "/api/games/{gameId}", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> launchRequest(@PathVariable long gameId,
                                              @RequestBody String sessionInfoJson
  ) {

    SessionInfo sessionInfo = new Gson().fromJson(sessionInfoJson, SessionInfo.class);
    System.out.println(sessionInfoJson);
    try {
      if (sessionInfo == null || sessionInfo.getGameServer() == null) {
        throw new Exception();
      }
      // Getting the players in the session
      sessionInfo.populatePlayerWrappers();
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
   * Route for registering a savegame.
   *
   * @param gameId to identify the game that requested this save
   * @return the results of the savegame request
   */
  @PutMapping(value = "/api/games/{gameId}/savegame")
  public ResponseEntity<String> saveGame(@PathVariable long gameId) {
    SplendorGame splendorGame = LocalGameStorage.getActiveGame(gameId).get();
    //refresh registrar token or just log in again actually with gameName Antichrist! account
    final JSONObject adminAuth = auth_token(
            splendorGame.getSessionInfo().getGameServer(), "Antichrist1!");
    //grab the game model, generate a savegameid and add it to an in-memory json "db"
    List<InventoryJson> inventoriesJson = new ArrayList<>();
    GameBoard gameboard = splendorGame.getBoard();
    for (UserInventory inventory : gameboard.getInventories()) {
      InventoryJson inventoryJson = new InventoryJson(inventory.getCards(), 
            inventory.getTokenPiles(), inventory.getPlayer().getName(), 
            inventory.getPrestigeWon(), inventory.getNobles(), 
            inventory.getPowers(), inventory.getCoatOfArmsPile(),
            inventory.getCities(), null);
      inventoriesJson.add(inventoryJson);
    }
    List<DeckJson> decksJson = new ArrayList<>();
    for (Deck deck : gameboard.getDecks()) {
      decksJson.add(new DeckJson(deck));
    }
    List<Integer> nobles = new ArrayList<>();
    for (Noble noble : gameboard.getNobles()) {
      nobles.add(noble.getId());
    }
    List<Integer> cardField = new ArrayList<>();
    for (Card card : gameboard.getCards()) {
      cardField.add(card.getId());
    }
    List<TradingPostJson> tradingPosts = new ArrayList<>();
    for (TradingPostSlot tradingPostSlot : gameboard.getTradingPostSlots()) {
      List<CoatOfArmsType> coatOfArmsTypes = new ArrayList<>();
      for (CoatOfArms coatOfArms : tradingPostSlot.getAcquiredCoatOfArmsList()) {
        coatOfArmsTypes.add(coatOfArms.getType());
      }
      tradingPosts.add(new TradingPostJson(tradingPostSlot.getId(), coatOfArmsTypes));
    }
    List<Integer> cities = new ArrayList<>();
    for (City city : gameboard.getCities()) {
      cities.add(city.getId());
    }
    ca.mcgill.splendorserver.model.savegame.GameBoardJson 
        gameboardJson = 
            new ca.mcgill.splendorserver.model.savegame.GameBoardJson(
                splendorGame.whoseTurn().getName(), inventoriesJson, decksJson,
                nobles, cardField, gameboard.getTokenPiles(), tradingPosts,
                cities);
    String id = String.valueOf(new Random().nextInt() & Integer.MAX_VALUE);
    SaveGame savegame = new SaveGame(id, new Gson().toJson(gameboardJson));
    SaveGameStorage.addSaveGame(savegame);
    //inform lobby service
    List<String> players = new ArrayList<>();
    for (PlayerWrapper player : splendorGame.getSessionInfo().getPlayers()) {
      players.add(player.getName());
    }
    SaveGameJson body = 
        new SaveGameJson(splendorGame.getSessionInfo().getGameServer(), players, id);
    save_game(
        (String) getFromKey(adminAuth, "access_token"),
         new Gson().toJson(body), splendorGame.getSessionInfo().getGameServer(), id);
    System.out.println(new Gson().toJson(gameboardJson));
    return ResponseEntity.status(HttpStatus.OK).build();
  }
  
  private void save_game(String accessToken, String body, String gameserviceName, String id) {
    try {
      String url = 
          String.format(
              "http://127.0.0.1:4242/api/gameservices/%s/savegames/%s?access_token=%s", 
              gameserviceName, id, URLEncoder.encode(accessToken, "UTF-8"));
      
      System.out.println(Unirest.put(url)
                  .header("Content-Type", "application/json")
                  .body(body).asString().getBody());
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Quit request.
   *
   * @param gameid The game id
   */
  @DeleteMapping("/api/games/{gameid}")
  public void quitRequest(@PathVariable Long gameid) {
    LocalGameStorage.removeActiveGame(LocalGameStorage.getActiveGame(gameid)
                                                      .get());
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
      System.out.println("Unable to find game: " + gameid);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
                           .build();
    } else {

      String json = buildGameBoardJson(manager.get().getSessionInfo().getGameServer(),
          manager.get().whoseTurn().getName(), 
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
