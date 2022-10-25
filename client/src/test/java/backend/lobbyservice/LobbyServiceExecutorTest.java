/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static backend.lobbyservice.LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR;;

/**
 * @author zacharyhayden
 *
 */
class LobbyServiceExecutorTest {
	private final LobbyServiceExecutor ls = LOBBY_SERVICE_EXECUTOR;

	@Test
	void runLSDebug() {
		RunScript runScript = new RunScript(
				"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/debug.bash",
				"http://127.0.0.1:4242");
		ls.execute(runScript);
		String out = ParseOutput.parseText(runScript.getOutput());
		System.out.println(out);

		assertEquals(0, runScript.exitCode());

		assertEquals("Lobby Service is happily serving 5 users.\n\n", out);
	}

	@Test
	void runLSGetAuth() throws InterruptedException {
		RunScript runScript = new RunScript(
				"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/auth_token.bash",
				"maex", "abc123_ABC123");
		ls.execute(runScript);
		JSONObject out = ParseOutput.parseJson(runScript.getOutput());
		System.out.println(out);
		assertEquals(0, runScript.exitCode());
	}

}
