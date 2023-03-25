package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.SplendorGame;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
import ca.mcgill.splendorserver.model.tradingposts.Power;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TerminalGameStateManagerTest {

  @Test
  void isTerminalGameState() {
    List<PlayerWrapper> lp = new ArrayList<>();
    PlayerWrapper larry = PlayerWrapper.newPlayerWrapper("Larry");
    PlayerWrapper zach = PlayerWrapper.newPlayerWrapper("Zach");
    List<Player> players = new ArrayList<>();
    Player player1 = new Player("Larry", "orange");
    Player player2 = new Player("Zach", "green");
    players.add(player1);
    players.add(player2);
    SessionInfo si = new SessionInfo("GameServer",players, lp, larry,"1L");
    SplendorGame sg = new SplendorGame(si,1L);
    UserInventory uinv = new UserInventory(larry, Optional.of(CoatOfArmsType.BLUE));
    lp.add(larry);
    lp.add(zach);
    assertEquals(false,TerminalGameStateManager.isTerminalGameState(sg));
    uinv.addPower(Power.GAIN_5_PRESTIGE);
    uinv.addPower(Power.GAIN_5_PRESTIGE);
    uinv.addPower(Power.GAIN_5_PRESTIGE);
    SessionInfo si2 = new SessionInfo("GameServer",players, lp, larry,"2L");
    SplendorGame sg2 = new SplendorGame(si2,2L);
    assertEquals(false,TerminalGameStateManager.isTerminalGameState(sg2));

  }
}