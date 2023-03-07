package ca.mcgill.splendorclient.control;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.MoveInfo;
import ca.mcgill.splendorclient.model.TokenType;
import ca.mcgill.splendorclient.model.users.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.scene.control.Alert;
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
   * Finds token moves in the move map and send token moves to the server.
   *
   * @param type the type of token
   * @return a HttpResponse
   */
  public static HttpResponse<String> findAndSendAssociatedTokenMove(TokenType type) {
    System.out.println("Searching for token move with type " + type);
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
   * Finds reserve dev moves in the move map and forwards them to server.
   *
   * @param cardid requested to reserve
   * @return response from server
   */
  public static HttpResponse<String> findAndSendReserveCardMove(int cardid) {
    System.out.println("Searching for reserve card move with id: " + cardid);
    for (Entry<String, MoveInfo> entry : currentMoveMap.entrySet()) {
      if (entry.getValue().getAction().equals("RESERVE_DEV_TAKE_JOKER") 
          || entry.getValue().getAction().equals("RESERVE_DEV")) {
        if (entry.getValue().getCardId().equals(String.valueOf(cardid))) {
          return sendAction(entry.getKey());
        }
      }
    }
    return null;
  }
  
  /**
   * Finds purchase dev moves in the move map and forwards to server.
   *
   * @param cardid requested to purchase
   * @return response from server
   */
  public static HttpResponse<String> findAndSendPurchaseCardMove(int cardid) {
    System.out.println("Search for purchase card move with id: " + cardid); 
    for (Entry<String, MoveInfo> entry : currentMoveMap.entrySet()) {
      if (entry.getValue().getAction().equals("PURCHASE_DEV")) {
        if (entry.getValue().getCardId().equals(String.valueOf(cardid))) {
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
      Alert takeTokenAlert = new Alert(Alert.AlertType.INFORMATION);
      takeTokenAlert.setTitle("Compound Move Info");
      takeTokenAlert.setHeaderText("Please select additional token to take.");
      takeTokenAlert.showAndWait();
    }
    if (action.equals("PAIR_SPICE_CARD")) {
      // inform user to pair card
      Alert takeTokenAlert = new Alert(Alert.AlertType.INFORMATION);
      takeTokenAlert.setTitle("Compound Move Info");
      takeTokenAlert.setHeaderText("Please select card in your inventory to pair with.");
      takeTokenAlert.showAndWait();
    }
    if (action.equals("RECEIVE_NOBLE")) {
      // inform user to select noble to be visited by if more than one
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid noble to be visited by.");
      alert.showAndWait();
    }
    if (action.equals("RESERVE_NOBLE")) {
      // inform user to select noble to reserve
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid noble to reserve.");
      alert.showAndWait();
    }
    if (action.equals("RET_1_TOKEN")) {
      // inform user to select token to return
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid token to return.");
      alert.showAndWait();
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
        User.THISUSER.getUsername()))
        .queryString("access_token", User.THISUSER.getAccessToken()).asJson();
  }
  
  private static HttpResponse<String> sendAction(String action) {
    return Unirest.put(String.format("http://%s/api/games/%d/players/%s/actions/%s", 
        LobbyServiceExecutor.SERVERLOCATION, 
        GameController.getInstance().getGameId(), 
        User.THISUSER.getUsername(), action))
        .queryString("access_token", User.THISUSER.getAccessToken()).asString();
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
