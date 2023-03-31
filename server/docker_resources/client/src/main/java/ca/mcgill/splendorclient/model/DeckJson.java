package ca.mcgill.splendorclient.model;

/**
 * Enables the creation of a stripped down deck for transmission to client. 
 *
 * @author lawrenceberardelli
 *
 */
public class DeckJson {
  
  private int ncards;
  private DeckType type;
  
  /**
   * Creates new deckjson should be based on actual deck from splendorgame.
   *
   * @param ncards The number of cards in the deck
   * @param type The type of deck
   */
  public DeckJson(int ncards, DeckType type) {
    this.ncards = ncards;
    this.type = type;
  }
}
