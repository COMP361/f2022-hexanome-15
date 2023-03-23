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
   * Noble visit type Action.
   */
  RECEIVE_NOBLE,
  /**
   * This action allows players to return 1 token to the bank.
   */
  RET_TOKEN,
  /**
   * Reserve type action.
   * A gold token is not received upon reserving a card because there are no gold tokens left.
   */
  RESERVE_DEV,
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
   * Discard a white card type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_FIRST_WHITE_CARD,
  /**
   * Discard a white card type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_SECOND_WHITE_CARD,
  /**
   * Discard a blue card type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_FIRST_BLUE_CARD,
  /**
   * Discard a blue card type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_SECOND_BLUE_CARD,
  /**
   * Discard a green card type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_FIRST_GREEN_CARD,
  /**
   * Discard a green card type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_SECOND_GREEN_CARD,
  /**
   * Discard a red card type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_FIRST_RED_CARD,
  /**
   * Discard a red card type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_SECOND_RED_CARD,
  /**
   * Discard a black card type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_FIRST_BLACK_CARD,
  /**
   * Discard a black card type action.
   * Some Orient expansion cards force a player
   * to discard 2 cards of a certain type
   * in their inventory upon purchasing the card.
   */
  DISCARD_SECOND_BLACK_CARD,
  /**
   * Place coat of arms type action.
   * This occurs automatically after noble visitation.
   * If a player qualifies for a trading post slot the coat of arms
   * of their colour is placed on the trading post slot.
   */
  PLACE_COAT_OF_ARMS,
  /**
   * Taking an extra token as a result of a trading post power.
   */
  TAKE_EXTRA_TOKEN

}
