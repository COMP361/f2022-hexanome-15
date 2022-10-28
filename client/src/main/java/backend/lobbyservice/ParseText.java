/**
 * Oct 25, 2022
 * TODO
 */
package backend.lobbyservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author zacharyhayden
 *
 */
public class ParseText implements OutputParser {
	public static final ParseText PARSE_TEXT = new ParseText();

	private ParseText() {

	}

	@Override
	/**
	 * Parses process output for text format
	 * 
	 * @param pScriptOutput output process from runnable
	 * @return textual output returned from the process
	 */
	public Object parse(InputStream pScriptOutput) {
		assert pScriptOutput != null;

		StringBuilder output = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(pScriptOutput));

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return output.toString();
	}

}
