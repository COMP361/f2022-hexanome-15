/**
 * Oct 26, 2022
 * TODO
 */
package backend.views;

import java.util.HashMap;
import java.util.Iterator;

import backend.users.User;

/**
 * @author zacharyhayden
 * @apiNote Stores the state of the LobbyService from the perspective of the
 *          game system and the information it'll need to access
 */
public class LobbyServiceView implements View {
	// Map of users currently logged in
	// stores the hash of the refresh token as key
	private static final HashMap<Integer, User> ACTIVE_USERS = new HashMap<>();
	public static final LobbyServiceView LOBBY_SERVICE_VIEW = new LobbyServiceView(); // singleton instance

	private LobbyServiceView() {
	}

	/**
	 * Called when a user is signed in signifying that they're active
	 * 
	 * @param pUser user who just signed in
	 */
	public void addActiveUser(User pUser) {
		ACTIVE_USERS.put(pUser.hashCode(), pUser);
	}

	public boolean containsActiveUser(User pUser) {
		return ACTIVE_USERS.containsKey(pUser.hashCode());
	}

	public void removeActiveUser(User pUser) {
		ACTIVE_USERS.remove(pUser.hashCode());
	}

	@Override
	public Iterator<User> iterator() {
		return ACTIVE_USERS.values().iterator();
	}

}
