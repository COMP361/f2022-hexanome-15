package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsPile;
import ca.mcgill.splendorserver.model.tradingposts.Power;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Facilitates the creation of a stripped down user inventory json. 
 *
 * @author lawrenceberardelli
 *
 */
public class InventoryJson {
  
  private List<Integer> cards = new ArrayList<Integer>();
  private Map<TokenType, Integer> tokens = new HashMap<TokenType, Integer>();
  private String userName;
  private int prestige;
  private List<Noble> visitingNobles;
  private List<Power> powers;
  private CoatOfArmsPile coatOfArmsPile;

  /**
   * Creates new inventoryjson. Should be based on actual user inventory from splendorgame.
   *
   * @param cards in the players inventory
   * @param tokens in the players inventory
   * @param userName of the player 
   * @param prestige earned by the player
   * @param visitingNobles earned by the player
   * @param powers earned by the player
   * @param pile of coat of arms
   */
  public InventoryJson(List<Card> cards, EnumMap<TokenType, TokenPile> tokens, 
      String userName, int prestige, List<Noble> visitingNobles, 
      List<Power> powers, CoatOfArmsPile pile) {
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
