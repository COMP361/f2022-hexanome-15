package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.nobles.NobleStatus;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsPile;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
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

  public List<Integer> purchasedCards = new ArrayList<Integer>();
  public List<Integer> reservedCards = new ArrayList<Integer>();
  public Map<TokenType, Integer> tokens = new HashMap<TokenType, Integer>();
  public Map<TokenType, Integer> purchasedCardCount = new HashMap<TokenType, Integer>();
  public String userName;
  public int prestige;
  public List<Integer> visitingNobles = new ArrayList<>();
  public List<Integer> reservedNobles = new ArrayList<>();
  public List<Power> powers;
  public CoatOfArmsType coatOfArmsType;
  public List<Integer> cities = new ArrayList<>();

  /**
   * Creates new inventoryjson. Should be based on actual user inventory from splendorgame.
   *
   * @param cards in the players inventory
   * @param tokens in the players inventory
   * @param userName of the player
   * @param prestige earned by the player
   * @param nobles earned by the player
   * @param powers earned by the player
   * @param pile in the player's inventory
   * @param cities in the player's inventory
   * @param purchasedCardCount the number of purchased cards per token type in the inventory
   */
  public InventoryJson(List<Card> cards, EnumMap<TokenType, TokenPile> tokens,
                       String userName, int prestige, List<Noble> nobles,
                       List<Power> powers, CoatOfArmsPile pile, List<City> cities,
                       Map<TokenType, Integer> purchasedCardCount) {
    for (Card card : cards) {
      if (card.isPurchased()) {
        this.purchasedCards.add(card.getId());
      } else if (card.isReserved()) {
        this.reservedCards.add(card.getId());
      }
    }
    for (TokenType type : tokens.keySet()) {
      this.tokens.put(type, tokens.get(type).getSize());
    }
    this.purchasedCardCount = purchasedCardCount;
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
    for (City city : cities) {
      this.cities.add(city.getId());
    }
    if (pile == null) {
      this.coatOfArmsType = null;
    } else {
      this.coatOfArmsType = pile.getType();
    }
  }
}
