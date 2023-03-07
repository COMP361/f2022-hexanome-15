package ca.mcgill.splendorserver.model.tradingposts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.*;
import static org.junit.jupiter.api.Assertions.*;

class CoatOfArmsPileTest {
  CoatOfArms cta;
  CoatOfArmsPile ctaP;

  @BeforeEach
  void setUp() {
    ctaP = new CoatOfArmsPile(BLUE);
    cta = new CoatOfArms(BLUE);
  }
  @Test
  void getType() {
    assertEquals(BLUE, ctaP.getType());
  }

  @Test
  void removeCoatOfArmsTest() {
    CoatOfArms elem = ctaP.removeCoatOfArms();
    assertEquals(elem, cta);
  }

  @Test
  void getSize() {
    assertEquals(5, ctaP.getSize());
  }

  @Test
  void testEquals() {
    CoatOfArmsPile ctaP2 = new CoatOfArmsPile(BLUE);
    assertEquals(ctaP, ctaP2);
  }

  @Test
  void testHashCode() {
    CoatOfArmsPile ctaP2 = new CoatOfArmsPile(BLUE);
    assertEquals(ctaP.hashCode(), ctaP2.hashCode());
  }
}