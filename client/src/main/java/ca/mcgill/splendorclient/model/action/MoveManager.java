package ca.mcgill.splendorclient.model.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.google.gson.Gson;

import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.model.cards.Card;
import ca.mcgill.splendorclient.model.cards.Observable;
import ca.mcgill.splendorclient.model.cards.Observer;
import ca.mcgill.splendorclient.users.User;

/**
 * Observes UserInventory to receive message of a valid move.
 * Observes CardView to get notification of when to forward the action to the server.
 * 
 * @author lawrenceberardelli
 *
 */
public class MoveManager implements Observer, Observable {
  
  private static MoveManager INSTANCE = new MoveManager();
  private static Stack<Move> moveStack;
  private Gson gson;
  private List<Observer> observers;
  
  private MoveManager() {
    moveStack = new Stack<Move>();
    gson = new Gson();
    observers = new ArrayList<Observer>();
  }
  
  public static MoveManager getInstance() {
    return INSTANCE;
  }
  
  /**
   * Called as a result of an update from the server. Only notifies observers if move is performed by another user.
   * 
   * @param json
   */
  public void deserializeMove(String json) {
    Gson gson = new Gson();
    Move move = gson.fromJson(json, Move.class);
    moveStack.push(move);
    if (move.getName() != User.THISUSER) {
      for (Observer observer : observers) {
        observer.onAction(move);
      }
    }
  }
  
  /**
   * creates the move object for later forwarding to server
   */
  public void onAction(Card card) {
    Move move = new Move(Action.PURCHASE, card, User.THISUSER);
    moveStack.push(move);
  }
  
  /**
   * Forwards the move to the server.
   */
  public void onAction() {
    assert User.THISUSER != null;
    Move move = getMostRecentMove();
    String json = gson.toJson(move);
    LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR.end_turn(0, User.THISUSER, json);
  }
  
  public Move getMostRecentMove() {
    return moveStack.peek();
  }

  @Override
  public void addListener(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeListener(Observer observer) {
    observers.remove(observer);
  }

}
