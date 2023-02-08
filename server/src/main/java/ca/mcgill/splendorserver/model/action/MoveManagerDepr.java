package ca.mcgill.splendorserver.model.action;


import com.google.gson.Gson;
import java.util.Stack;

/**
 * PROBABLY DEPRECATED DUE TO BLACKBOARD PATTERN
 *
 * @author lawrenceberardelli
 */
public class MoveManagerDepr {

  private static final MoveManagerDepr INSTANCE = new MoveManagerDepr();
  private static       Stack<Move>     moveStack;

  private MoveManagerDepr() {
    moveStack = new Stack<Move>();
  }

  /**
   * Returns this instance of MoveManagerDepr.
   *
   * @return this instance of MoveManagerDepr
   */
  public static MoveManagerDepr getInstance() {
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
