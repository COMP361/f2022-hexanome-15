/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

/**
 * @author zacharyhayden
 *
 */
class ParseOutputTest {

	ParseOutput parse;

	private Process getOutput() {
		Process process = RunScript.exec(
				"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/auth_token.bash",
				"maex", "abc123_ABC123");
		return process;
	}

	@Test
	void testParseOnAuthScript() {
		JSONObject out = ParseOutput.parse(getOutput());
		Object authToken = ParseOutput.getFromKey(out, "access_token");

		// this should be proper output; the authToken should correspond directly to the
		// field in output
		System.out.println(out);
		System.out.println(authToken);
	}

}
