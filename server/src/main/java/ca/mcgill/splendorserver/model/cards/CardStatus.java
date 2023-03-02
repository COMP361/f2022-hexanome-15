package ca.mcgill.splendorserver.model.cards;

/**
 * Status of a Splendor Card.
 * Can either be reserved, purchased or neither.
 */
public enum CardStatus {
  /**
   * The card is reserved by a player.
   */
  RESERVED,
  /**
   * The card is purchased by a player.
   */
  PURCHASED,
  /**
   * The card is on the game board.
   */
  NONE
}
