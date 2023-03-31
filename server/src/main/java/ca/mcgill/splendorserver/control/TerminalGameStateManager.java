package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.model.SplendorGame;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;

/**
 * Static class to provide utility in determining if the current state of a game board is in fact
 * a terminal one in which case the round will finish until the next persons turn would be the
 * creator of the session ie the starting player.
 *
 * @author Zachary Hayden
 */
public final class TerminalGameStateManager {
  private static final int prestigePointsToWin = 15;

  private TerminalGameStateManager() {}

  /**
   * Checks if the given game is in a terminal state.
   *
   * @param game game to check
   * @return if the game is in a terminal state
   */
  public static boolean isTerminalGameState(SplendorGame game) {
    for (UserInventory inventory : game.getBoard()
                                       .getInventories()) {
      if (game.getSessionInfo().getGameServer().equals("SplendorOrientCities")) {
        if (!inventory.getCities().isEmpty()) {
          game.setFinished();
          return true;
        }
      } else {
        if (inventory.getPrestigeWon() >= prestigePointsToWin) {
          game.setFinished();
          return true;
        }
      }
    }
    return false;
  }

}
