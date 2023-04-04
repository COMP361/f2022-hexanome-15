package ca.mcgill.splendorclient.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import kong.unirest.HttpRequest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ca.mcgill.splendorclient.lobbyserviceio.NullParser;
import ca.mcgill.splendorclient.lobbyserviceio.OutputParser;
import ca.mcgill.splendorclient.lobbyserviceio.ParseText;
import ca.mcgill.splendorclient.lobbyserviceio.Parsejson;


/**
 * Executes Lobby Service commands.
 *
 * @author zacharyhayden
 */
@Component
public class LobbyServiceExecutor {
  
  public static final LobbyServiceExecutor LOBBYSERVICEEXUTOR = new LobbyServiceExecutor();
  
  /**
   * The location of the server.
   */
  public static String SERVERLOCATION = "192.168.2.220:8080"; // TODO: fix the value injection;

  /**
   * Sets the game service location from properties.
   *
   * @param location location to set.
   */
  @Value("{gameservice.location}")
  public void setServerLocation(String location) {
    SERVERLOCATION = location;
  }

  // location of the running lobby service (ex http.127.0.0.1:4242)
  @Value("{lobbyservice.location}")
  private String lobbyServiceLocation = "http://192.168.2.220:4242"; // TODO: fix the value injection;

  /**
   * Creates a LobbyServiceExecutor.
   *
   */
  private LobbyServiceExecutor() {
  }

  /**
   * Debugs a command.
   *
   * @return the output of the command as a string
   */
  public final String debug() {
    String command = String.format("curl -X GET %s/api/online", lobbyServiceLocation);
    String output = (String) run(command, ParseText.PARSE_TEXT);
    return output;
  }
  
  /**
   * Returns the lobby service location.
   *
   * @return the lobby service location
   */
  public final String getLobbyServiceLocation() {
    return lobbyServiceLocation;
  }

  /**
   * Sends the gameboard to the server using a put request.
   *
   * @param gameboard The current gameboard
   * @param gameid The game id
   */
  public final void sendGameboard(String gameboard, int gameid) {
    String command = String.format("curl -X PUT --header 'Content-Type: application/json' "
                                     + "--data {'gameboard':%s} "
                                     + "http://%s/api/games/%s/gameboard",
        gameboard, SERVERLOCATION, gameid);
    run(command, Parsejson.PARSE_JSON);
  }

  /**
   * Adds a player to a session.
   *
   * @param accessToken the accessToken of the player
   * @param sessionid   the id of the session
   * @param userName    the player's username
   */
  public final void add_player_to_session(String accessToken, String sessionid, String userName) {
    String command = String.format("curl -X PUT %s/api/sessions/%s/players/%s?access_token=%s",
        lobbyServiceLocation, sessionid, userName, accessToken);
    run(command, NullParser.NULLPARSER);
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
   * Issues a token.
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
   * Creates a session.
   *
   * @param accessToken    the accessToken of the user
   * @param createUserName the username to be created
   * @param gameName       the name of the game
   * @param saveGame       the saved game if creating session from saved game
   * @return the output of the command as a long
   */
  public final long create_session(String accessToken, String createUserName,
                                   String gameName, String saveGame) {
    checkNotNullNotEmpty(accessToken, createUserName, gameName, saveGame);

    long output;
    if (saveGame == null) {
      String command = String.format(
          "curl -X POST --header 'Content-Type:application/json' "
            + "--data {game:%s, creator:%s} %s/api/sessions?access_token=%s",
          gameName, createUserName, lobbyServiceLocation, accessToken);
      output = (long) run(command, ParseText.PARSE_TEXT);

    } else {
      String command = String.format(
          "curl -X POST --header 'Content-Type:application/json' --data "
          + "{game:%s, creator:%s, savegame:%s} %s/api/sessions?access_token=%s",
          gameName, createUserName, saveGame, lobbyServiceLocation, accessToken);
      output = (long) run(command, ParseText.PARSE_TEXT);
    }
    return output;
  }

  /**
   * Launches a session.
   *
   * @param accessToken the accessToken of the user
   * @param sessionid   the id of the session
   */
  public final void launch_session(String accessToken, long sessionid) {
    checkNotNullNotEmpty(accessToken);
    String command = String.format("curl -X POST %s/api/sessions/%d?access_token=%s",
        lobbyServiceLocation,
        String.valueOf(sessionid), accessToken);
    run(command, NullParser.NULLPARSER);
  }

  /**
   * Registers a game service.
   *
   * @param accessToken       the accessToken of the user
   * @param gameLocation      the location of the game
   * @param maxSessionPlayers the max amount of players that can be in a session
   * @param minSessionPlayers the min amount of players that can be in a session
   * @param gameName          the name of the game
   * @param displayName       the name of the display
   * @param webSupport        boolean value for webSupport
   */
  public final void register_gameservice(String accessToken, String gameLocation,
                                         int maxSessionPlayers,
                                         int minSessionPlayers, String gameName,
                                         String displayName, boolean webSupport) {
    checkNotNullNotEmpty(accessToken, gameLocation, gameName, displayName);
    String command = String.format(
        "curl -X PUT --header 'Content-Type:application/json' --data "
        + "{'name':%s,'displayName':%s,'location':%s,"
        + "'minSessionPlayers':%d,'maxSessionPlayers':%d, 'webSupport':%d} "
        + "%s/api/gameservice/$name?access_token=%s",
        gameName, displayName, gameLocation, String.valueOf(minSessionPlayers),
        String.valueOf(maxSessionPlayers), String.valueOf(webSupport),
        lobbyServiceLocation, accessToken);
    run(command, NullParser.NULLPARSER);
  }

  /**
   * Removes a session.
   *
   * @param accessToken the accessToken of the user
   * @param sessionid   the id of the session
   */
  public final void remove_session(String accessToken, long sessionid) {
    checkNotNullNotEmpty(accessToken);
    String command = String.format("curl -X DELETE %s/api/sessions/%d?access_token=%s",
        lobbyServiceLocation,
        String.valueOf(sessionid), accessToken);
    run(command, NullParser.NULLPARSER);
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
    System.out.println("Renew: " + output);
    return output;
  }

  /**
   * Unregisters game service.
   *
   * @param gameName    the name of the game
   * @param accessToken the access token of the user
   */
  public final void unregister_gameservice(String gameName, String accessToken) {
    checkNotNullNotEmpty(gameName, accessToken);
    String command = String.format("curl -X DELETE %s/api/gameservices/%s?access_token=%s",
        lobbyServiceLocation,
        gameName, accessToken);
    run(command, NullParser.NULLPARSER);
  }
  
  
  public final void revoke_auth(String accessToken) {
    String url = 
        String.format("%s/oauth/active?access_token=%s",
            lobbyServiceLocation,
            accessToken);
    HttpResponse<String> response = Unirest.delete(url).asString();
    System.out.println(response.getBody());
  }
  
  /**
   * Returns the user with a given username and access token.
   *
   * @param username    the username of the user
   * @param accessToken the access token of the user
   * @return the output as a JSON object
   */
  public final JSONObject get_user(String username, String accessToken) {
    checkNotNullNotEmpty(username, accessToken);
    String command = String.format("curl -X GET %s/api/users/%s?access_token=%s",
        lobbyServiceLocation, username,
        accessToken);
    if (run(command, ParseText.PARSE_TEXT).equals("User details can not be queried. "
                                                    + "No such user.\n")) {
      return null;
    } else {
      return (JSONObject) run(command, Parsejson.PARSE_JSON);
    }
  }

  /**
   * Sends the move done by a player to the server at the end of their turn.
   *
   * @param gameId The game id
   * @param username The player's username
   * @param move The move that the player made
   */
  public final void end_turn(int gameId, String username, String move) {
    //String command = String.format("curl -x POST %s/api/games/%d/%s/endTurn -d %s",
    // SERVERLOCATION, gameId, username, move);
    // run(command, NullParser.NULLPARSER);
    HttpResponse<String> response = Unirest.put(String.format("http://" + SERVERLOCATION + "/api/games/%d/endturn", gameId, username))
        .header("Content-Type", "application/json")
        .body(move)
        .asString();
    System.out.println(response.getStatus());
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

  private Object run(String command, OutputParser parser) {
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
