/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import static backend.lobbyservice.LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
				ParseText.PARSE_TEXT, "http://127.0.0.1:4242");
		ls.execute(runScript);
		System.out.println(runScript.getOutput());

		assertEquals(0, runScript.exitCode());
		assertEquals("Lobby Service is happily serving 5 users.\n\n", runScript.getOutput());
	}

	@Test
	void runLSGetAuth() throws InterruptedException {
		RunScript runScript = new RunScript(
				"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/auth_token.bash",
				ParseJSON.PARSE_JSON, "http://127.0.0.1:4242", "maex", "abc123_ABC123");
		ls.execute(runScript);
		System.out.println(runScript.getOutput());
		assertEquals(0, runScript.exitCode());
	}

	// TODO: complete below functionality; i got it to work one time only.
//	@Test
//	void runBuildLS() throws InterruptedException {
//		RunScript buildLS = new RunScript(
//				"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/buildLS_dev.bash",
//				"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/LobbyService", "&");
//		ls.execute(buildLS);
//		//String out = ParseOutput.parseText(buildLS.getOutput());
//		//System.out.println(out);
//	}

}
