package ca.mcgill.splendorserver.model.savegame;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;

public class DeckJson {
  
  private String deckType;
  private List<Integer> cards = new ArrayList<>();
  
  public DeckJson(Deck deck) {
    deckType = deck.getType().toString();
    for (Card card : deck.getCards()) {
      cards.add(card.getId());
    }
  }

}
