/**
 * Nov 22, 2022
 * TODO
 */
package comp361.f2022hexanome15.splendorserver.games;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author zacharyhayden
 *
 */
@RestController
public class GameController {
	private final static Logger LOGGER = LoggerFactory.getLogger(GameController.class);
	private final GameRepository aRepository;

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
	void quitRequest(@PathVariable Long gameid) {
		aRepository.deleteById(gameid);
		LOGGER.info("DELETED GAME ID: " + gameid);
	}
	
	

	@GetMapping("/api/debug")
	void debug() {
		LOGGER.info("SOMEONE'S KNOCKING");
	}

}
