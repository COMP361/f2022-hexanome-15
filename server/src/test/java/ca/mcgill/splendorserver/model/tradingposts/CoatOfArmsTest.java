package ca.mcgill.splendorserver.model.tradingposts;

import org.junit.jupiter.api.Test;

import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.BLUE;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static org.junit.jupiter.api.Assertions.*;

class CoatOfArmsTest {
  CoatOfArms cta = new CoatOfArms(BLUE);
  CoatOfArms cta2 = new CoatOfArms(RED);



  @Test
  void getType() {
    assertEquals(BLUE, cta.getType(), "");
  }

  @Test
  void testEquals() {
    assertFalse(cta.equals(cta2),"");
    assertTrue(cta.equals(cta),"");
  }

  /*@Test
  void testHashCode() {
    assertEquals(2030538934,cta.hashCode(),"");
  }*/
}