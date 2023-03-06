package ca.mcgill.splendorserver.model.cities;

import ca.mcgill.splendorserver.model.cards.CardCost;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CityTest {

  CardCost cost = new CardCost(1,2,3,2,1);
  City Lima = new City(3,cost);

  @Test
  void getRequiredPrestige() {
    assertEquals(3,Lima.getRequiredPrestige(),"");
  }

  @Test
  void getRequiredCardBonuses() {
    assertEquals(cost,Lima.getRequiredCardBonuses(),"");
  }

  @Test
  void getCitiesFourPlayers() {
    assertEquals(4, City.getCities(4).size());
  }
}