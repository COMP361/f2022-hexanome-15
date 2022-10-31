/**
 * Oct 26, 2022
 * TODO
 */
package backend.users;

import static backend.lobbyservice.LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import backend.lobbyservice.ParseJSON;

/**
 * @author zacharyhayden
 * @implNote Flyweight design pattern
 */
public class User {
	private String aAccessToken;
	private final String aRefreshToken;
	private final Role aRole;
	private int aExpiresIn = 1800000; // time in milliseconds until the users access token expires

	private final static HashMap<String, User> USERS = new HashMap<>();

	private User(String pAcessToken, String pRefreshToken, Role pRole) {
		aAccessToken = pAcessToken;
		aRefreshToken = pRefreshToken;
		aRole = pRole;

		// setting repeating timer process to renew access token once it expires
		new Timer().scheduleAtFixedRate(new RenewAccessToken(), aExpiresIn, aExpiresIn);
	}

	// flyweight constructor
	public static User newUser(String pAcessToken, String pRefreshToken, Role pRole) {
		if (!USERS.containsKey(pRefreshToken)) {
			try {
				USERS.put(URLEncoder.encode(pRefreshToken, "UTF-8"), new User(URLEncoder.encode(pAcessToken, "UTF-8"),
						URLEncoder.encode(pRefreshToken, "UTF-8"), pRole));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		System.out.println("USERS: " + USERS);
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
			JSONObject renewedTokens = LOBBY_SERVICE_EXECUTOR.renew_auth_token(aRefreshToken);
			aAccessToken = (String) ParseJSON.PARSE_JSON.getFromKey(renewedTokens, "access_token");
			aExpiresIn = (int) ParseJSON.PARSE_JSON.getFromKey(renewedTokens, "expires_in");
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
	public String toString() {
		return "User [aAccessToken=" + aAccessToken + ", aRefreshToken=" + aRefreshToken + ", aRole=" + aRole
				+ ", aExpiresIn=" + aExpiresIn + "]";
	}

}
