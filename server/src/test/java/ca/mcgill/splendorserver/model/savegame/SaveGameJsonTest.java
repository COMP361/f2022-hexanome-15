package ca.mcgill.splendorserver.model.savegame;

import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.SplendorGame;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaveGameJsonTest {

  @Test
  void createSaveGameJson() {
    PlayerWrapper sofia = PlayerWrapper.newPlayerWrapper("Sofia");
    PlayerWrapper jeff = PlayerWrapper.newPlayerWrapper("Jeff");
    Player player1 = new Player("Sofia", "purple");
    Player player2 = new Player("Jeff", "blue");
    List<Player> playerList = new ArrayList<>();
    playerList.add(player1);
    playerList.add(player2);
    List<PlayerWrapper> players = new ArrayList<>();
    players.add(sofia);
    players.add(jeff);
    SessionInfo sessionInfo = new SessionInfo("SplendorOrientTradingPosts", playerList, players, sofia,"");
    SplendorGame game = new SplendorGame(sessionInfo, 1L);
    List<String> playerNames = new ArrayList<>();
    for (Player player : playerList) {
      playerNames.add(player.getName());
    }
    SaveGameJson body = new SaveGameJson(game.getSessionInfo().getGameServer(), playerNames, "1L");

    assertEquals(game.getSessionInfo().getGameServer(), body.gamename);
    assertEquals(playerNames, body.players);
    assertEquals("1L", body.savegameid);
  }

}