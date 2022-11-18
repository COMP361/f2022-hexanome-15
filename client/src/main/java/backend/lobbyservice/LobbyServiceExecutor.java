package backend.lobbyservice;

import backend.users.Role;
import backend.users.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import org.json.JSONObject;

/**
 * Executes Lobby Service commands.
 *
 * @author zacharyhayden
 */
public class LobbyServiceExecutor {
  public static final LobbyServiceExecutor LOBBY_SERVICE_EXECUTOR = new LobbyServiceExecutor("http://127.0.0.1:4242",
      "/home/zacharyhayden/Documents/school/mcgill/comp361"
        + "/software/Splendor/f2022-hexanome-15/client/src/main/bash/");

  // location of the running lobby service (ex http.127.0.0.1:4242)
  private final String lobbyServiceLocation;
  // location of bash scripts directory
  private final String scriptsDir;
  private static User ADMIN;

  /**
   * Creates a LobbyServiceExecutor.
   *
   * @param lobbyServiceLocation location of running lobby service
   * @param scriptsDir           path to the directory housing scripts to run
   * @assert aLobbyServiceLocation != null && aScriptsDir != null &&
   *         aLobbyServiceLocation.length() != 0 && aScriptsDir.length() != 0
   */
  private LobbyServiceExecutor(String lobbyServiceLocation, String scriptsDir) {
    assert lobbyServiceLocation != null && scriptsDir != null && lobbyServiceLocation.length() != 0
             && scriptsDir.length() != 0;
    this.lobbyServiceLocation = lobbyServiceLocation;
    this.scriptsDir = scriptsDir;

    // creates a user object for the default admin of the LS: maex, abc123_ABC123
    JSONObject auth = auth_token("maex", "abc123_ABC123");
    ADMIN = User.newUser("maex", (String) Parsejson.PARSE_JSON.getFromKey(auth, "access_token"),
      (String) Parsejson.PARSE_JSON.getFromKey(auth, "refresh_token"), Role.ADMIN);
  }


  /**
   * Debugs a command.
   */
  public final String debug() {
    String output = (String) run(makeRunCommand("debug.bash", lobbyServiceLocation),
        ParseText.PARSE_TEXT);
    return output;
  }

  /**
   * Adds a player to a session.
   *
   * @param accessToken the accessToken of the player
   * @param sessionid the id of the session
   * @param userName the player's username
   */
  public final void add_player_to_session(String accessToken, String sessionid, String userName) {
    run(makeRunCommand("add_player_to_session.bash",
          lobbyServiceLocation, accessToken, sessionid, userName),
        NullParser.NULLPARSER);
  }

  /**
   * Auth role.
   *
   * @param accessToken the accessToken of the user
   */
  public final JSONObject auth_role(String accessToken) {
    JSONObject output = (JSONObject) run(makeRunCommand("auth_role.bash",
          lobbyServiceLocation, accessToken),
        Parsejson.PARSE_JSON);
    return output;
  }

  /**
   * JSON Object.
   *
   * @param userName the username of the user
   * @param password the password of the user
   */
  public final JSONObject auth_token(String userName, String password) {
    JSONObject output = (JSONObject) run(
        makeRunCommand("auth_token.bash",
          lobbyServiceLocation, userName, password), Parsejson.PARSE_JSON);
    return output;
  }

  /**
   * Creates a session.
   *
   * @param accessToken the accessToken of the user
   * @param createUserName the username to be created
   * @param gameName the name of the game
   * @param saveGame the saved game if creating session from saved game
   */
  public final long create_session(String accessToken, String createUserName,
                                    String gameName, String saveGame) {
    checkNotNullNotEmpty(accessToken, createUserName, gameName, saveGame);

    long output;
    if (saveGame == null) {
      output = (long) run(makeRunCommand("create_session.bash", lobbyServiceLocation, accessToken,
        createUserName, gameName), ParseText.PARSE_TEXT);
    } else {
      output = (long) run(makeRunCommand("create_session.bash", lobbyServiceLocation, accessToken,
        createUserName, gameName, saveGame), ParseText.PARSE_TEXT);
    }
    return output;
  }

  /**
   * Launches a session.
   *
   * @param accessToken the accessToken of the user
   * @param sessionid the id of the session
   */
  public final void launch_session(String accessToken, long sessionid) {
    checkNotNullNotEmpty(accessToken);
    run(makeRunCommand("launch_session.bash", lobbyServiceLocation,
          accessToken, String.valueOf(sessionid)),
        NullParser.NULLPARSER);
  }

  /**
   * Registers a game service.
   *
   * @param accessToken the accessToken of the user
   * @param gameLocation the location of the game
   * @param maxSessionPlayers the max amount of players that can be in a session
   * @param minSessionPlayers the min amount of players that can be in a session
   * @param gameName the name of the game
   * @param displayName the name of the display
   * @param webSupport boolean value for webSupport
   */
  public final void register_gameservice(String accessToken, String gameLocation,
                                         int maxSessionPlayers, int minSessionPlayers,
                                         String gameName, String displayName, boolean webSupport) {
    checkNotNullNotEmpty(accessToken, gameLocation, gameName, displayName);
    run(makeRunCommand("register_gameservice.bash",
        accessToken, gameLocation, String.valueOf(maxSessionPlayers),
        String.valueOf(minSessionPlayers), gameName, displayName, String.valueOf(webSupport),
        lobbyServiceLocation), NullParser.NULLPARSER);
  }

  /**
   * Removes a session.
   *
   * @param accessToken the accessToken of the user
   * @param sessionid the id of the session
   */
  public final void remove_session(String accessToken, long sessionid) {
    checkNotNullNotEmpty(accessToken);
    run(makeRunCommand("remove_session.bash", lobbyServiceLocation,
        accessToken, String.valueOf(sessionid)),
        NullParser.NULLPARSER);
  }

  /**
   * Renews authentication token.
   *
   * @param refreshToken the accessToken of the user
   */
  public final JSONObject renew_auth_token(String refreshToken) {
    checkNotNullNotEmpty(refreshToken);
    JSONObject output = (JSONObject) run(
        makeRunCommand("renew_auth_token.bash",
        lobbyServiceLocation, refreshToken), Parsejson.PARSE_JSON);
    return output;
  }

  /**
   * Unregisters game service.
   *
   * @param gameName the name of the game
   * @param accessToken the access token of the user
   */
  public final void unregister_gameservice(String gameName, String accessToken) {
    checkNotNullNotEmpty(gameName, accessToken);
    run(makeRunCommand("unregister_gameservice.bash",
        gameName, accessToken), NullParser.NULLPARSER);
  }

  /**
   * Returns the user with a given username and access token.
   *
   * @param username the username of the user
   * @param accessToken the access token of the user
   */
  public final JSONObject get_user(String username, String accessToken) {
    checkNotNullNotEmpty(username, accessToken);
    String[] command = makeRunCommand("get_user.bash", lobbyServiceLocation, username, accessToken);
    if (run(command, ParseText.PARSE_TEXT).equals(
        "User details can not be queried. No such user.\n")) {
      return null;
    } else {
      return (JSONObject) run(command, Parsejson.PARSE_JSON);
    }

  }

  private Object run(String[] command, OutputParser parser) {
    try {
      // execute the command
      Process process = Runtime.getRuntime().exec(command);

      // handle exit code
      int exitCode = process.waitFor();

      // send log message
      logExecution(command, exitCode);

      if (exitCode != 0) {
        // get error message
        BufferedReader errorStream = new BufferedReader(
            new InputStreamReader(process.getErrorStream()));
        String line;
        System.out.println("Error Stream: \n");
        while ((line = errorStream.readLine()) != null) {
          System.out.println(line);
        }
        errorStream.close();
        // throw exception if error with the script
        throw new RuntimeException(
          "[WARNING] Process: " + Arrays.toString(command) + " resulted in exit code: " + exitCode);
      }

      // get parsed output; output will be "NULLPARSER" if the parser if NULLParser
      Object output = parser.parse(process.getInputStream()); // assign global variable

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

  private String formatDirectoryName(String dir) {
    if (dir.charAt(dir.length() - 1) != '/') {
      return (dir += "/");
    }
    return dir;
  }

  private String[] makeRunCommand(String scriptName, String... args) {
    assert scriptName != null && scriptName.length() != 0;
    String basisCommand = formatDirectoryName(scriptsDir) + scriptName;

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
    for (String arg : args) {
      assert arg != null && arg.length() != 0 : "Arguments cannot be empty nor null.";
    }
  }

  public static User getAdmin() {
    return ADMIN;
  }

}
