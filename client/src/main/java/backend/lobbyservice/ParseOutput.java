/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONObject;

/**
 * @author zacharyhayden
 *
 */
public class ParseOutput {
	/**
	 * Defines how the Process output should be parsed
	 * 
	 * @param pScriptOutput output produced from running a script which results in
	 *                      json formatted output
	 * @return the string (or concatenated string) containing the desired output
	 * @assert pScriptOutput != null
	 */
	public static JSONObject parseJson(Process pScriptOutput) {
		assert pScriptOutput != null;

		StringBuilder output = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(pScriptOutput.getInputStream()));

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
	 * Parses process output for text format
	 * 
	 * @param pScriptOutput output process from runnable
	 * @return textual output returned from the process
	 */
	public static String parseText(Process pScriptOutput) {
		assert pScriptOutput != null;

		StringBuilder output = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(pScriptOutput.getInputStream()));

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

	/**
	 * 
	 * @param pJson parsed json object
	 * @param pKey  string key to search for and get associated value of
	 * @return the returned value corresponding to the key
	 * @assert pJson != null && pKey != null
	 */
	public static Object getFromKey(JSONObject pJson, String pKey) {
		assert pJson != null && pKey != null;
		return pJson.get(pKey);
	}

}
