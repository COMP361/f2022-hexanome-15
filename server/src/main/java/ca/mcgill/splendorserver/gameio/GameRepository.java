package ca.mcgill.splendorserver.gameio;

import ca.mcgill.splendorserver.model.SplendorGame;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * GameRepository.
 *
 * @author zacharyhayden
 */
public interface GameRepository extends JpaRepository<SplendorGame, Long> {

}
