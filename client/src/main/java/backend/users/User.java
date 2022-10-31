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

import backend.lobbyservice.LobbyServiceExecutor;
import backend.lobbyservice.ParseJSON;
import static backend.lobbyservice.LobbyServiceExecutor.getLobbyServiceExecutor;

/**
 * @author zacharyhayden
 * @implNote Flyweight design pattern
 */
public class User {
	private String aAccessToken;
	private final String aRefreshToken;
	private final Role aRole;
	private long aExpiresIn = 1800000L; // time in milliseconds until the users access token expires

	private final static HashMap<String, User> USERS = new HashMap<>();

	private User(String pAcessToken, String pRefreshToken, Role pRole) {
		aAccessToken = pAcessToken;
		aRefreshToken = pRefreshToken;
		aRole = pRole;

		new Timer().scheduleAtFixedRate(new RenewAccessToken(), 0, aExpiresIn); // every 30 minutes (when the users
																				// access
																				// token expires)
	}

	// flyweight constructor
	public static User newUser(String pAcessToken, String pRefreshToken, Role pRole) {
		if (!USERS.containsKey(pRefreshToken)) {
			USERS.put(pRefreshToken, new User(pAcessToken, pRefreshToken, pRole));
		}
		return USERS.get(pRefreshToken);
	}

	/**
	 * Static initialization of LS Executor, and default admin user
	 */
	static {
		// creates a user object for the default admin of the LS: maex, abc123_ABC123
		// creates the lobby service executor used across entire project
		LobbyServiceExecutor ls = getLobbyServiceExecutor("http://127.0.0.1:4242",
				"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/");
		JSONObject auth = ls.auth_token("maex", "abc123_ABC123");
		newUser((String) ParseJSON.PARSE_JSON.getFromKey(auth, "access_token"),
				(String) ParseJSON.PARSE_JSON.getFromKey(auth, "refresh_token"), Role.ADMIN);
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
			JSONObject renewedTokens = getLobbyServiceExecutor(null, null).renew_auth_token(aRefreshToken);
			aAccessToken = (String) ParseJSON.PARSE_JSON.getFromKey(renewedTokens, "access_token");
			aExpiresIn = (long) ParseJSON.PARSE_JSON.getFromKey(renewedTokens, "expires_in");
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
