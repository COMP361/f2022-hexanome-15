/**
 * Oct 24, 2022
 * TODO
 */
package lobbyserviceio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import comp361.f2022hexanome15.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import comp361.f2022hexanome15.splendorclient.lobbyserviceio.Parsejson;

/**
 * @author zacharyhayden
 *
 */
class ParseOutputTest {
	private final LobbyServiceExecutor ls = LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR;

	@Test
	void testParseOnAuthScript() {
		JSONObject runScript = ls.auth_token("maex", "abc123_ABC123");

		assertNotEquals(null, runScript);

		String authToken = (String) Parsejson.PARSE_JSON.getFromKey(runScript, "access_token");

		// this should be proper output; the authToken should correspond directly to the
		// field in output
		System.out.println(runScript);
		System.out.println(authToken);

	}

}
