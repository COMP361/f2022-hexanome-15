/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

/**
 * @author zacharyhayden
 *
 */
class LobbyServiceExecutorTest {
	private final LobbyServiceExecutor ls = new LobbyServiceExecutor("http://127.0.0.1:4242",
			"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/");

	@Test
	void runLSDebug() {
		String debug = ls.debug();
		System.out.println(debug);
		assertEquals("Lobby Service is happily serving 5 users.\n\n", debug);
	}

	@Test
	void runLSGetAuth() {
		JSONObject auth = ls.auth_token("maex", "abc123_ABC123");

		System.out.println(auth);
	}
	
//	@Test
//	void runGetUser() {
//		JSONObject user = ls.get_user("maex", "SORnHCUQIWqgB7f8oFN2PE3qDVo=");
//		System.out.println(user);
//	}

}
