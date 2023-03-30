package ca.mcgill.splendorclient.control;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.DeckType;
import ca.mcgill.splendorclient.model.MoveInfo;
import ca.mcgill.splendorclient.model.TokenType;
import ca.mcgill.splendorclient.model.users.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
    for (Entry<String, MoveInfo> entry : currentMap.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue().getAction());
    }
  }

  /**
   * Finds token moves in the move map and send token moves to the server.
   *
   * @param type the type of token
   * @return a HttpResponse
   */
  public static HttpResponse<String> findAndSendAssociatedTakeExtraTokenMove(TokenType type) {
    System.out.println("Searching for take extra token move with type " + type);
    for (Entry<String, MoveInfo> entry : currentMoveMap.entrySet()) {
      if (entry.getValue().getAction().equals("TAKE_EXTRA_TOKEN")) {
        if (entry.getValue().getTokenType().equals(type.toString())) {
          return sendAction(entry.getKey());
        }
      }
    }
    return null;
  }

  /**
   * Finds token moves in the move map and send token moves to the server.
   *
   * @param type the type of token
   * @return a HttpResponse
   */
  public static HttpResponse<String> findAndSendAssociatedTakeTokenMove(TokenType type) {
    System.out.println("Searching for take token move with type " + type);
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
   * Finds return token moves in the move map and forwards them to server.
   *
   * @param type the token type of the token
   * @return response from server
   */
  public static HttpResponse<String> findAndSendAssociatedReturnTokenMove(TokenType type) {
    System.out.println("Searching for return token move with type " + type);
    for (Entry<String, MoveInfo> entry : currentMoveMap.entrySet()) {
      if (entry.getValue().getAction().equals("RET_TOKEN")) {
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
      if (entry.getValue().getAction().equals("RESERVE_DEV")) {
        if (entry.getValue().getCardId() != null) {
          if (entry.getValue().getCardId().equals(String.valueOf(cardid))) {
            return sendAction(entry.getKey());
          }
        }
      }
    }
    return null;
  }

  /**
   * Finds reserve dev moves in the move map and forwards them to server.
   *
   * @param deckType requested to reserve
   * @return response from server
   */
  public static HttpResponse<String> findAndSendReserveFromDeckMove(DeckType deckType) {
    System.out.println("Searching for reserve card move with deck level: " + deckType);
    for (Entry<String, MoveInfo> entry : currentMoveMap.entrySet()) {
      if (entry.getValue().getAction().equals("RESERVE_DEV")) {
        if (entry.getValue().getDeckLevel() != null) {
          if (entry.getValue().getDeckLevel().equals(deckType.toString())) {
            return sendAction(entry.getKey());
          }
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
   * Finds cascade card moves in the move map and forwards to server.
   *
   * @param cardid requested to take for free
   * @return response from server
   */
  public static HttpResponse<String> findAndSendCascadeLevel1Move(int cardid) {
    System.out.println("Search for cascade card move with id: " + cardid);
    for (Entry<String, MoveInfo> entry : currentMoveMap.entrySet()) {
      if (entry.getValue().getAction().equals("CASCADE_LEVEL_1")) {
        if (entry.getValue().getCardId().equals(String.valueOf(cardid))) {
          return sendAction(entry.getKey());
        }
      }
    }
    return null;
  }

  /**
   * Finds cascade card moves in the move map and forwards to server.
   *
   * @param cardid requested to take for free
   * @return response from server
   */
  public static HttpResponse<String> findAndSendCascadeLevel2Move(int cardid) {
    System.out.println("Search for cascade card move with id: " + cardid);
    for (Entry<String, MoveInfo> entry : currentMoveMap.entrySet()) {
      if (entry.getValue().getAction().equals("CASCADE_LEVEL_2")) {
        if (entry.getValue().getCardId().equals(String.valueOf(cardid))) {
          return sendAction(entry.getKey());
        }
      }
    }
    return null;
  }

  /**
   * Finds and sends pair spice bag move in the move map
   * and forwards to server.
   *
   * @param cardid requested to pair
   * @return response from server
   */
  public static HttpResponse<String> findAndSendPairSpiceCardMove(int cardid) {
    System.out.println("Searching for pair spice card move with id: " + cardid);
    for (Entry<String, MoveInfo> entry : currentMoveMap.entrySet()) {
      if (entry.getValue().getAction().equals("PAIR_SPICE_CARD")) {
        if (entry.getValue().getCardId().equals(String.valueOf(cardid))) {
          return sendAction(entry.getKey());
        }
      }
    }
    return null;
  }

  /**
   * Finds discard and sends discard move in the move map
   * and forwards to server.
   *
   * @param cardid requested to discard
   * @return response from server
   */
  public static HttpResponse<String> findAndSendDiscardCardMove(int cardid) {
    System.out.println("Searching for discard card move with id: " + cardid);
    for (Entry<String, MoveInfo> entry : currentMoveMap.entrySet()) {
      if (entry.getValue().getAction().equals("DISCARD_FIRST_WHITE_CARD")
            || entry.getValue().getAction().equals("DISCARD_FIRST_BLUE_CARD")
            || entry.getValue().getAction().equals("DISCARD_FIRST_GREEN_CARD")
            || entry.getValue().getAction().equals("DISCARD_FIRST_RED_CARD")
            || entry.getValue().getAction().equals("DISCARD_FIRST_BLACK_CARD")
            || entry.getValue().getAction().equals("DISCARD_SECOND_WHITE_CARD")
            || entry.getValue().getAction().equals("DISCARD_SECOND_BLUE_CARD")
            || entry.getValue().getAction().equals("DISCARD_SECOND_GREEN_CARD")
            || entry.getValue().getAction().equals("DISCARD_SECOND_RED_CARD")
            || entry.getValue().getAction().equals("DISCARD_SECOND_BLACK_CARD")) {
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
      Alert takeTokenAlert = new Alert(Alert.AlertType.INFORMATION);
      takeTokenAlert.setTitle("Compound Move Info");
      takeTokenAlert.setHeaderText("Please select additional token to take.");
      takeTokenAlert.show();
    } else if (action.equals("PAIR_SPICE_CARD")) {
      Alert takeTokenAlert = new Alert(Alert.AlertType.INFORMATION);
      takeTokenAlert.setTitle("Compound Move Info");
      takeTokenAlert.setHeaderText("Please select card in your inventory to pair with.");
      takeTokenAlert.show();
    } else if (action.equals("RECEIVE_NOBLE")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid noble to be visited by.");
      alert.show();
    } else if (action.equals("RESERVE_NOBLE")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid noble to reserve.");
      alert.show();
    } else if (action.equals("RET_TOKEN")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid token to return.");
      alert.show();
    } else if (action.equals("CASCADE_LEVEL_1")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid level 1 card.");
      alert.show();
    } else if (action.equals("CASCADE_LEVEL_2")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid level 2 card.");
      alert.show();
    } else if (action.equals("DISCARD_FIRST_WHITE_CARD")
                 || action.equals("DISCARD_SECOND_WHITE_CARD")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid white card to discard.");
      alert.show();
    } else if (action.equals("DISCARD_FIRST_BLUE_CARD")
                 || action.equals("DISCARD_SECOND_BLUE_CARD")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid blue card to discard.");
      alert.show();
    } else if (action.equals("DISCARD_FIRST_GREEN_CARD")
                 || action.equals("DISCARD_SECOND_GREEN_CARD")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid green card to discard.");
      alert.show();
    } else if (action.equals("DISCARD_FIRST_RED_CARD")
                 || action.equals("DISCARD_SECOND_RED_CARD")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid red card to discard.");
      alert.show();
    } else if (action.equals("DISCARD_FIRST_BLACK_CARD")
                 || action.equals("DISCARD_SECOND_BLACK_CARD")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select valid black card to discard.");
      alert.show();
    } else if (action.equals("TAKE_EXTRA_TOKEN")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select token to take as a result of your trading post power.");
      alert.show();
    } else if (action.equals("PLACE_COAT_OF_ARMS")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("You've unlocked a power as a result of placing a coat of arms.");
      alert.show();
    } else if (action.equals("RECEIVE_CITY")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Compound Move Info");
      alert.setHeaderText("Please select a valid city to receive.");
      alert.show();
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
