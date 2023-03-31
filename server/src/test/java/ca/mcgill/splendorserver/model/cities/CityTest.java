package ca.mcgill.splendorserver.model.cities;

import ca.mcgill.splendorserver.model.cards.CardCost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CityTest {

  private CardCost cost;
  private City Lima;

  @BeforeEach
  void setUp() {
    cost = new CardCost(1,2,3,2,1);
    Lima = new City(0, 3, cost, 0);
  }

  @Test
  void getRequiredPrestige() {
    assertEquals(3, Lima.getRequiredPrestige());
  }

  @Test
  void getRequiredCardBonuses() {
    assertEquals(cost,Lima.getRequiredCardBonuses());
  }

  @Test
  void getCities() {
    assertEquals(3, City.getCities().size());
  }

  @Test
  void getId() {
    assertEquals(0, Lima.getId());
  }

  @Test
  void getNumSameCards() {
    assertEquals(0, Lima.getNumSameCards());
  }
}