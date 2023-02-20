package ca.mcgill.splendorserver.model.cities;

import ca.mcgill.splendorserver.model.cards.CardCost;

/**
 * Represents a Splendor Cities expansion City with required prestige and gem amount to obtain the city.
 */
public class City {
  private final int requiredPrestige;
  private final CardCost requiredCardBonuses;

  /**
   * Creates a city.
   *
   * @param requiredPrestige the prestige required to obtain this city
   * @param requiredCardBonuses the card bonuses required to obtain this city
   */
  public City(int requiredPrestige, CardCost requiredCardBonuses) {
    assert requiredPrestige >= 0 && requiredCardBonuses != null;
    this.requiredPrestige = requiredPrestige;
    this.requiredCardBonuses = requiredCardBonuses;
  }

  /**
   * Returns the prestige required to obtain this city.
   *
   * @return the gem amount required to obtain this city
   */
  public int getRequiredPrestige() {
    return requiredPrestige;
  }

  /**
   * Returns the card bonuses required to obtain this city.
   *
   * @return the card bonuses required to obtain this city
   */
  public CardCost getRequiredCardBonuses() {
    return requiredCardBonuses;
  }

}
