package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.GameServiceAccountJson;
import ca.mcgill.splendorserver.gameio.GameServiceJson;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Executes Lobby Service commands.
 *
 * @author zacharyhayden
 */
@Component
public class LobbyServiceExecutor implements LobbyServiceExecutorInterface {
  private static String gameServiceLocation = "http://localhost:8080";
  private String lobbyServiceLocation = "http://localhost:4242";
  private       JSONObject      adminAuth    = auth_token("maex", "abc123_ABC123");
  private       String          refreshToken = (String) adminAuth.get("refresh_token");
  private String accessToken = (String) adminAuth.get("access_token");

  /**
   * Creates a LobbyServiceExecutor.
   *
   */
  public LobbyServiceExecutor() {
  }

  /**
   * Gets auth token from the lobby service.
   *
   * @param username username
   * @param password password
   * @return the json containing object with response
   */
  private JSONObject auth_token(String username, String password) {
    String command = String.format(
        "curl -X POST " + "--user bgp-client-name:bgp-client-pw "
        + "%s/oauth/token?grant_type=password&username=%s&password=%s",
        lobbyServiceLocation, username, password);

    try {
      // execute the command
      Process process = Runtime.getRuntime().exec(command);

      // handle exit code
      int exitCode = process.waitFor();

      java.util.logging.Logger.getAnonymousLogger()
          .log(Level.INFO, command + " exit code: " + exitCode);

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
      // assign global variable
      JSONObject output = (JSONObject) parse(process.getInputStream());

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

  private HttpResponse<JsonNode> getRegisteredGameServices() {
    return Unirest.get(lobbyServiceLocation + "/api/gameservices")
             .header("accept", "application/json")
             .asJson();
  }

  @Override
  public void register_gameservice(int maxSessionPlayers,
                                   int minSessionPlayers, String gameName,
                                   String displayName,
                                   boolean webSupport) {
    checkNotNullNotEmpty(accessToken, gameServiceLocation, gameName, displayName);

    System.out.println(getRegisteredGameServices().getBody()
                         .toPrettyString());

    GameServiceAccountJson
        acc = new GameServiceAccountJson(
        gameName, "Antichrist1!", "#000000");

    String newUserjSon = new Gson().toJson(acc);

    final HttpResponse<String> response1 = Unirest.put(
        lobbyServiceLocation + "/api/users/"
          + gameName
          + "?access_token="
          + accessToken.replace("+", "%2B")
      )
                                             .header("Content-Type", "application/json")
                                             .body(newUserjSon)
                                             .asString();


    adminAuth    = auth_token(gameName, "Antichrist1!");
    accessToken  = (String) adminAuth.get("access_token");
    refreshToken = (String) adminAuth.get("refresh_token");
    GameServiceJson gs = new GameServiceJson(gameName, displayName,
        gameServiceLocation, "2", "4", "true"
    );

    System.out.println("Response from service user registration: " + response1.getBody());
    String newServiceJson = new Gson().toJson(gs);

    HttpResponse<String> response2 = Unirest.put(
        lobbyServiceLocation + "/api/gameservices/"
          + gameName
          + "?access_token="
          + accessToken.replace("+", "%2B")
      )
                                       .header("Content-Type", "application/json")
                                       .body(newServiceJson)
                                       .asString();
    System.out.println("Response from registration request: " + response2.getBody());
  }

  @Override
  public void save_game(String body, String gameserviceName, String id) {
    try {
      String url =
          String.format(
          lobbyServiceLocation
            + "/api/gameservices/%s/savegames/%s?access_token=%s",
          gameserviceName, id, URLEncoder.encode(accessToken, "UTF-8"));

      System.out.println(Unirest.put(url)
                           .header("Content-Type", "application/json")
                           .body(body).asString().getBody());
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private Object parse(InputStream scriptOutput) {
    assert scriptOutput != null;

    StringBuilder output = new StringBuilder();
    BufferedReader reader = new BufferedReader(new InputStreamReader(scriptOutput));

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

  private void checkNotNullNotEmpty(String... args) {
    for (String arg : args) {
      assert arg != null && arg.length() != 0 : "Arguments cannot be empty nor null.";
    }
  }

}
