package model;

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
}
