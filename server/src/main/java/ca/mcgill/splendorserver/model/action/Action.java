package ca.mcgill.splendorserver.model.action;

/**
 * Represents an action made in the Splendor game.
 * A player can purchase cards, reserve cards or take gem tokens.
 */
public enum Action {

  /**
   * Purchase type Action. Card must be face up in middle of table or a previously reserved one.
   */
  PURCHASE_DEV,
  /**
   * Reserve type Action.
   */
  RESERVE_DEV_TAKE_JOKER,
  TAKE_3_GEM_TOKENS_DIFF_COL,
  /*
  this action is only possible if there are at least 4 tokens of the chosen color left
  when the player takes them
  */
  TAKE_2_GEM_TOKENS_SAME_COL


}
