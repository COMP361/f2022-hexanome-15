package ca.mcgill.splendorserver.model;

/**
 * IllegalGameStateException for when unexpected game behaviour happens.
 *
 * @author Zachary Hayden
 */
public class IllegalGameStateException extends RuntimeException {

  public IllegalGameStateException(String message) {
    super(message);
  }
}
