package ca.mcgill.splendorserver.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Executes Lobby Service commands.
 *
 * @author zacharyhayden
 */
@Component
public class LobbyServiceExecutor {

  /**
   * Instance of Lobby Service Executor.
   */
  public static final LobbyServiceExecutor LOBBY_SERVICE_EXECUTOR = new LobbyServiceExecutor();
  /**
   * The location of the server.
   */
  public static String SERVERLOCATION = "http://192.168.2.236:8080"; // TODO: fix the value injection;

  /**
   * Sets the game service location from properties.
   *
   * @param location location to set.
   */
  @Value("{gameservice.location}")
  public void setServerLocation(String location) {
    LobbyServiceExecutor.SERVERLOCATION = location;
  }

  // location of the running lobby service (ex http.127.0.0.1:4242)
  @Value("{lobbyservice.location}")
  private String lobbyServiceLocation = "http://192.168.2.236:4242"; // TODO: fix the value injection;

  /**
   * Creates a LobbyServiceExecutor.
   *
   */
  private LobbyServiceExecutor() {
  }


  /**
   * Auth role.
   *
   * @param accessToken the accessToken of the user
   * @return the output of the command as a JSONObject
   */
  public final JSONObject auth_role(String accessToken) {
    String command = String.format("curl -X GET %s/oauth/role?access_token=%s",
                                   lobbyServiceLocation, accessToken);
    JSONObject output = (JSONObject) run(command, Parsejson.PARSE_JSON);
    return output;
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
    JSONObject output = (JSONObject) run(command, Parsejson.PARSE_JSON);
    return output;
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
    JSONObject output = (JSONObject) run(command, Parsejson.PARSE_JSON);
    return output;
  }

  /**
   * Returns the session info as a JSONObject.
   *
   * @param sessionid The id of the session
   * @return the session info
   */
  public JSONObject getSessionInfo(int sessionid) {
    String command = String.format("curl -X GET %s/api/sessions/%s",
                                   lobbyServiceLocation, sessionid);
    JSONObject output = (JSONObject) run(command, Parsejson.PARSE_JSON);
    return output;
  }

  private Object run(String command, Parsejson parser) {
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
