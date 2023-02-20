package ca.mcgill.splendorserver.model.tradingpost;

/**
 * Represents a Splendor Trading Posts expansion Coat of Arms with color type.
 */
public class CoatOfArms {
  private final CoatOfArmsType type;

  /**
   * Creates a coat of arms.
   *
   * @param type the color type of the coat of arms
   */
  public CoatOfArms(CoatOfArmsType type) {
    assert type != null;
    this.type = type;
  }

  /**
   * Returns this coat of arms' type.
   *
   * @return this coat of arms' type
   */
  public CoatOfArmsType getType() {
    return type;
  }
}
