package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.cards.DeckType;

/**
 * Enables the creation of a stripped down deck for transmission to client. 
 *
 * @author lawrenceberardelli
 *
 */
public class DeckJson {
  
  private int nCards;
  private DeckType type;
  
  /**
   * Creates new deckjson should be based on actual deck from splendorgame.
   *
   * @param nCards
   * @param type
   */
  public DeckJson(int ncards, DeckType type) {
    this.nCards = ncards;
    this.type = type;
  }
}
