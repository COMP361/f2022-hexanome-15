package ca.mcgill.splendorserver.model.cities;

import ca.mcgill.splendorserver.model.cards.CardCost;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO: Make cities flyweights

/**
 * Represents a Splendor Cities expansion City
 * with required prestige and gem amount to obtain the city.
 */
public class City {
  private final int requiredPrestige;
  private final CardCost requiredCardBonuses;
  private static final List<City> cities = new ArrayList<City>();
  private static final List<City> citiesInGame = new ArrayList<>();

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

  /**
   * Returns the cities that are currently on the game board.
   *
   * @param numPlayers the number of players in the game
   * @return a list of cities that are currently on the game board
   */
  public static List<City> getCities(int numPlayers) {
    if (cities.size() == 0) {
      generateCities();
    }
    Random random = new Random();
    if (citiesInGame.size() == 0) {
      for (int i = 0; i < numPlayers; i++) {
        int randomIndex = random.nextInt(cities.size());
        City randomCity = cities.get(randomIndex);
        citiesInGame.add(randomCity);
      }
    }
    return citiesInGame;
  }

  /**
   * Generates all cities
   * Then 2-4 random cities will be added to the game.
   */
  private static void generateCities() {
    cities.add(new City(13, new CardCost(0, 0, 0, 4, 3)));
    cities.add(new City(13, new CardCost(3, 4, 0, 0, 0)));
    cities.add(new City(17, new CardCost(0, 0, 0, 0, 0)));
    cities.add(new City(13, new CardCost(0, 0, 3, 0, 4)));
    cities.add(new City(13, new CardCost(4, 0, 0, 3, 0)));
    cities.add(new City(16, new CardCost(1, 1, 1, 1, 1)));
    cities.add(new City(14, new CardCost(2, 1, 1, 2, 2)));
    cities.add(new City(11, new CardCost(3, 3, 0, 3, 3)));
    cities.add(new City(11, new CardCost(3, 0, 3, 3, 3)));
    cities.add(new City(13, new CardCost(0, 3, 4, 0, 0)));
    cities.add(new City(13, new CardCost(2, 2, 2, 2, 2)));

    //TODO: Make it possible to have a card cost of n cards of the same type
    /*cities.add(new City(12, new CardCost(6,0,0,0,0)));
    cities.add(new City(15, new CardCost(5,0,0,0,0)));
    cities.add(new City(14, new CardCost(0,0,4,4,0)));*/
  }

}
