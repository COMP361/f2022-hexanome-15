package comp361.f2022hexanome15.splendorclient.model.cards;

/**
 * Observable interface.
 */
public interface CardObservable {
  void addListener(CardObserver cardView);

  void removeListener(CardObserver cardView);

}
