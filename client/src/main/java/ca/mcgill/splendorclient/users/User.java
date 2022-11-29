package ca.mcgill.splendorclient.users;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.lobbyserviceio.Parsejson;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

/**
 * Represents a user.
 * Flyweight design pattern.
 *
 * @author zacharyhayden
 */
public class User {
  private final String userName;
  private String accessToken;
  private String refreshToken;
  private final Role role;
  private int expiresIn = 1800000; // time in milliseconds until the users access token expires
  public static String THISUSER = null;

  // using the user's user-name as the unique key for each of them
  private static final HashMap<String, User> USERS = new HashMap<>();

  private User(String userName, String acessToken, String refreshToken, Role role) {
    this.userName = userName;
    this.accessToken = acessToken;
    this.refreshToken = refreshToken;
    this.role = role;

    // setting repeating timer process to renew access token once it expires
    new Timer().scheduleAtFixedRate(new RenewAccessToken(), expiresIn, expiresIn);
  }

  /**
   * Creates a User.
   *
   * @param userName     the username of the user
   * @param accessToken  the access token of the user
   * @param refreshToken the refresh token of the user
   * @param role         the role of the user
   */
  public static User newUser(String userName, String accessToken,
                             String refreshToken, Role role, boolean isThisUser) {
    if (!USERS.containsKey(userName)) {
      try {
        USERS.put(userName, new User(userName, URLEncoder.encode(accessToken, "UTF-8"),
            URLEncoder.encode(refreshToken, "UTF-8"), role));
        if (isThisUser) {
          THISUSER  = userName;
        }
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    return USERS.get(refreshToken);
  }

  /**
   * Inner class to update the access token of the user using their refresh token.
   *
   * @author zacharyhayden
   */
  private class RenewAccessToken extends TimerTask {

    @Override
    public void run() {
      JSONObject renewedTokens =
          LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR.renew_auth_token(refreshToken);
      accessToken = (String) Parsejson.PARSE_JSON.getFromKey(renewedTokens, "access_token");
      expiresIn = (int) Parsejson.PARSE_JSON.getFromKey(renewedTokens, "expires_in");
    }

  }

  public String getAccessToken() {
    return accessToken;
  }

  public Role getRole() {
    return role;
  }

  @Override
  public String toString() {
    return "User [userName=" + userName + ", accessToken="
             + accessToken + ", refreshToken=" + refreshToken
             + ", role=" + role + ", expiresIn=" + expiresIn + "]";
  }

  public String getUsername() {
    return userName;
  }

}
