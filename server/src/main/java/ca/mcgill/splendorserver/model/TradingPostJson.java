package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
import java.util.List;

/**
 * Enables the creation of a stripped down trading post slot.
 *
 */
public class TradingPostJson {

  private final int id;
  private final List<CoatOfArmsType> acquiredCoatOfArmsList;

  /**
   * Creates a TradingPostJson object.
   *
   * @param id the id of the trading post
   * @param acquiredCoatOfArmsList the acquired coat of arms in the trading post
   */
  public TradingPostJson(int id, List<CoatOfArmsType> acquiredCoatOfArmsList) {
    this.id = id;
    this.acquiredCoatOfArmsList = acquiredCoatOfArmsList;
  }
}
