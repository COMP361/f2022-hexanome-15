/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Test;

/**
 * @author zacharyhayden
 *
 */
class RunScriptTest {

	@Test
	void testCorrectOutput() throws InterruptedException, IOException {
		Process process = RunScript.exec("/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/debug.bash", "http://127.0.0.1:4242");
		int exitCode = process.waitFor();
		StringBuilder output = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String line;
		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");
		}
		
		assertEquals(0, exitCode);
		assertEquals("Lobby Service is happily serving 5 users.\n\n", output.toString());
	}

}
