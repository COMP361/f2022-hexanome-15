package ca.mcgill.splendorserver.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;

public class GameBoardJson {
  
  private List<InventoryJson> inventories;
  private List<DeckJson> decks = new ArrayList<DeckJson>();
  private List<Noble> nobles;
  private List<Integer> cardField = new ArrayList<Integer>(); //implicit flattened 2d array
  private Map<TokenType, Integer> tokenField = new HashMap<TokenType, Integer>();
  
  public GameBoardJson(List<InventoryJson> inventories, List<Deck> decks, 
      List<Noble> nobles, List<Card> cardField, EnumMap<TokenType, TokenPile> tokenField) {
    this.inventories = inventories;
    for (Deck deck : decks) {
      this.decks.add(new DeckJson(deck.getSize(), deck.getType()));
    }
    this.nobles = nobles;
    for (Card card : cardField) {
      this.cardField.add(card.getId());
    }
    for (TokenType type : tokenField.keySet()) {
      this.tokenField.put(type, tokenField.get(type).getSize());
    }
  }

}
