/**
 * Oct 31, 2022
 * TODO
 */
package backend.lobbyservice;

import java.util.Optional;

/**
 * @author zacharyhayden
 *
 */
public class Session {
	private final String aCreator; // username of creator user
	private final Optional<String> aSaveGame;
	private final Optional<String> aTitle;
	private final long aSessionID = -1; // TODO: implement this from the LS

	/**
	 * 
	 * @param pCreator creator of the session: user who presses create session
	 * @param pSaveGame optional fork of previously saved game; can be null
	 */
	public Session(String pCreator, String pSaveGame, String pTitle) {
		assert pCreator != null && pCreator.length() != 0;
		aCreator = pCreator;
		aSaveGame = Optional.ofNullable(pSaveGame);
		aTitle = Optional.ofNullable(pTitle);
	}

	@Override
	public String toString() {
		if (aTitle.isPresent()) {
			return "Session " + aTitle.get() + " : created by " + aCreator;
		}
		return "Session created by " + aCreator;
	}

}
