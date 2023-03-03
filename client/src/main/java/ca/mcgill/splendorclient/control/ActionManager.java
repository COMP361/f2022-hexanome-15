package ca.mcgill.splendorclient.control;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.TokenType;
import ca.mcgill.splendorclient.model.users.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONException;

/**
 * Manages actions that are sent to the server.
 */
public class ActionManager {
  
  private List<String> tokenCart = new ArrayList<String>();
  
  private static ActionManager instance;
  
  private static String tokenTypeToLocationCode(TokenType type) {
    return "T" + Arrays.asList(TokenType.values()).indexOf(type);
  }
  
  
  private ActionManager() {
    instance = new ActionManager();
  }
  
  public static ActionManager getInstance() {
    return instance;
  }
  
  /**
   * Adds a token to the current take tokens attempt.
   * Forwards a request to the server when a take tokens attempt is a valid request.
   *
   * @param tokenType the type of token to be added to the user inventory
   */
  public void addToRequest(TokenType tokenType) {
    assert tokenCart.size() < 3;
    if (tokenCart.contains(tokenTypeToLocationCode(tokenType))) {
      tokenCart.add(tokenTypeToLocationCode(tokenType));
      try {
        forwardGrabTokensRequest();
      } catch (JSONException e) {
        tokenCart.clear();
      }
    } else if (tokenCart.size() == 2) {
      tokenCart.add(tokenTypeToLocationCode(tokenType));
      forwardGrabTokensRequest();
    } else {
      tokenCart.add(tokenTypeToLocationCode(tokenType));
    }
  }
  
  private static HttpResponse<JsonNode> getActions() {
    return Unirest.get(String.format("http://%s/api/games/%d/players/%s/actions", 
        LobbyServiceExecutor.SERVERLOCATION, 
        GameController.getInstance().getGameId(), 
        User.THISUSER.getUsername())).asJson();
  }
  
  private static void sendAction(String action) {
    Unirest.put(String.format("http://%s/api/games/%d/players/%s/actions/%s", 
        LobbyServiceExecutor.SERVERLOCATION, 
        GameController.getInstance().getGameId(), 
        User.THISUSER.getUsername(), action));
  }

  /**
   * Hashes a card action and sends it to the server.
   *
   * @param locationCode the location code of card
   */
  public static void forwardCardRequest(String locationCode) {
    HttpResponse<JsonNode> response = getActions();
    // json should be of the form { location1 : hashCode, ... }
    // where the location codes are known to both applications a priori
    String hash = (String) response.getBody().getObject().get(locationCode);
    // send the hash back to the server, where the server will use it
    // as a key to retrieve the move object and update the board model.
    sendAction(hash);
  }
  
  private void forwardGrabTokensRequest() {
    tokenCart.sort(Comparator.naturalOrder());
    String locationCode = "";
    for (String code : tokenCart) {
      locationCode += code;
    }
    HttpResponse<JsonNode> response = getActions();
    String hash = (String) response.getBody().getObject().get(locationCode);
    sendAction(hash);
  }


}
