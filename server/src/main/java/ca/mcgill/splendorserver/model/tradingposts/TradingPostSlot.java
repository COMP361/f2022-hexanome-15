package ca.mcgill.splendorserver.model.tradingposts;

import ca.mcgill.splendorserver.model.cards.CardCost;

import java.util.ArrayList;
import java.util.List;

public class TradingPostSlot {
  private final boolean requiresNoble;
  private final Power power;
  private final CardCost cardRequirements;
  private final List<CoatOfArms> acquiredCoatOfArmsList;
  private static final List<TradingPostSlot> tradingPostSlots = new ArrayList<>();

  /**
   * Creates a Trading Post slot.
   *
   * @param requiresNoble A boolean to describe whether this slot requires a noble to gain its power
   * @param power The power given to a player after they acquire this trading post slot
   * @param cardRequirements The cards required to acquire this trading post slot
   */
  public TradingPostSlot(boolean requiresNoble, Power power, CardCost cardRequirements) {
    assert power != null && cardRequirements != null;
    this.requiresNoble = requiresNoble;
    this.power = power;
    this.cardRequirements = cardRequirements;
    this.acquiredCoatOfArmsList = new ArrayList<>();
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
   * Generates all trading post slots.
   */
  private static void generateTradingPostSlots() {
    tradingPostSlots.add(new TradingPostSlot(false, Power.PURCHASE_CARD_TAKE_TOKEN, new CardCost(1, 0, 0, 3, 0)));
    tradingPostSlots.add(new TradingPostSlot(false, Power.TAKE_2_GEMS_SAME_COL_AND_TAKE_1_GEM_DIF_COL, new CardCost(2, 0, 0, 0, 0)));
    tradingPostSlots.add(new TradingPostSlot(false, Power.GOLD_TOKENS_WORTH_2_GEMS_SAME_COL, new CardCost(0,3, 0,0,1)));
    tradingPostSlots.add(new TradingPostSlot(true, Power.GAIN_5_PRESTIGE, new CardCost(0,0,5,0,0)));
    tradingPostSlots.add(new TradingPostSlot(false, Power.GAIN_1_PRESTIGE_FOR_EVERY_PLACED_COAT_OF_ARMS, new CardCost(0,0,0,0,3)));
  }
}
