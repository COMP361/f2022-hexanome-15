package ca.mcgill.splendorclient.control;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.MoveInfo;
import ca.mcgill.splendorclient.model.TokenType;
import ca.mcgill.splendorclient.model.users.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONException;

/**
 * Manages actions that are sent to the server.
 */
public class ActionManager {
  
  
  private static ActionManager instance;
  
  private static Map<String, MoveInfo> currentMoveMap;
  
  
  private ActionManager() {
    instance = new ActionManager();
    currentMoveMap = new HashMap<>();
  }
  
  public static void setCurrentMoveMap(Map<String, MoveInfo> currentMap) {
    currentMoveMap = currentMap;
  }
  
  public static HttpResponse<JsonNode> findAndSendAssociatedTokenMove(TokenType type) {
    for (Entry<String, MoveInfo> entry : currentMoveMap.entrySet()) {
      if (entry.getValue().getAction().equals("TAKE_TOKEN")) {
        if (entry.getValue().getTokenType().equals(type.toString())) {
          return sendAction(entry.getKey());
        }
      }
    }
    return null;
  }
  
  public static ActionManager getInstance() {
    return instance;
  }
  
  private static HttpResponse<JsonNode> getActions() {
    return Unirest.get(String.format("http://%s/api/games/%d/players/%s/actions", 
        LobbyServiceExecutor.SERVERLOCATION, 
        GameController.getInstance().getGameId(), 
        User.THISUSER.getUsername())).asJson();
  }
  
  private static HttpResponse<JsonNode> sendAction(String action) {
    return Unirest.put(String.format("http://%s/api/games/%d/players/%s/actions/%s", 
        LobbyServiceExecutor.SERVERLOCATION, 
        GameController.getInstance().getGameId(), 
        User.THISUSER.getUsername(), action)).asJson();
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

}
