/**
 * Oct 25, 2022
 * TODO
 */
package backend.lobbyservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

/**
 * @author zacharyhayden
 *
 */
public class ParseJSON implements OutputParser {
	public static final ParseJSON PARSE_JSON = new ParseJSON();

	private ParseJSON() {

	}

	@Override
	/**
	 * Defines how the Process output should be parsed
	 * 
	 * @param pScriptOutput output produced from running a script which results in
	 *                      json formatted output
	 * @return the string (or concatenated string) containing the desired output
	 * @assert pScriptOutput != null
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
			e.printStackTrace();
			System.exit(1);
		}
		return new JSONObject(output.toString());
	}

	/**
	 * 
	 * @param pJson parsed json object, will require a cast when used with output
	 *              from executing
	 * @param pKey  string key to search for and get associated value of
	 * @return the returned value corresponding to the key
	 * @assert pJson != null && pKey != null
	 */
	public Object getFromKey(JSONObject pJson, String pKey) {
		assert pJson != null && pKey != null;
		return pJson.get(pKey);
	}

}
