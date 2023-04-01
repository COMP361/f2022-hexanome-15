package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.control.LobbyServiceExecutor;
import ca.mcgill.splendorserver.control.Parsejson;
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
  private int expiresIn = 1800000; // time in milliseconds until the users access token expires
  private Timer renewalTimer = new Timer();
  /**
   * Instance of this user.
   */
  public static User THISUSER = null;

  // using the user's user-name as the unique key for each of them
  private static final HashMap<String, User> USERS = new HashMap<>();

  private User(String userName, String acessToken, String refreshToken) {
    this.userName = userName;
    this.accessToken = acessToken;
    this.refreshToken = refreshToken;

    // setting repeating timer process to renew access token once it expires
    renewalTimer.scheduleAtFixedRate(new RenewAccessToken(), expiresIn, expiresIn);
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
      accessToken = (String) renewedTokens.get("access_token");
      expiresIn = (int) renewedTokens.get("expires_in");
    }

  }

  @Override
  public String toString() {
    return "User [userName=" + userName + ", accessToken="
        + accessToken + ", refreshToken=" + refreshToken
         + ", expiresIn=" + expiresIn + "]";
  }

}
