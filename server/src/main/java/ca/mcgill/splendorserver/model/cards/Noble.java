package ca.mcgill.splendorserver.model.cards;

/**
 * Represents a Splendor Noble with reserved status.
 */
public class Noble {

  private final int[]   cardRequirements;
  private final boolean reserved;

  /**
   * Creates a noble.
   *
   * @param requirements the cards required to acquire this noble
   */
  public Noble(int[] requirements) {
    this.cardRequirements = requirements;
    this.reserved = false;
  }

  /**
   * Returns this noble's card requirements.
   *
   * @return this noble's card requirements
   */
  public int[] getCardRequirements() {
    return cardRequirements;
  }

  /**
   * Returns the reserved status of this noble.
   *
   * @return the reserved status of this noble
   */
  public boolean isReserved() {
    return reserved;
  }
}
