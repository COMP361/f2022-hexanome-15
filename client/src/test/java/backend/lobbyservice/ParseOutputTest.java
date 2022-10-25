/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.InputStream;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author zacharyhayden
 *
 */
class ParseOutputTest {

	private InputStream getOutput() {
		RunScript runScript = new RunScript(
				"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/auth_token.bash",
				"maex", "abc123_ABC123");
		runScript.run();

		assertNotEquals(null, runScript.getOutput());
		return runScript.getOutput();
	}

	@Test
	void testParseOnAuthScript() {
		JSONObject out = ParseOutput.parseJson(getOutput());
		Object authToken = ParseOutput.getFromKey(out, "refresh_token");

		// this should be proper output; the authToken should correspond directly to the
		// field in output
		System.out.println(out);
		System.out.println(authToken);
	}

}
