/**
 * Oct 26, 2022
 * TODO
 */
package backend.users;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import backend.lobbyservice.LobbyServiceExecutor;
import backend.lobbyservice.ParseJSON;

/**
 * @author zacharyhayden
 *
 */
class UserTest {

	private final LobbyServiceExecutor ls = new LobbyServiceExecutor("http://127.0.0.1:4242",
			"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/");

	@Test
	void test() {
		JSONObject runScript = ls.auth_token("maex", "abc123_ABC123");

		assertNotEquals(null, runScript);

		String accessToken = (String) ParseJSON.PARSE_JSON.getFromKey(runScript, "access_token");
		String refreshToken = (String) ParseJSON.PARSE_JSON.getFromKey(runScript, "refresh_token");

		User user = User.newUser(accessToken, refreshToken, Role.ADMIN, ls);

		// TODO
		// this method is hard to test but if you put the timer duration to something
		// much lower like 5 seconds and configure an output you should see there be a
		// change in the access_token every n seconds
	}

}