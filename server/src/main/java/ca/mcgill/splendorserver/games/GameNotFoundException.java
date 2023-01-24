package ca.mcgill.splendorserver.games;

/**
 * Exception that is thrown when a game cannot be found.
 *
 * @author zacharyhayden
 */
public class GameNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Creates a GameNotFound exception.
   *
   * @param gameid The game id
   */
  public GameNotFoundException(Long gameid) {
    super("Could not find game ID: " + gameid);
  }

}
