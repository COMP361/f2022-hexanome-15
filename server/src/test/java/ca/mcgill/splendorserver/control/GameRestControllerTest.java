package ca.mcgill.splendorserver.control;


import ca.mcgill.splendorserver.control.GameRestController;
import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;

import java.util.*;

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


