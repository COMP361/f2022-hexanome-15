/**
 * Nov 23, 2022
 * TODO
 */
package ca.mcgill.splendorserver.games;

/**
 * @author zacharyhayden
 *
 */
public class GameNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GameNotFoundException(Long pGameid) {
		super("Could not find game ID: " + pGameid);
	}

}
