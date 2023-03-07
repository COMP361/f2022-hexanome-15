package ca.mcgill.splendorserver.control;

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
  List<PlayerWrapper> lp = new ArrayList<>();
  PlayerWrapper larry = PlayerWrapper.newPlayerWrapper("Larry");
  PlayerWrapper zack = PlayerWrapper.newPlayerWrapper("Zack");
  SessionInfo si = new SessionInfo("GameServer",lp,larry,"1L");
  SplendorGame sg = new SplendorGame(si,1L);
  UserInventory uinv = new UserInventory(larry, Optional.of(CoatOfArmsType.BLUE));


  @Test
  void isTerminalGameState() {
    lp.add(larry); lp.add(zack);
    assertEquals(false,TerminalGameStateManager.isTerminalGameState(sg),"");
    uinv.addPower(Power.GAIN_5_PRESTIGE);
    uinv.addPower(Power.GAIN_5_PRESTIGE);
    uinv.addPower(Power.GAIN_5_PRESTIGE);
    SessionInfo si2 = new SessionInfo("GameServer",lp,larry,"2L");
    SplendorGame sg2 = new SplendorGame(si2,2L);
    assertEquals(false,TerminalGameStateManager.isTerminalGameState(sg2),"");

  }
}