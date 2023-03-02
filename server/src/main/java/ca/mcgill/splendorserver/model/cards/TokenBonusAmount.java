package ca.mcgill.splendorserver.model.cards;

/**
 * Amount of token bonus a card has.
 * Can only be one for base game cards and two for special Orient cards.
 * Can be zero for spice bag Orient cards.
 */
public enum TokenBonusAmount {
  /**
   * For cards worth 0 token bonuses.
   */
  ZERO,
  /**
   * For cards worth 1 token bonus.
   */
  ONE,
  /**
   * For cards worth 2 token bonuses.
   */
  TWO
}
