package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.nobles.NobleStatus;
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
  
  private List<Integer> purchasedcards = new ArrayList<Integer>();
  private List<Integer> reservedcards = new ArrayList<Integer>();
  private Map<TokenType, Integer> tokens = new HashMap<TokenType, Integer>();
  private String userName;
  private int prestige;
  private List<Integer> visitingNobles = new ArrayList<>();
  private List<Integer> reservedNobles = new ArrayList<>();
  private List<Power> powers;
  private CoatOfArmsPile coatOfArmsPile;

  /**
   * Creates new inventoryjson. Should be based on actual user inventory from splendorgame.
   *
   * @param cards in the players inventory
   * @param tokens in the players inventory
   * @param userName of the player 
   * @param prestige earned by the player
   * @param nobles earned by the player
   * @param powers earned by the player
   * @param pile of coat of arms
   */
  public InventoryJson(List<Card> cards, EnumMap<TokenType, TokenPile> tokens, 
      String userName, int prestige, List<Noble> nobles, 
      List<Power> powers, CoatOfArmsPile pile) {
    for (Card card : cards) {
      if (card.isPurchased()) {
        this.purchasedcards.add(card.getId());
      } else if (card.isReserved()) {
        this.reservedcards.add(card.getId());
      }
    }
    for (TokenType type : tokens.keySet()) {
      this.tokens.put(type, tokens.get(type).getSize());
    }
    this.userName = userName;
    this.prestige = prestige;
    for (Noble noble : nobles) {
      if (noble.getStatus() == NobleStatus.VISITING) {
        this.visitingNobles.add(noble.getId());
      } else if (noble.getStatus() == NobleStatus.RESERVED) {
        this.reservedNobles.add(noble.getId());
      }
    }
    this.powers = powers;
    coatOfArmsPile = pile;
  }
}
