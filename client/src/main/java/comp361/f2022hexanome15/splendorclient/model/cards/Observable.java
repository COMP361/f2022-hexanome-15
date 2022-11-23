package comp361.f2022hexanome15.splendorclient.model.cards;

/**
 * Observable interface.
 */
public interface Observable {
  void addListener(Observer observer);

  void removeListener(Observer observer);

}
