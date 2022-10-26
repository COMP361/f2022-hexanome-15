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

	@Test
	void testParseOnAuthScript() {
		RunScript runScript = new RunScript(
				"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/auth_token.bash",
				ParseJSON.PARSE_JSON, "http://127.0.0.1:4242", "maex", "abc123_ABC123");
		LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR.execute(runScript);

		assertNotEquals(null, runScript.getOutput());

		String authToken = (String) ParseJSON.PARSE_JSON.getFromKey((JSONObject) runScript.getOutput(), "access_token");

		// this should be proper output; the authToken should correspond directly to the
		// field in output
		System.out.println(runScript.getOutput());
		System.out.println(authToken);

		assertEquals(0, runScript.exitCode());
	}

}
