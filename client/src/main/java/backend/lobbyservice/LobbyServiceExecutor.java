/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.json.JSONObject;

/**
 * @author zacharyhayden
 */
public class LobbyServiceExecutor {
	private final String aLobbyServiceLocation; // location of the running lobby service (ex http.127.0.0.1:4242)
	private final String aScriptsDir; // location of bash scripts directory

	/**
	 * @param aLobbyServiceLocation location of running lobby service
	 * @param aScriptsDir           path to the directory housing scripts to run
	 * @assert aLobbyServiceLocation != null && aScriptsDir != null &&
	 *         aLobbyServiceLocation.length() != 0 && aScriptsDir.length() != 0
	 */
	public LobbyServiceExecutor(String aLobbyServiceLocation, String aScriptsDir) {
		assert aLobbyServiceLocation != null && aScriptsDir != null && aLobbyServiceLocation.length() != 0
				&& aScriptsDir.length() != 0;
		this.aLobbyServiceLocation = aLobbyServiceLocation;
		this.aScriptsDir = aScriptsDir;
	}

	public final String debug() {
		String output = (String) run(makeRunCommand("debug.bash", aLobbyServiceLocation), ParseText.PARSE_TEXT);
		return output;
	}

	public final void add_player_to_session(String accessToken, String sessionID, String userName) {
		run(makeRunCommand("add_player_to_session", aLobbyServiceLocation, accessToken, sessionID, userName),
				NullParser.NULLPARSER);
	}

	public final JSONObject auth_role(String accessToken) {
		JSONObject output = (JSONObject) run(makeRunCommand("auth_role.bash", aLobbyServiceLocation, accessToken),
				ParseJSON.PARSE_JSON);
		return output;
	}

	public final JSONObject auth_token(String userName, String password) {
		JSONObject output = (JSONObject) run(
				makeRunCommand("auth_token.bash", aLobbyServiceLocation, userName, password), ParseJSON.PARSE_JSON);
		return output;
	}

	public final long create_session(String accessToken, String createrUserName, String gameName, String saveGame) {
		checkNotNullNotEmpty(accessToken, createrUserName, gameName, saveGame);

		long output;
		if (saveGame == null) {
			output = (long) run(makeRunCommand("create_session.bash", aLobbyServiceLocation, accessToken,
					createrUserName, gameName), ParseText.PARSE_TEXT);
		} else {
			output = (long) run(makeRunCommand("create_session.bash", aLobbyServiceLocation, accessToken,
					createrUserName, gameName, saveGame), ParseText.PARSE_TEXT);
		}
		return output;
	}

	public final void launch_session(String accessToken, long sessionID) {
		checkNotNullNotEmpty(accessToken);
		run(makeRunCommand("launch_session.bash", aLobbyServiceLocation, accessToken, String.valueOf(sessionID)),
				NullParser.NULLPARSER);
	}

	public final void register_gameservice(String accessToken, String gameLocation, int maxSessionPlayers,
			int minSessionPlayers, String gameName, String diplayName, boolean webSupport) {
		checkNotNullNotEmpty(accessToken, gameLocation, gameName, diplayName);
		run(makeRunCommand("register_gameservice.bash", accessToken, gameLocation, String.valueOf(maxSessionPlayers),
				String.valueOf(minSessionPlayers), gameName, diplayName, String.valueOf(webSupport),
				aLobbyServiceLocation), NullParser.NULLPARSER);
	}

	public final void remove_session(String accessToken, long sessionID) {
		checkNotNullNotEmpty(accessToken);
		run(makeRunCommand("remove_session.bash", aLobbyServiceLocation, accessToken, String.valueOf(sessionID)),
				NullParser.NULLPARSER);
	}

	public final JSONObject renew_auth_token(String refreshToken) {
		checkNotNullNotEmpty(refreshToken);
		JSONObject output = (JSONObject) run(
				makeRunCommand("renew_auth_token.bash", aLobbyServiceLocation, refreshToken), ParseJSON.PARSE_JSON);
		return output;
	}

	public final void unregister_gameservice(String gameName, String accessToken) {
		checkNotNullNotEmpty(gameName, accessToken);
		run(makeRunCommand("unregister_gameservice.bash", gameName, accessToken), NullParser.NULLPARSER);
	}

	private Object run(String[] command, OutputParser pParser) {
		try {
			// execute the command
			Process process = Runtime.getRuntime().exec(command);

			// handle exit code
			int exitCode = process.waitFor();

			// send log message
			logExecution(command, exitCode);

			if (exitCode != 0) {
				// get error message
				InputStream errorStream = process.getErrorStream();
				for (int i = 0; i < errorStream.available(); i++) {
					System.out.println("" + errorStream.read());
				}
				// throw exception if error with the script
				throw new RuntimeException(
						"[WARNING] Process: " + Arrays.toString(command) + " resulted in exit code: " + exitCode);
			}

			// get parsed output; output will be "NULLPARSER" if the parser if NULLParser
			Object output = pParser.parse(process.getInputStream()); // assign global variable

			// kill process
			process.destroy();

			// return parsed output or null
			return output;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}

	private String formatDirectoryName(String pDir) {
		if (pDir.charAt(pDir.length() - 1) != '/') {
			return (pDir += "/");
		}
		return pDir;
	}

	private String[] makeRunCommand(String scriptName, String... args) {
		assert scriptName != null && scriptName.length() != 0;
		String basisCommand = formatDirectoryName(aScriptsDir) + scriptName;

		String[] command;
		if (args == null) {
			command = new String[] { basisCommand };
		} else {
			command = new String[1 + args.length];
			command[0] = basisCommand;
			// fill array with script arguments
			for (int i = 0; i < args.length && (i + 1) < command.length; i++) {
				assert args[i] != null && args[i].length() != 0;
				command[i + 1] = args[i];
			}
		}
		return command;

	}

	private void logExecution(String[] command, int exitCode) {
		String logMsg = "Executed:";
		for (int i = 0; i < command.length; i++) {
			logMsg += (" " + command[i]);
		}
		logMsg += (" -> exit code: " + exitCode);
		System.out.println(logMsg);
	}

	private void checkNotNullNotEmpty(String... args) {
		for (int i = 0; i < args.length; i++) {
			assert args[i] != null && args[i].length() != 0 : "Arguments cannot be empty nor null.";
		}
	}

}
