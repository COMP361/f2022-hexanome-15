package ca.mcgill.splendorserver.model.cities;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ca.mcgill.splendorserver.model.cards.DeckType.BASE1;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static org.junit.jupiter.api.Assertions.*;

class CityTest {

  private CardCost cost;
  private City city;

  @BeforeEach
  void setUp() {
    cost = new CardCost(0, 0, 0, 4, 3);
    city = new City(0, 13, cost, 0);
  }

  @Test
  void getRequiredPrestige() {
    assertEquals(13, city.getRequiredPrestige());
  }

  @Test
  void getRequiredCardBonuses() {
    assertEquals(cost, city.getRequiredCardBonuses());
  }

  @Test
  void getCities() {
    assertEquals(3, City.getCities().size());
  }

  @Test
  void getId() {
    assertEquals(0, city.getId());
  }

  @Test
  void getNumSameCards() {
    assertEquals(0, city.getNumSameCards());
  }

  @Test
  void getCity() {
    assertEquals(city, City.getCity(0));
  }

  @Test
  void testEqualsSameCity() {
    assertEquals(city, city);
  }

  @Test
  void testEquals() {
    CardCost cost1 = new CardCost(0, 0, 0, 4, 3);
    City city1 = new City(0, 13, cost1, 0);
    assertEquals(city, city1);
  }

  @Test
  void testEqualsNotCity() {
    assertFalse(city.equals(null));
  }

  @Test
  void testHashCode() {
    CardCost cost1 = new CardCost(0, 0, 0, 4, 3);
    City city1 = new City(0, 13, cost1, 0);
    assertEquals(city.hashCode(), city1.hashCode());
  }
}