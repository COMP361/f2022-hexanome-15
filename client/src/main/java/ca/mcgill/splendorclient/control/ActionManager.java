package ca.mcgill.splendorclient.control;

import java.util.ArrayList;

public class ActionManager {
  
  private static ArrayList<String> purchaseActions = new ArrayList<String>();
  private static ArrayList<String> reserveActions = new ArrayList<String>();
  
  public static boolean forwardPurchaseRequest(int column, int row) {
    //TODO: forward the request to our server, also need gameId and player name. 
    String hash = purchaseActions.get(4 * row + column);
    return true;
  }
  
  public static boolean forwardReserveRequest(int column, int row) {
    //TODO: same as above
    String hash = reserveActions.get(4*row + column);
    return true;
  }
  
  public static boolean forwardGrabTokensRequest(String actionHash) {
    //TODO: same as above, but the hash might should be a json sent to a special endpoint as grabbing tokens is a compound action.
    return true;
  }
  
  public static void getCardActions() {
    //TODO: call the actions endpoint then loop over the returned hashes in an agreed upon order, adding them to the correct containers.
  }

}
