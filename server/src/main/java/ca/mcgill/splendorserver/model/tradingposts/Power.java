package ca.mcgill.splendorserver.model.tradingposts;

/**
 * Represents a Splendor Trading Posts expansion Power.
 */
public enum Power {
  /**
   * This power allows a player to take a token every time they purchase a card.
   */
  PURCHASE_CARD_TAKE_TOKEN,
  /**
   * This power allows a player to take an extra different coloured token if they take two tokens of the same colour.
   */
  TAKE_2_GEMS_SAME_COL_AND_TAKE_1_GEM_DIF_COL,
  /**
   * This power allows a player to immediately gain 5 prestige.
   */
  GAIN_5_PRESTIGE,
  /**
   * This power allows a player to gain 1 prestige for every coat of arms they placed on the board.
   */
  GAIN_1_PRESTIGE_FOR_EVERY_PLACED_COAT_OF_ARMS,
  /**
   * Gold tokens are now worth 2 tokens of the same colour for any player that acquires this power.
   */
  GOLD_TOKENS_WORTH_2_GEMS_SAME_COL
}
