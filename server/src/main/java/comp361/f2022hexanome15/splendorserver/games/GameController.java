/**
 * Nov 22, 2022
 * TODO
 */
package comp361.f2022hexanome15.splendorserver.games;

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

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author zacharyhayden
 *
 */
@RestController
public class GameController {
	private final static Logger LOGGER = LoggerFactory.getLogger(GameController.class);
	private final GameRepository aRepository;
	private ExecutorService updators = Executors.newFixedThreadPool(4); // 4 threads for the max 4 players
	private boolean updateGameBoard = true;

	public GameController(GameRepository repository) {
		this.aRepository = repository;
	}

	@PutMapping("/api/games/{gameid}")
	void launchRequest(@RequestBody ObjectNode sessionInfo, @PathVariable Long gameid) {

		// TODO: parse the players argument then change the game constructor not to
		// accept null
		System.out.println(sessionInfo);
		// TODO: parse the players to get their names
		System.out.println("players: " + sessionInfo.get("players").get(0));
		// Game newGame = new Game(creator, savegame, gameid, players);
		// save created game object to aRepository
		// aRepository.save(newGame);
		// LOGGER.info("CREATED: " + newGame); // sending log info
	}

	@DeleteMapping("/api/games/{gameid}")
	public void quitRequest(@PathVariable Long gameid) {
		aRepository.deleteById(gameid);
		LOGGER.info("DELETED GAME ID: " + gameid);
	}

	/*
	 * TODO: encoding for the game state. Create initial for when the LS contacts
	 * server -> send to all users in the session via get
	 */

	@PutMapping("api/games/{gameid}/{username}")
	public PlayerWrapper endTurn(@PathVariable(name = "gameid") Long pGameid,
			@PathVariable(name = "username") String pUsername) {
		// access the game object from database and then update the reference to the
		// player not their turn
		Game game = aRepository.findById(pGameid).orElseThrow(() -> new GameNotFoundException(pGameid));
		updateGameBoard = true;
		return game.endTurn();
	}

	@GetMapping("api/games/{gameid}")
	public DeferredResult<GameBoard> currentGameBoard(@PathVariable Long pGameid) {
		DeferredResult<GameBoard> updatedGameBoard = new DeferredResult<>();
		updators.execute(() -> {
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
