package ca.mcgill.splendorserver.gameio;


import ca.mcgill.splendorserver.control.LobbyServiceExecutor;
import ca.mcgill.splendorserver.control.LobbyServiceExecutorInterface;
import ca.mcgill.splendorserver.control.LocalGameStorage;
import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.GameBoardJson;
import ca.mcgill.splendorserver.model.InventoryJson;
import ca.mcgill.splendorserver.model.SplendorGame;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class GameRestControllerTest {
  private LobbyServiceExecutorInterface lsExecutor = Mockito.mock(LobbyServiceExecutor.class);
  private GameRestController controller = new GameRestController(lsExecutor);
  private PlayerWrapper sofia = PlayerWrapper.newPlayerWrapper("Sofia");
  private PlayerWrapper jeff = PlayerWrapper.newPlayerWrapper("Jeff");
  private List<PlayerWrapper> players = new ArrayList<>(List.of(sofia, jeff));
  private Player player1 = new Player("Sofia", "purple");
  private Player player2 = new Player("Jeff", "blue");
  List<Player> playerList = new ArrayList<>(List.of(player1, player2));
  private SessionInfo sessionInfo = new SessionInfo("SplendorOrientCities", playerList, players, sofia,"");

  @Test
  void launchRequestBadRequest() {
    assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).build(),
      controller.launchRequest(5L, null));
  }

  @Test
  void quitRequest() {
    String sessionInfoJson = new Gson().toJson(sessionInfo);
    controller.launchRequest(5L, sessionInfoJson);
    assertEquals(ResponseEntity.status(HttpStatus.OK).build(), controller.launchRequest(5L, sessionInfoJson));
    Optional<SplendorGame> manager = LocalGameStorage.getActiveGame(5L);
    String json = buildGameBoardJson(manager.get().whoseTurn().getName(), manager.get().getBoard());
    assertEquals(ResponseEntity.status(HttpStatus.OK).body(json), controller.getGameBoard(5L));
    controller.quitRequest(5L);
    assertEquals(Optional.empty(), LocalGameStorage.getActiveGame(5L));
  }

  @Test
  void getGameBoardNotFound() {
    assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .build(), controller.getGameBoard(10L));
  }

  @Test
  void knock() {
    assertEquals("SOMEONE'S KNOCKING", controller.knock());
  }

  private String buildGameBoardJson(String whoseTurn, GameBoard gameboard) {
    List<InventoryJson> inventories = new ArrayList<InventoryJson>();
    for (UserInventory inventory : gameboard.getInventories()) {
      Map<TokenType, Integer> purchasedCardCount = new HashMap<TokenType, Integer>();
      for (Map.Entry<TokenType, TokenPile> entry : inventory.getTokenPiles().entrySet()) {
        purchasedCardCount.put(entry.getKey(), inventory.tokenBonusAmountByType(entry.getKey()));
      }
      InventoryJson inventoryJson = new InventoryJson(inventory.getCards(),
        inventory.getTokenPiles(), inventory.getPlayer().getName(),
        inventory.getPrestigeWon(), inventory.getNobles(),
        inventory.getPowers(), inventory.getCoatOfArmsPile(), inventory.getCities(), purchasedCardCount);
      inventories.add(inventoryJson);
    }
    GameBoardJson gameBoardJson = new GameBoardJson("SplendorOrientCities", whoseTurn, inventories,
      gameboard.getDecks(), gameboard.getNobles(),
      gameboard.getCards(), gameboard.getTokenPiles(),
      gameboard.getTradingPostSlots(), gameboard.getCities());
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(gameBoardJson);
  }
}


