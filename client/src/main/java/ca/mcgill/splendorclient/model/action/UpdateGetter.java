package ca.mcgill.splendorclient.model.action;

/**
 * Gets updates from the server.
 */
public class UpdateGetter implements Runnable {
  
  private int gameId;
  private boolean exit = false;
  
  @Override
  public void run() {
    while (!exit) {
      //request update from server via lobby service class.
      
      //if the update actually returns new info
      //MoveManager.getInstance().deserializeMove(null);
    }
  }
  
  public UpdateGetter(int gameId) {
    this.gameId = gameId;
  }
  
  public void exit() {
    exit = true;
  }

}
