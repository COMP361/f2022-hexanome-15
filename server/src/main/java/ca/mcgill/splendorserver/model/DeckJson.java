package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.cards.DeckType;

public class DeckJson {
  
  private int nCards;
  private DeckType type;
  
  public DeckJson(int nCards, DeckType type) {
    this.nCards = nCards;
    this.type = type;
  }
}
