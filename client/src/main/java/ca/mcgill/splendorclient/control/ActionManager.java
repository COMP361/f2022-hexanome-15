package ca.mcgill.splendorclient.control;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.MoveInfo;
import ca.mcgill.splendorclient.model.TokenType;
import ca.mcgill.splendorclient.model.users.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

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

  /**
   * Sets the current move map to the given map.
   *
   * @param currentMap the given map
   */
  public static void setCurrentMoveMap(Map<String, MoveInfo> currentMap) {
    currentMoveMap = currentMap;
  }

  /**
   * Finds token moves in the token map and send token moves to the server.
   *
   * @param type the type of token
   * @return a HttpResponse
   */
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
  

  /**
   * Returns this instance of ActionManager.
   *
   * @return this instance of ActionManager
   */
  public static ActionManager getInstance() {
    return instance;
  }

  /**
   * Allows players to execute compound moves.
   *
   * @param action the action to be executed
   */
  public static void handleCompoundMoves(String action) {
    HttpResponse<JsonNode> moveMap = ActionManager.getActions();
    String moves = moveMap.getBody().toString();
    Gson gson = new Gson();
    Map<String, MoveInfo> availableMoves = gson.fromJson(moves,
        new TypeToken<Map<String, MoveInfo>>() {}.getType());
    ActionManager.setCurrentMoveMap(availableMoves);
    if (action.equals("TAKE_TOKEN")) {
      //inform user to take next token
    }
  }

  /**
   * Gets the list of actions from the server.
   *
   * @return the list of actions in the server
   */
  public static HttpResponse<JsonNode> getActions() {
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
