/**
 * Nov 22, 2022.
 * TODO
 */

package ca.mcgill.splendorserver.games;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.mcgill.splendorserver.model.Game;

/**
 * GameRepository.
 *
 * @author zacharyhayden
 */
public interface GameRepository extends JpaRepository<Game, Long> {

}
