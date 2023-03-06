package ca.mcgill.splendorserver.model.tradingposts;

import org.junit.jupiter.api.Test;

import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.*;
import static org.junit.jupiter.api.Assertions.*;

class CoatOfArmsPileTest {
  CoatOfArms cta = new CoatOfArms(BLUE);
  CoatOfArms cta2 = new CoatOfArms(BLUE);
  CoatOfArmsPile ctaP = new CoatOfArmsPile(BLUE);

  @Test
  void getType() {
    assertEquals(BLUE,ctaP.getType(),"");
  }

  @Test
  void addCoatOfArms() {

    ctaP.removeCoatOfArms();
    ctaP.removeCoatOfArms();
    ctaP.removeCoatOfArms();
    ctaP.removeCoatOfArms();
    ctaP.removeCoatOfArms();
    ctaP.addCoatOfArms(cta);
    CoatOfArms elem = ctaP.removeCoatOfArms();
    assertEquals(elem,cta,"");
  }

  @Test
  void removeCoatOfArms() {
    ctaP.addCoatOfArms(cta);
    CoatOfArms elem = ctaP.removeCoatOfArms();
    assertEquals(elem,cta);
  }

  @Test
  void getSize() {
    ctaP.addCoatOfArms(cta);
    ctaP.addCoatOfArms(cta2);
    assertEquals(7,ctaP.getSize(),"");
  }
}