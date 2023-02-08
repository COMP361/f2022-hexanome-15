package ca.mcgill.splendorserver.games;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * GameRepository.
 *
 * @author zacharyhayden
 */
public interface GameRepository extends JpaRepository<Game, Long> {

}
