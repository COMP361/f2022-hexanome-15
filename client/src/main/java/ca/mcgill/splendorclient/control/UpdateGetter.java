package ca.mcgill.splendorclient.control;

/**
 * Gets updates from the server.
 */
public class UpdateGetter implements Runnable {
  
  private long gameId;
  private boolean exit = false;
  
  @Override
  public void run() {
    while (!exit) {
      //request update from server via lobby service class.
      
      //if the update actually returns new info
      //MoveManager.getInstance().deserializeMove(null);
    }
  }

  /**
   * Gets updates for this game.
   *
   * @param gameId the game id
   */
  public UpdateGetter(long gameId) {
    this.gameId = gameId;
  }

  /**
   * Exits this thread.
   */
  public void exit() {
    exit = true;
  }

}
