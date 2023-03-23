package ca.mcgill.splendorserver.model.tradingposts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Splendor Trading Posts expansion Coat of Arms pile with Coat of Arms and type.
 */
public class CoatOfArmsPile {
  private final CoatOfArmsType type;
  private final List<CoatOfArms> coatOfArmsList;

  /**
   * Creates a CoatOfArmsPile.
   *
   * @param type The type of CoatOfArms that are in the CoatOfArmsPile
   */
  public CoatOfArmsPile(CoatOfArmsType type) {
    assert type != null;
    this.type = type;
    this.coatOfArmsList = new ArrayList<>();
    setUp();
  }

  /**
   * Returns the color type of coat of arms in the pile.
   *
   * @return the color type of coat of arms in the pile
   */
  public CoatOfArmsType getType() {
    return type;
  }

  /**
   * Adds a Coat of Arms to the pile.
   *
   * @param coatOfArms the coat of arms to be added
   */
  public void addCoatOfArms(CoatOfArms coatOfArms) {
    assert coatOfArms != null;
    coatOfArmsList.add(coatOfArms);
  }

  /**
   * Removes a Coat of Arms from the pile.
   * Assumes that the coat of arms list is not empty.
   *
   * @return the coat of arms that is removed
   */
  public CoatOfArms removeCoatOfArms() {
    assert !coatOfArmsList.isEmpty();
    return coatOfArmsList.remove(0);
  }

  /**
   * Returns the number of coat of arms in the pile.
   *
   * @return the size of the coat of arms pile
   */
  public int getSize() {
    return coatOfArmsList.size();
  }

  /**
   * Sets up the Coat of Arms piles with 5 coat of arms upon starting the game.
   */
  private void setUp() {
    for (int i = 0; i < 5; i++) {
      CoatOfArms c = new CoatOfArms(this.type);
      addCoatOfArms(c);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CoatOfArmsPile)) {
      return false;
    }
    CoatOfArmsPile that = (CoatOfArmsPile) o;
    return type == that.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type);
  }
}

