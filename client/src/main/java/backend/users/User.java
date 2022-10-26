/**
 * Oct 26, 2022
 * TODO
 */
package backend.users;

import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import static backend.lobbyservice.LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR;
import backend.lobbyservice.ParseJSON;
import backend.lobbyservice.RunScript;

/**
 * @author zacharyhayden
 * @implNote Flyweight design pattern
 */
public class User {
	private String aAccessToken;
	private final String aRefreshToken;
	private final Role aRole;

	private final static HashMap<String, User> USERS = new HashMap<>();

	private User(String pAcessToken, String pRefreshToken, Role pRole) {
		aAccessToken = pAcessToken;
		aRefreshToken = pRefreshToken;
		aRole = pRole;

		new Timer().scheduleAtFixedRate(new RenewAccessToken(), 0, 1800000); // every 30 minutes (when the users access
																				// token expires)
	}

	// flyweight constructor
	public User newUser(String pAcessToken, String pRefreshToken, Role pRole) {
		if (!USERS.containsKey(pRefreshToken)) {
			USERS.put(pRefreshToken, new User(pAcessToken, pRefreshToken, pRole));
		}
		return USERS.get(pRefreshToken);
	}

	/**
	 * Inner class to update the access token of the user using their refresh token
	 * 
	 * @author zacharyhayden
	 *
	 */
	private class RenewAccessToken extends TimerTask {

		@Override
		/**
		 * Runs the renew access token script at regular intervals when it expires
		 * (every 30 minutes or 1800 seconds)
		 */
		public void run() {
			RunScript renewAccessToken = new RunScript(
					"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/renew_auth_token.bash",
					ParseJSON.PARSE_JSON, "http://127.0.0.1:4242", aRefreshToken);
			LOBBY_SERVICE_EXECUTOR.execute(renewAccessToken);
			aAccessToken = (String) ParseJSON.PARSE_JSON.getFromKey((JSONObject) renewAccessToken.getOutput(),
					"access_token");
		}

	}

	public String getAccessToken() {
		return aAccessToken;
	}

	public Role getRole() {
		return aRole;
	}

	@Override
	public int hashCode() {
		return Objects.hash(aRefreshToken);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(aRefreshToken, other.aRefreshToken);
	}

}
