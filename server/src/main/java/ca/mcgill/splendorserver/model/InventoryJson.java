package ca.mcgill.splendorserver.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsPile;
import ca.mcgill.splendorserver.model.tradingposts.Power;

public class InventoryJson {
  
  private List<Integer> cards = new ArrayList<Integer>();
  private Map<TokenType, Integer> tokens = new HashMap<TokenType, Integer>();
  private String userName;
  private int prestige;
  private List<Noble> visitingNobles;
  private List<Power> powers;
  private CoatOfArmsPile coatOfArmsPile;

  public InventoryJson(List<Card> cards, EnumMap<TokenType, TokenPile> tokens, 
      String userName, int prestige, List<Noble> visitingNobles, List<Power> powers, CoatOfArmsPile pile) {
    for (Card card : cards) {
      this.cards.add(card.getId());
    }
    for (TokenType type : tokens.keySet()) {
      this.tokens.put(type, tokens.get(type).getSize());
    }
    this.userName = userName;
    this.prestige = prestige;
    this.visitingNobles = visitingNobles;
    this.powers = powers;
    coatOfArmsPile = pile;
  }
}
