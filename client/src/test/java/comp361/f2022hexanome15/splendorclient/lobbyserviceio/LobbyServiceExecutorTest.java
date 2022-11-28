package lobbyserviceio;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;

/**
 * @author zacharyhayden
 *
 */
class LobbyServiceExecutorTest {
	private final LobbyServiceExecutor ls = LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR;

	@Test
	void runLSDebug() {
		String debug = ls.debug();
		System.out.println(debug);
		assertEquals("Lobby Service is happily serving 5 users.\n\n", debug);
	}

	@Test
	void runLSGetAuth1() {
		JSONObject auth = ls.auth_token("maex", "abc123_ABC123");

		System.out.println(auth);
	}

	@Test
	void runLSGetAuth2() {
		JSONObject auth = ls.auth_token("maex", "abc123_ABC123");

		System.out.println(auth);
	}

//	@Test
//	void runGetUser() throws UnsupportedEncodingException {
//		JSONObject user = ls.get_user("maex", URLEncoder.encode("NfrUufyuEnprhECtXHXW/a0lLuA=", "UTF-8"));
//		System.out.println(user);
//	}

}
