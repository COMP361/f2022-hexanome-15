package ca.mcgill.splendorserver.model.action;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.users.User;
import ca.mcgill.splendorserver.model.cards.Card;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * PROBABLY DEPRECATED DUE TO BLACKBOARD PATTERN
 *
 * @author lawrenceberardelli
 */
public class MoveManager {
  
  private static MoveManager INSTANCE = new MoveManager();
  private static Stack<Move> moveStack;
  
  private MoveManager() {
    moveStack = new Stack<Move>();
  }

  /**
   * Returns this instance of MoveManager.
   *
   * @return this instance of MoveManager
   */
  public static MoveManager getInstance() {
    return INSTANCE;
  }
  
  /**
   * Called as a result of an update from the server.
   * Only notifies observers if move is performed by another user.
   *
   * @param json The move that is being retrieved as a String
   */
  public void deserializeMove(String json) {
    Gson gson = new Gson();
    Move move = gson.fromJson(json, Move.class);
    moveStack.push(move);
  }
 
  /**
   * Returns the most recent move.
   *
   * @return the most recent move
   */
  public Move getMostRecentMove() {
    return moveStack.peek();
  }
}
