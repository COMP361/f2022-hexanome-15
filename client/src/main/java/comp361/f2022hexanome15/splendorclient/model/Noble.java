package comp361.f2022hexanome15.splendorclient.model;

/**
 * Represents a Splendor Noble with reserved status.
 */
public class Noble {

  private int[] cardRequirements;
  private boolean reserved;

  public Noble(int[] requirements) {
    this.cardRequirements = requirements;
    this.reserved = false;
  }

  public int[] getCardRequirements() {
    return cardRequirements;
  }

  public boolean isReserved() {
    return reserved;
  }
}
