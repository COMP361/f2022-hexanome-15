package ca.mcgill.splendorserver.model.nobles;

import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cities.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NobleTest {

  private CardCost cardc;
  private Noble anoble;

  @BeforeEach
  void setUp() {
    cardc = new CardCost(4, 4, 0, 0, 0);
    anoble = new Noble(0, cardc);
  }

  @Test
  void getPrestige() {
    assertEquals(3,anoble.getPrestige());
  }

  @Test
  void getVisitRequirements() {
    assertEquals(cardc, anoble.getVisitRequirements());
  }

  @Test
  void getStatus() {
    assertEquals(NobleStatus.ON_BOARD,anoble.getStatus());
  }

  @Test
  void getId() {
    assertEquals(0, anoble.getId());
  }

  @Test
  void setStatus() {
    anoble.setStatus(NobleStatus.VISITING);
    assertEquals(NobleStatus.VISITING, anoble.getStatus());
  }

  @Test
  void getNoblesTwoPlayers() {
    assertEquals(3, Noble.getNobles(2).size());
  }

  @Test
  void getNoblesThreePlayers() {
    assertEquals(4, Noble.getNobles(3).size());
  }

  @Test
  void getNoblesFourPlayers() {
    assertEquals(5, Noble.getNobles(4).size());
  }

  @Test
  void testEqualsSameNoble() {
    assertEquals(anoble, anoble);
  }

  @Test
  void getNoble() {
    assertEquals(anoble, Noble.getNoble(0));
  }

  @Test
  void testEquals() {
    CardCost cost = new CardCost(4, 4, 0, 0, 0);
    Noble noble1 = new Noble(0, cost);
    assertEquals(anoble, noble1);
  }

  @Test
  void testEqualsNotNoble() {
    assertFalse(anoble.equals(null));
  }

  @Test
  void testHashCode() {
    CardCost cost = new CardCost(4, 4, 0, 0, 0);
    Noble noble1 = new Noble(0, cost);
    assertEquals(anoble.hashCode(), noble1.hashCode());
  }

}