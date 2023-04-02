package ca.mcgill.splendorserver.gameio;


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
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class GameRestControllerTest {

  private GameRestController controller = new GameRestController();
  private PlayerWrapper sofia = PlayerWrapper.newPlayerWrapper("Sofia");
  private PlayerWrapper jeff = PlayerWrapper.newPlayerWrapper("Jeff");
  private List<PlayerWrapper> players = new ArrayList<>(List.of(sofia, jeff));
  private Player player1 = new Player("Sofia", "purple");
  private Player player2 = new Player("Jeff", "blue");
  List<Player> playerList = new ArrayList<>(List.of(player1, player2));
  private SessionInfo sessionInfo = new SessionInfo("SplendorOrientCities", playerList, players, sofia,"");


}


