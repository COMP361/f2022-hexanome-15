package ca.mcgill.splendorserver.model.action;

/**
 * Represents an action made in the Splendor game.
 * A player can purchase cards, reserve cards or take gem tokens.
 */
public enum Action {

  /**
   * Purchase type Action. Card must be face up in middle of table or a previously reserved one.
   */
  PURCHASE_DEV, PURCHASE_DEV_RECEIVE_NOBLE,
  /**
   * Reserve type Action.
   */
  RESERVE_DEV_TAKE_JOKER,
  TAKE_3_GEM_TOKENS_DIFF_COL,
  TAKE_3_GEM_TOKENS_DIFF_COL_RET_2,
  /*
  this action is only possible if there are at least 4 tokens of the chosen color left
  when the player takes them
  */
  TAKE_2_GEM_TOKENS_SAME_COL,
  TAKE_2_GEM_TOKENS_SAME_COL_RET_2, TAKE_3_GEM_TOKENS_DIFF_COL_RET_1,
  TAKE_3_GEM_TOKENS_DIFF_COL_RET_3, RESERVE_DEV, TAKE_2_GEM_TOKENS_SAME_COL_RET_1


}
