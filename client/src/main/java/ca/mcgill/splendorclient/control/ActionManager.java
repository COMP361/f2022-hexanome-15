package ca.mcgill.splendorclient.control;

import java.util.ArrayList;
import java.util.Arrays;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.TokenType;
import ca.mcgill.splendorclient.model.users.User;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class ActionManager {
  
  private static String tokenTypeToLocationCode(TokenType type) {
    return "T" + Arrays.asList(TokenType.values()).indexOf(type);
  }
  
  
  private ActionManager() {
    
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
  
  public static boolean forwardCardRequest(String locationCode) {
    HttpResponse<JsonNode> response = getActions();
    
    String hash = (String) response.getBody().getObject().get(locationCode);
    
    sendAction(hash);
    return true;
  }
  
  public static boolean forwardGrabTokensRequest(String actionHash) {
    //TODO: same as above, but the hash might should be a json sent to a special endpoint as grabbing tokens is a compound action.
    return true;
  }


}
