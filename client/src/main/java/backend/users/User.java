package backend.users;

import static backend.lobbyservice.LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR;

import backend.lobbyservice.Parsejson;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

/**
 * Represents a user.
 *
 * @author zacharyhayden
 * @implNote Flyweight design pattern
 */
public class User {
  private final String userName;
  private String accessToken;
  private final String refreshToken;
  private final Role role;
  private int expiresIn = 1800000; // time in milliseconds until the users access token expires

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
   * @param userName the username of the user
   * @param accessToken the access token of the user
   * @param refreshToken the refresh token of the user
   * @param role the role of the user
   */
  public static User newUser(String userName, String accessToken,
                              String refreshToken, Role role) {
    if (!USERS.containsKey(refreshToken)) {
      try {
        USERS.put(URLEncoder.encode(refreshToken, "UTF-8"),
          new User(userName, URLEncoder.encode(accessToken, "UTF-8"),
            URLEncoder.encode(refreshToken, "UTF-8"), role));
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
   *
   */
  private class RenewAccessToken extends TimerTask {

    @Override
    public void run() {
      JSONObject renewedTokens = LOBBY_SERVICE_EXECUTOR.renew_auth_token(refreshToken);
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
  public int hashCode() {
    return Objects.hash(refreshToken);
  }

  @Override
  public String toString() {
    return "User [aAccessToken=" + accessToken
             + ", aRefreshToken=" + refreshToken + ", aRole=" + role
             + ", aExpiresIn=" + expiresIn + "]";
  }

  public String getUsername() {
    return userName;
  }

}
