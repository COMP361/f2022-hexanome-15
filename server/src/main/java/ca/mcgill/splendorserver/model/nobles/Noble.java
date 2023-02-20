package ca.mcgill.splendorserver.model.nobles;

import ca.mcgill.splendorserver.model.cards.CardCost;

/**
 * Represents a Splendor Noble with reserved status.
 */
public class Noble {

  private final CardCost visitRequirements;
  private final NobleStatus status;
  private final int         prestige;

  /**
   * Creates a noble.
   *
   * @param visitRequirements the token type bonus requirements for visiting this
   */
  public Noble(CardCost visitRequirements, int prestige) {
    assert visitRequirements != null && prestige >= 0;
    this.visitRequirements = visitRequirements;
    this.status            = NobleStatus.ON_BOARD;
    this.prestige          = prestige;
  }

  public int getPrestige() {
    return prestige;
  }

  /**
   * Returns this noble's card requirements.
   *
   * @return this noble's card requirements
   */
  public CardCost getVisitRequirements() {
    return visitRequirements;
  }

  /**
   * Returns the reserved status of this noble.
   *
   * @return the reserved status of this noble
   */
  public NobleStatus getStatus() {
    return status;
  }
}
