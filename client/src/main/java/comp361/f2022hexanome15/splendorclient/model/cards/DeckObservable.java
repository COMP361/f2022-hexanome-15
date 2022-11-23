package comp361.f2022hexanome15.splendorclient.model.cards;

/**
 * Observable interface.
 */
public interface DeckObservable {

  void addListener(DeckObserver deckView);

  void removeListener(DeckObserver deckView);
}
