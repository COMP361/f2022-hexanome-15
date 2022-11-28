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
  private ExecutorService updaters =
      Executors.newFixedThreadPool(4); // 4 threads for the max 4 players
  private boolean updateGameBoard = true;

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
   * Ends a player's turn and returns the player whose turn it is.
   *
   * @param gameid The game id
   * @param username The player's username
   * @return The player whose turn it is
   */
  @PutMapping("api/games/{gameid}/{username}")
  public PlayerWrapper endTurn(@PathVariable(name = "gameid") Long gameid,
                               @PathVariable(name = "username") String username) {
    // access the game object from database and then update the reference to the
    // player not their turn
    Game game = repository.findById(gameid).orElseThrow(() -> new GameNotFoundException(gameid));
    updateGameBoard = true;
    return game.endTurn();
  }

  /**
   * Returns the current gameboard.
   *
   * @param gameid The game id
   * @return the current gameboard
   */
  @GetMapping("api/games/{gameid}")
  public DeferredResult<GameBoard> currentGameBoard(@PathVariable Long gameid) {
    DeferredResult<GameBoard> updatedGameBoard = new DeferredResult<>();
    updaters.execute(() -> {
      while (!updateGameBoard) {

      }
      // TODO: implement
      updatedGameBoard.setResult(null);
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
