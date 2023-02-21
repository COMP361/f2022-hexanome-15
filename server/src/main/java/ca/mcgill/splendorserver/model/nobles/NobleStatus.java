package ca.mcgill.splendorserver.model.nobles;

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
