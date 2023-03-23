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
  private CardCost cost;
  private TradingPostSlot tradingSlot;

  @BeforeEach
  void setUp() {
    cost = new CardCost(1, 0, 0, 3, 0);
    tradingSlot = new TradingPostSlot(0, true, GOLD_TOKENS_WORTH_2_GEMS_SAME_COL, cost);
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
  void getId() {
    assertEquals(0, tradingSlot.getId());
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

  @Test
  void getAcquiredCoatOfArms() {
    assertEquals(0, tradingSlot.getAcquiredCoatOfArmsList().size());
  }

  @Test
  void removeCoatOfArms() {
    CoatOfArms cta1 = new CoatOfArms(BLUE);
    tradingSlot.addCoatOfArms(cta1);
    CoatOfArms removed = tradingSlot.removeCoatOfArms(cta1.getType());
    assertEquals(cta1, removed);
  }

  @Test
  void removeCoatOfArmsNull() {
    CoatOfArms cta1 = new CoatOfArms(BLUE);
    tradingSlot.addCoatOfArms(cta1);
    CoatOfArms removed = tradingSlot.removeCoatOfArms(RED);
    assertEquals(null, removed);
  }
}