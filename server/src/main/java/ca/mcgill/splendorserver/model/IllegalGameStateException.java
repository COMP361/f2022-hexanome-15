package ca.mcgill.splendorserver.model;

/**
 * @author Zachary Hayden
 * Date: 2/5/23
 */
public class IllegalGameStateException extends RuntimeException {

  public IllegalGameStateException(String message) {
    super(message);
  }
}
