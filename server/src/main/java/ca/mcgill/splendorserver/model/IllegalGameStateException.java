package ca.mcgill.splendorserver.model;

/**
 * IllegalGameStateException for when unexpected game behaviour happens.
 *
 * @author Zachary Hayden
 */
public class IllegalGameStateException extends RuntimeException {

  /**
   * IllegalGameStateException with message.
   *
   * @param message the message to be displayed
   */
  public IllegalGameStateException(String message) {
    super(message);
  }
}
