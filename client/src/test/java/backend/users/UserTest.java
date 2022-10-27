/**
 * Oct 26, 2022
 * TODO
 */
package backend.users;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import backend.lobbyservice.LobbyServiceExecutor;
import backend.lobbyservice.ParseJSON;
import backend.lobbyservice.RunScript;

/**
 * @author zacharyhayden
 *
 */
class UserTest {

	@Test
	void test() {
		RunScript runScript = new RunScript(
				"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/auth_token.bash",
				ParseJSON.PARSE_JSON, "http://127.0.0.1:4242", "maex", "abc123_ABC123");
		LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR.execute(runScript);

		assertNotEquals(null, runScript.getOutput());

		String accessToken = (String) ParseJSON.PARSE_JSON.getFromKey((JSONObject) runScript.getOutput(),
				"access_token");
		String refreshToken = (String) ParseJSON.PARSE_JSON.getFromKey((JSONObject) runScript.getOutput(),
				"refresh_token");

		User user = User.newUser(accessToken, refreshToken, Role.ADMIN);

		// TODO
		// this method is hard to test but if you put the timer duration to something
		// much lower like 5 seconds and configure an output you should see there be a
		// change in the access_token every n seconds
	}

}