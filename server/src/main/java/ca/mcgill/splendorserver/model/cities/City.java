package ca.mcgill.splendorserver.model.cities;

import ca.mcgill.splendorserver.model.cards.CardCost;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Represents a Splendor Cities expansion City
 * with required prestige and gem amount to obtain the city.
 */
public class City {

  private final int id;
  private final int requiredPrestige;
  private final CardCost requiredCardBonuses;
  private final int numSameCards;
  private static final List<City> cities = new ArrayList<City>();
  private static List<City> citiesInGame = new ArrayList<>();

  /**
   * Creates a city.
   *
   * @param id the city id
   * @param requiredPrestige the prestige required to obtain this city
   * @param requiredCardBonuses the card bonuses required to obtain this city
   * @param numSameCards the number of cards of the same token type that this city requires
   */
  public City(int id, int requiredPrestige, CardCost requiredCardBonuses, int numSameCards) {
    assert id >= 0 && requiredPrestige >= 0 && requiredCardBonuses != null;
    this.requiredPrestige = requiredPrestige;
    this.requiredCardBonuses = requiredCardBonuses;
    this.id = id;
    this.numSameCards = numSameCards;
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

  /**
   * Returns the id of this city.
   *
   * @return the id of this city
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the number of cards of the same token type that this city requires.
   *
   * @return the number of cards of the same token type that this city requires
   */
  public int getNumSameCards() {
    return numSameCards;
  }

  /**
   * Returns the cities that are currently on the game board.
   *
   * @param numPlayers the number of players in the game
   * @return a list of cities that are currently on the game board
   */
  public static List<City> getCities(int numPlayers) {
    assert numPlayers >= 2 && numPlayers <= 4;
    if (cities.size() == 0) {
      generateCities();
    }
    Collections.shuffle(cities);
    citiesInGame = new ArrayList<>();
    for (int i = 0; i < numPlayers; i++) {
      City city = cities.get(i);
      citiesInGame.add(city);
    }
    return citiesInGame;
  }
  
  /**
   * Gets city by id
   *
   * @param id of sought city
   * @return the city
   */
  public static City getCity(int id) {
    if (cities.size() == 0) {
      generateCities();
    }
    return cities.get(id);
  }

  /**
   * Generates all cities
   * Then 2-4 random cities will be added to the game.
   */
  private static void generateCities() {
    cities.add(new City(0, 13,
        new CardCost(0, 0, 0, 4, 3), 0));
    cities.add(new City(1, 13,
        new CardCost(3, 4, 0, 0, 0), 0));
    cities.add(new City(2, 17,
        new CardCost(0, 0, 0, 0, 0), 0));
    cities.add(new City(3, 13,
        new CardCost(0, 0, 3, 0, 4), 0));
    cities.add(new City(4, 13,
        new CardCost(4, 0, 0, 3, 0), 0));
    cities.add(new City(5, 16,
        new CardCost(1, 1, 1, 1, 1), 0));
    cities.add(new City(6, 14,
        new CardCost(2, 1, 1, 2, 2), 0));
    cities.add(new City(7, 11,
        new CardCost(3, 3, 0, 3, 3), 0));
    cities.add(new City(8, 11,
        new CardCost(3, 0, 3, 3, 3), 0));
    cities.add(new City(9, 13,
        new CardCost(0, 3, 4, 0, 0), 0));
    cities.add(new City(10, 13,
        new CardCost(2, 2, 2, 2, 2), 0));
    cities.add(new City(11, 12,
        new CardCost(0, 0, 0, 0, 0), 6));
    cities.add(new City(12, 14,
        new CardCost(0, 0, 4, 0, 0), 4));
    cities.add(new City(13, 15,
        new CardCost(5, 0, 0, 0, 0), 5));
  }
}
