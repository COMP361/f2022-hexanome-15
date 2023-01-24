package ca.mcgill.splendorserver.model.cards;

/**
 * Type of Card.
 * Cards can belong to the base back or the orient pack.
 * The numbers are used to indicate the level of the card
 */
public enum CardType {
  /**
   * Base 1 card type.
   */
  BASE1,
  /**
   * Base 2 card type.
   */
  BASE2,
  /**
   * Base 3 card type.
   */
  BASE3,
  /**
   * Orient 1 card type.
   */
  ORIENT1,
  /**
   * Orient 2 card type.
   */
  ORIENT2,
  /**
   * Orient 3 card type.
   */
  ORIENT3
}
