package ca.mcgill.splendorserver.model.tradingposts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.BLUE;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static org.junit.jupiter.api.Assertions.*;

class CoatOfArmsTest {
  CoatOfArms cta;

  @BeforeEach
  void setUp() {
    cta = new CoatOfArms(BLUE);
  }

  @Test
  void getType() {
    assertEquals(BLUE, cta.getType());
  }

  @Test
  void testEquals() {
    CoatOfArms cta2 = new CoatOfArms(BLUE);
    assertEquals(cta, cta2);
  }

  @Test
  void testHashCode() {
    CoatOfArms cta2 = new CoatOfArms(BLUE);
    assertEquals(cta.hashCode(), cta2.hashCode());
  }
}