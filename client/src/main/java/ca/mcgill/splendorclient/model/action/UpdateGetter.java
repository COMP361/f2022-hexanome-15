package ca.mcgill.splendorclient.model.action;

public class UpdateGetter implements Runnable {
  
  private int gameId;
  private boolean bExit = false;
  
  @Override
  public void run() {
    while (!bExit) {
      //request update from server via lobby service class.
      
      //if the update actually returns new info
//      MoveManager.getInstance().deserializeMove(null);
    }
  }
  
  public UpdateGetter(int gameId) {
    this.gameId = gameId;
  }
  
  public void exit() {
    bExit = true;
  }

}
