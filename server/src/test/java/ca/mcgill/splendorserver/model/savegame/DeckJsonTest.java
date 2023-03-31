package ca.mcgill.splendorserver.model.savegame;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.DeckType;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.CriteriaBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckJsonTest {

  @Test
  void createDeckJson() {
    Deck deck = new Deck(DeckType.BASE1);
    DeckJson deckJson = new DeckJson(deck);
    List<Integer> cards = new ArrayList<>();
    String deckType = deck.getType().toString();
    for (Card card : deck.getCards()) {
      cards.add(card.getId());
    }
    assertEquals(deckType, deckJson.deckType);
    assertEquals(cards, deckJson.cards);
  }
}