package ca.mcgill.splendorserver.model.cards;

/**
 * Observable interface.
 */
public interface Observable {
  /**
   * Adds a listener.
   *
   * @param observer The observer to be added
   */
  void addListener(Observer observer);

  /**
   * Removes a listener.
   *
   * @param observer The listener to be removed
   */
  void removeListener(Observer observer);

}
