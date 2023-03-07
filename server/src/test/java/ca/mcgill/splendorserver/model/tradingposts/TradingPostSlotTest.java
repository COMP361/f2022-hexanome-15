package ca.mcgill.splendorserver.model.tradingposts;

import ca.mcgill.splendorserver.model.cards.CardCost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.*;
import static ca.mcgill.splendorserver.model.tradingposts.Power.GOLD_TOKENS_WORTH_2_GEMS_SAME_COL;
import static org.junit.jupiter.api.Assertions.*;

class TradingPostSlotTest {
  CardCost cost = new CardCost(1,2,3,2,1);
  TradingPostSlot tradingSlot;

  @BeforeEach
  void setUp() {
    tradingSlot = new TradingPostSlot(true,GOLD_TOKENS_WORTH_2_GEMS_SAME_COL,cost);
  }


  @Test
  void isRequiresNoble() {
    assertTrue(tradingSlot.isRequiresNoble());
  }

  @Test
  void getPower() {
    assertEquals(GOLD_TOKENS_WORTH_2_GEMS_SAME_COL, tradingSlot.getPower());
  }

  @Test
  void getCardRequirements() {
    assertEquals(cost,tradingSlot.getCardRequirements());
  }

  @Test
  void isFull() {
    CoatOfArms cta1 = new CoatOfArms(BLUE);
    CoatOfArms cta2 = new CoatOfArms(RED);
    CoatOfArms cta3 = new CoatOfArms(YELLOW);
    CoatOfArms cta4 = new CoatOfArms(BLACK);
    tradingSlot.addCoatOfArms(cta1);
    tradingSlot.addCoatOfArms(cta2);
    tradingSlot.addCoatOfArms(cta3);
    tradingSlot.addCoatOfArms(cta4);
    assertTrue(tradingSlot.isFull());
  }

  @Test
  void getTradingPostSlots() {
    assertEquals(5, TradingPostSlot.getTradingPostSlots().size());
  }
}