package model.cards;

/**
 * Observable interface.
 */
public interface CardObservable {
  void addListener(CardObserver cardView);

  void removeListener(CardObserver cardView);

}
