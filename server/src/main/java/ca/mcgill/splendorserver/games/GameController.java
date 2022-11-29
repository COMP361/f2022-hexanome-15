/**
 * Nov 22, 2022.
 * TODO
 */

package ca.mcgill.splendorserver.games;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

  /**
   * Creates a GameController.
   *
   * @param repository the repository
   */
  public GameController(GameRepository repository) {
    this.repository = repository;
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
