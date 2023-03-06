package ca.mcgill.splendorserver.model.cities;

import ca.mcgill.splendorserver.model.cards.CardCost;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.TWO;
import static org.junit.jupiter.api.Assertions.*;

class CityTest {

  List<City> cities = new ArrayList<>();
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
  void getCities() {
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
    assertEquals(cities.get(0).getRequiredPrestige(),Lima.getCities(4).get(0).getRequiredPrestige(),"");
  }
}