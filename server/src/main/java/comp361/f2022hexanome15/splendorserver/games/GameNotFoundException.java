/**
 * Nov 23, 2022
 * TODO
 */
package comp361.f2022hexanome15.splendorserver.games;

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
