package ca.mcgill.splendorserver.model.tradingposts;

import ca.mcgill.splendorserver.model.cards.CardCost;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Splendor Trading Posts expansion Trading Post slot
 * with coat of arm slots and powers.
 * Each slot requires certain cards and one specific slot requires a noble to be unlocked.
 */
public class TradingPostSlot {
  private final int id;
  private final boolean requiresNoble;
  private final Power power;
  private final CardCost cardRequirements;
  private final List<CoatOfArms> acquiredCoatOfArmsList;
  private static final List<TradingPostSlot> tradingPostSlots = new ArrayList<>();

  /**
   * Returns the acquired coat of arms in the trading post slot.
   *
   * @return the acquired coat of arms in the trading post slot
   */
  public List<CoatOfArms> getAcquiredCoatOfArmsList() {
    return acquiredCoatOfArmsList;
  }

  /**
   * Creates a Trading Post slot.
   *
   * @param id the trading post slot id
   * @param requiresNoble A boolean to describe whether this slot requires a noble to gain its power
   * @param power The power given to a player after they acquire this trading post slot
   * @param cardRequirements The cards required to acquire this trading post slot
   */
  public TradingPostSlot(int id, boolean requiresNoble, Power power, CardCost cardRequirements) {
    assert id >= 0 && power != null && cardRequirements != null;
    this.requiresNoble = requiresNoble;
    this.power = power;
    this.cardRequirements = cardRequirements;
    this.acquiredCoatOfArmsList = new ArrayList<>();
    this.id = id;
  }

  /**
   * Returns a boolean determining if this trading post slot requires a noble to acquire it.
   *
   * @return the desired boolean
   */
  public boolean isRequiresNoble() {
    return requiresNoble;
  }

  /**
   * Returns the power obtained upon acquiring this trading post slot..
   *
   * @return the obtained power
   */
  public Power getPower() {
    return power;
  }

  /**
   * Returns the cards required to acquire this trading post slot.
   *
   * @return the cards required to acquire this trading post slot
   */
  public CardCost getCardRequirements() {
    return cardRequirements;
  }

  /**
   * Returns the id of this trading post slot.
   *
   * @return the id of this trading post slot
   */
  public int getId() {
    return id;
  }

  /**
   * Checks whether the trading post slot has all possible coat of arms placed on it.
   *
   * @return a boolean determining whether the trading post slot is full
   */
  public boolean isFull() {
    return acquiredCoatOfArmsList.size() >= 4;
  }

  /**
   * Adds a Coat of Arms to the trading post slot.
   *
   * @param coatOfArms The Coat of Arms to be added
   */
  public void addCoatOfArms(CoatOfArms coatOfArms) {
    assert coatOfArms != null;
    if (!isFull() && !acquiredCoatOfArmsList.contains(coatOfArms)) {
      acquiredCoatOfArmsList.add(coatOfArms);
    }
  }

  /**
   * Removes a Coat of Arms from the trading post slot.
   *
   * @param coatOfArms The Coat of Arms to be removed
   * @return the removed coat of arms
   */
  public CoatOfArms removeCoatOfArms(CoatOfArms coatOfArms) {
    assert coatOfArms != null && acquiredCoatOfArmsList.contains(coatOfArms);
    int index = acquiredCoatOfArmsList.indexOf(coatOfArms);
    return acquiredCoatOfArmsList.remove(index);
  }

  /**
   * Returns the trading post slots that are currently in the game.
   *
   * @return a list of trading post slots that are currently in the game
   */
  public static List<TradingPostSlot> getTradingPostSlots() {
    if (tradingPostSlots.size() == 0) {
      generateTradingPostSlots();
    }
    return tradingPostSlots;
  }

  /**
   * Generates all trading post slots.
   */
  private static void generateTradingPostSlots() {
    tradingPostSlots.add(new TradingPostSlot(0, false, Power.PURCHASE_CARD_TAKE_TOKEN,
          new CardCost(1, 0, 0, 3, 0)));
    tradingPostSlots.add(new TradingPostSlot(1, false,
          Power.TAKE_2_GEMS_SAME_COL_AND_TAKE_1_GEM_DIF_COL,
          new CardCost(2, 0, 0, 0, 0)));
    tradingPostSlots.add(new TradingPostSlot(2, false, Power.GOLD_TOKENS_WORTH_2_GEMS_SAME_COL,
          new CardCost(0, 3,  0, 0, 1)));
    tradingPostSlots.add(new TradingPostSlot(3, true, Power.GAIN_5_PRESTIGE,
          new CardCost(0, 0, 5, 0, 0)));
    tradingPostSlots.add(new TradingPostSlot(4, false,
          Power.GAIN_1_PRESTIGE_FOR_EVERY_PLACED_COAT_OF_ARMS,
          new CardCost(0, 0, 0, 0, 3)));
  }
}
