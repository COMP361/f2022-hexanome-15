package ca.mcgill.splendorserver.model.savegame;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility to facilitate the translation to/from json for savegame jsons. 
 *
 * @author lawrenceberardelli
 *
 */
public class DeckJson {
  
  public String deckType;
  public List<Integer> cards = new ArrayList<>();
  
  /**
   * Creates a deckjson object.
   *
   * @param deck the model which the json is based on
   */
  public DeckJson(Deck deck) {
    deckType = deck.getType().toString();
    for (Card card : deck.getCards()) {
      cards.add(card.getId());
    }
  }

}
