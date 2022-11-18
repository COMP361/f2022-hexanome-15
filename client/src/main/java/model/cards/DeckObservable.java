package model.cards;

/**
 * Observable interface.
 */
public interface DeckObservable {

  void addListener(DeckObserver deckView);

  void removeListener(DeckObserver deckView);
}
