package ca.mcgill.splendorserver.model.action;

/**
 * Represents an action made in the Splendor game.
 * A player can purchase cards, reserve cards or take gem tokens.
 * Additional actions are added for extensions.
 */
public enum Action {

  /**
   * Purchase type Action. Card must be face up in middle of table or a previously reserved one.
   */
  PURCHASE_DEV,
  
  /**
   * Taking one token at a time. 
   */
  TAKE_TOKEN,
  /**
   * Purchase type Action. A noble visits the current player after they have purchased a card.
   */
  PURCHASE_DEV_RECEIVE_NOBLE,
  /**
   * This action allows players to take 3 different-coloured gem tokens.
   */
  TAKE_3_GEM_TOKENS_DIFF_COL,
  /**
   * This action allows players to take 3 different-coloured gem tokens.
   * 1 token must be returned to the bank
   * because the player has 11 tokens after taking the 3 new tokens.
   */
  TAKE_3_GEM_TOKENS_DIFF_COL_RET_1,
  /**
   * This action allows players to take 3 different-coloured gem tokens.
   * 2 tokens must be returned to the bank
   * because the player has 12 tokens after taking the 3 new tokens.
   */
  TAKE_3_GEM_TOKENS_DIFF_COL_RET_2,
  /**
   * This action allows players to take 3 different-coloured gem tokens.
   * 3 tokens must be returned to the bank
   * because the player has 13 tokens after taking the 3 new tokens.
   */
  TAKE_3_GEM_TOKENS_DIFF_COL_RET_3,
  /**
   * This action allows players to take 2 same-coloured gem tokens.
   * This action is only possible if there are at least 4 tokens of the chosen color left
   * when the player takes them.
  */
  TAKE_2_GEM_TOKENS_SAME_COL,
  /**
   * This action allows players to take 2 same-coloured gem tokens.
   * 1 token must be returned to the bank
   * because the player has 11 tokens after taking the 2 new tokens.
   */
  TAKE_2_GEM_TOKENS_SAME_COL_RET_1,
  /**
   * This action allows players to take 2 same-coloured gem tokens.
   * 2 tokens must be returned to the bank
   * because the player has 12 tokens after taking the 2 new tokens.
   */
  TAKE_2_GEM_TOKENS_SAME_COL_RET_2,
  /**
   * Reserve type action.
   * A gold token is not received upon reserving a card because there are no gold tokens left.
   */
  RESERVE_DEV,
  /**
   * Reserve type Action. A gold token is received upon reserving a card.
   */
  RESERVE_DEV_TAKE_JOKER,
  /**
   * Reserve noble type action.
   * Some Orient expansion cards allow a player to reserve a noble upon purchasing the card.
   */
  RESERVE_NOBLE,
  /**
   * Cascade level 1 type action.
   * Some Orient expansion cards allow a player
   * to choose a free level 1 Orient card upon purchasing the card.
   */
  CASCADE_LEVEL_1,
  /**
   * Cascade level 2 type action.
   * Some Orient expansion cards allow a player
   * to choose a free level 2 Orient card upon purchasing the card.
   */
  CASCADE_LEVEL_2,
  /**
   * Pair spice card type action.
   * Some Orient expansion cards allow a player
   * to pair the purchased card with a development card in their inventory.
   * The paired spice card will then have the same gem discount type
   * as the card that it was paired with.
   * The gem discount will be worth 1.
   */
  PAIR_SPICE_CARD,
  /**
   * Discard 2 white cards type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_2_WHITE_CARDS,
  /**
   * Discard 2 blue cards type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_2_BLUE_CARDS,
  /**
   * Discard 2 green cards type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_2_GREEN_CARDS,
  /**
   * Discard 2 red cards type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_2_RED_CARDS,
  /**
   * Discard 2 black cards type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_2_BLACK_CARDS,
  /**
   * Take 1 gem token type action.
   * This is used for two Trading post expansion powers.
   * One where a player is allowed to take 1 token after purchasing a card.
   * The other where a player is allowed to take 1 token
   * of a different colour after taking 2 same-coloured gems.
   */
  TAKE_1_GEM_TOKEN,
  /**
   * Take 1 gem token type action.
   * This is used for two Trading post expansion powers.
   * One where a player is allowed to take 1 token after purchasing a card.
   * The other where a player is allowed to take 1 token
   * of a different colour after taking 2 same-coloured gems.
   * 1 token must be returned to the bank
   * because the player has 11 tokens after taking the 2 new tokens.
   */
  TAKE_1_GEM_TOKEN_RET_1,
  /**
   * Place coat of arms type action.
   * This occurs automatically after noble visitation.
   * If a player qualifies for a trading post slot the coat of arms
   * of their colour is placed on the trading post slot.
   */
  PLACE_COAT_OF_ARMS


}
