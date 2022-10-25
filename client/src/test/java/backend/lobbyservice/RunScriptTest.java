/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * @author zacharyhayden
 *
 */
class RunScriptTest {

	@Test
	void testCorrectOutput() throws InterruptedException, IOException {
		RunScript runScript = new RunScript(
				"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/debug.bash",
				"http://127.0.0.1:4242");
		runScript.run();
		int exitCode = runScript.exitCode();
		String output = ParseOutput.parseText(runScript.getOutput());

		assertEquals(0, exitCode);
		assertEquals("Lobby Service is happily serving 5 users.\n\n", output.toString());
	}

}
