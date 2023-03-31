package ca.mcgill.splendorclient.model.users;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.lobbyserviceio.Parsejson;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

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
  private Timer renewalTimer = new Timer();
  /**
   * Instance of this user.
   */
  public static User THISUSER = null;

  // using the user's user-name as the unique key for each of them
  private static final HashMap<String, User> USERS = new HashMap<>();

  private User(String userName, String acessToken, String refreshToken, Role role) {
    this.userName = userName;
    this.accessToken = acessToken;
    this.refreshToken = refreshToken;
    this.role = role;

    // setting repeating timer process to renew access token once it expires
    renewalTimer.scheduleAtFixedRate(new RenewAccessToken(), expiresIn, expiresIn);
  }
  
  private Timer getTimer() {
    return renewalTimer;
  }
  
  /**
   * Creates a User.
   *
   * @param userName     the username of the user
   * @param accessToken  the access token of the user
   * @param refreshToken the refresh token of the user
   * @param role         the role of the user
   * @param isThisUser is this the user that is created
   * @return the newly created user
   */
  public static User newUser(String userName, String accessToken,
                             String refreshToken, Role role, boolean isThisUser) {
    if (!USERS.containsKey(userName)) {
      User user = new User(userName, accessToken,
          refreshToken, role);
      USERS.put(userName, user);
      if (isThisUser) {
        THISUSER  = user;
      }
    }
    return USERS.get(refreshToken);
  }

  /**
   * Logs out the user with the given username.
   *
   * @param userName the given username
   */
  public static void logout(String userName) {
    USERS.get(userName).getTimer().cancel();
    USERS.remove(userName);
  }

  /**
   * Inner class to update the access token of the user using their refresh token.
   *
   * @author zacharyhayden
   */
  private class RenewAccessToken extends TimerTask {
    @Autowired
    private LobbyServiceExecutor lobbyServiceExecutor;

    @Override
    public void run() {
      JSONObject renewedTokens =
          lobbyServiceExecutor.renew_auth_token(refreshToken);
      accessToken = (String) Parsejson.PARSE_JSON.getFromKey(renewedTokens, "access_token");
      expiresIn = (int) Parsejson.PARSE_JSON.getFromKey(renewedTokens, "expires_in");
    }

  }

  /**
   * Returns the user's accessToken.
   *
   * @return the user's accessToken
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Returns this user's role.
   *
   * @return this user's role
   */
  public Role getRole() {
    return role;
  }

  @Override
  public String toString() {
    return "User [userName=" + userName + ", accessToken="
             + accessToken + ", refreshToken=" + refreshToken
             + ", role=" + role + ", expiresIn=" + expiresIn + "]";
  }

  /**
   * Returns this user's username.
   *
   * @return this user's username
   */
  public String getUsername() {
    return userName;
  }

}
