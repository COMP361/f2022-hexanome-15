package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.GameServiceAccountJson;
import ca.mcgill.splendorserver.gameio.GameServiceJson;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  private LobbyServiceExecutorInterface lobbyServiceExecutor;

  /**
   * Creates a Game Rest Controller.
   *
   * @param lobbyServiceExecutor the class that will execute lobby service calls
   */
  public GameRestController(@Autowired LobbyServiceExecutorInterface lobbyServiceExecutor) {
    this.lobbyServiceExecutor = lobbyServiceExecutor;
    this.lobbyServiceExecutor.register_gameservice(4, 2,
        "SplendorOrient", "SplendorOrient", true);
    this.lobbyServiceExecutor.register_gameservice(4, 2,
        "SplendorOrientTradingPosts",
        "SplendorOrientTradingPosts", true);
    this.lobbyServiceExecutor.register_gameservice(4, 2,
        "SplendorOrientCities",
        "SplendorOrientCities", true);
    System.out.println("in here");
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
    lobbyServiceExecutor.save_game(new Gson().toJson(body),
        splendorGame.getSessionInfo().getGameServer(), id);
    System.out.println(new Gson().toJson(gameboardJson));
    return ResponseEntity.status(HttpStatus.OK).build();
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
