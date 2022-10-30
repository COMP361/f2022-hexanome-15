package model.Cards;

public interface DeckObservable {

    void addListener(DeckObserver deckView);

    void removeListener(DeckObserver deckView);
}
