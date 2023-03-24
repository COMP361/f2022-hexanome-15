package ca.mcgill.splendorclient.lobbyserviceio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONObject;

/**
 * Executes Lobby Service commands.
 *
 * @author zacharyhayden
 */
public class LobbyServiceExecutor {
  /**
   * Instance of Lobby Service Executor.
   */
  public static final LobbyServiceExecutor LOBBY_SERVICE_EXECUTOR = new LobbyServiceExecutor("http://127.0.0.1:4242");
  /**
   * The location of the server.
   */
  public static final String SERVERLOCATION = "127.0.0.1:8080";

  // location of the running lobby service (ex http.127.0.0.1:4242)
  private final String lobbyServiceLocation;

  /**
   * Creates a LobbyServiceExecutor.
   *
   * @param lobbyServiceLocation location of running lobby service
   */
  private LobbyServiceExecutor(String lobbyServiceLocation) {
    assert lobbyServiceLocation != null && lobbyServiceLocation.length() != 0;
    this.lobbyServiceLocation = lobbyServiceLocation;
  }

  /**
   * JSON Object.
   *
   * @param userName the username of the user
   * @param password the password of the user
   * @return the output as a JSONObject
   */
  public final JSONObject auth_token(String userName, String password) {
    String command = String.format(
        "curl -X POST " + "--user bgp-client-name:bgp-client-pw "
        + "%s/oauth/token?grant_type=password&username=%s&password=%s",
        lobbyServiceLocation, userName, password);
    return (JSONObject) run(command);
  }

  /**
   * Renews authentication token.
   *
   * @param refreshToken the accessToken of the user
   * @return the output as a JSONObject
   */
  public final JSONObject renew_auth_token(String refreshToken) {
    checkNotNullNotEmpty(refreshToken);
    String command = String.format(
        "curl -X POST " + "--user bgp-client-name:bgp-client-pw "
        + "%s/oauth/token?grant_type=refresh_token&refresh_token=%s",
        lobbyServiceLocation, refreshToken);
    return (JSONObject) run(command);
  }

  private Object run(String command) {
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
        throw new RuntimeException("[WARNING] Process: " + command
                                     + " resulted in exit code: " + exitCode);
      }

      // get parsed output; output will be "NULLPARSER" if the parser if NULLParser
      Object output = ((OutputParser) Parsejson.PARSE_JSON).parse(process.getInputStream());

      // kill process
      process.destroy();

      // return parsed output or null
      return output;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      System.exit(1);
    }

    return null;
  }

  private void logExecution(String command, int exitCode) {
    String logMsg = "Executed: ";
    logMsg += command;
    logMsg += (" -> exit code: " + exitCode);
    System.out.println(logMsg);
  }

  private void checkNotNullNotEmpty(String... args) {
    for (String arg : args) {
      assert arg != null && arg.length() != 0 : "Arguments cannot be empty nor null.";
    }
  }

}
