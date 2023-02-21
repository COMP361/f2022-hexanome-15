package ca.mcgill.splendorserver.model.nobles;

/**
 * Status of a Splendor Noble.
 * Can be either on the game board, visiting a player or reserved by a player.
 */
public enum NobleStatus {
  /**
  * The noble is on the game board.
  */
  ON_BOARD,
  /**
   * The noble is visiting a player.
   */
  VISITING,
  /**
   * The noble has been reserved by a player.
   */
  RESERVED
}
