/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

/**
 * @author zacharyhayden
 *
 */
class ParseOutputTest {
	private final LobbyServiceExecutor ls = new LobbyServiceExecutor("http://127.0.0.1:4242",
			"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/");

	@Test
	void testParseOnAuthScript() {
		JSONObject runScript = ls.auth_token("maex", "abc123_ABC123");

		assertNotEquals(null, runScript);

		String authToken = (String) ParseJSON.PARSE_JSON.getFromKey(runScript, "access_token");

		// this should be proper output; the authToken should correspond directly to the
		// field in output
		System.out.println(runScript);
		System.out.println(authToken);

	}

}
