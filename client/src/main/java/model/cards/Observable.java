package model.cards;

/**
 * Observable interface.
 */
public interface Observable {
  void addListener(Observer observer);

  void removeListener(Observer observer);

}
