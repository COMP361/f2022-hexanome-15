package ca.mcgill.splendorserver.model.tradingposts;

import ca.mcgill.splendorserver.model.cards.CardCost;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.*;
import static ca.mcgill.splendorserver.model.tradingposts.Power.GOLD_TOKENS_WORTH_2_GEMS_SAME_COL;
import static org.junit.jupiter.api.Assertions.*;

class TradingPostSlotTest {
  CoatOfArms cta = new CoatOfArms(BLUE);
  CoatOfArms cta2 = new CoatOfArms(RED);
  CoatOfArms cta3 = new CoatOfArms(YELLOW);
  CoatOfArms cta4 = new CoatOfArms(BLACK);
  CardCost cost = new CardCost(1,2,3,2,1);
  TradingPostSlot tradingSlot = new TradingPostSlot(true,GOLD_TOKENS_WORTH_2_GEMS_SAME_COL,cost);


  @Test
  void isRequiresNoble() {
    assertTrue(tradingSlot.isRequiresNoble(),"");
  }

  @Test
  void getPower() {
    assertEquals(GOLD_TOKENS_WORTH_2_GEMS_SAME_COL,tradingSlot.getPower(),"");
  }

  @Test
  void getCardRequirements() {
    assertEquals(cost,tradingSlot.getCardRequirements(),"");
  }

  @Test
  void isFull() {
    tradingSlot.addCoatOfArms(cta);
    tradingSlot.addCoatOfArms(cta2);
    tradingSlot.addCoatOfArms(cta3);
    tradingSlot.addCoatOfArms(cta4);
    assertTrue(tradingSlot.isFull(),"");
  }

  @Test
  void addCoatOfArms() {

    tradingSlot.addCoatOfArms(cta);
    tradingSlot.addCoatOfArms(cta2);
    tradingSlot.addCoatOfArms(cta3);
    tradingSlot.addCoatOfArms(cta4);
    assertTrue(tradingSlot.isFull(),"");
  }

  @Test
  void getTradingPostSlots() {
    List<TradingPostSlot> tradingPostSlots = new ArrayList();
    tradingPostSlots.add(new TradingPostSlot(false, Power.PURCHASE_CARD_TAKE_TOKEN,
      new CardCost(1, 0, 0, 3, 0)));
    tradingPostSlots.add(new TradingPostSlot(false,
      Power.TAKE_2_GEMS_SAME_COL_AND_TAKE_1_GEM_DIF_COL,
      new CardCost(2, 0, 0, 0, 0)));
    tradingPostSlots.add(new TradingPostSlot(false, Power.GOLD_TOKENS_WORTH_2_GEMS_SAME_COL,
      new CardCost(0, 3,  0, 0, 1)));
    tradingPostSlots.add(new TradingPostSlot(true, Power.GAIN_5_PRESTIGE,
      new CardCost(0, 0, 5, 0, 0)));
    tradingPostSlots.add(new TradingPostSlot(false,
      Power.GAIN_1_PRESTIGE_FOR_EVERY_PLACED_COAT_OF_ARMS,
      new CardCost(0, 0, 0, 0, 3)));
    assertEquals(tradingPostSlots.get(0).getPower(),tradingSlot.getTradingPostSlots().get(0).getPower(),"");
    assertEquals(tradingPostSlots.get(1).getPower(),tradingSlot.getTradingPostSlots().get(1).getPower(),"");
    assertEquals(tradingPostSlots.get(2).getPower(),tradingSlot.getTradingPostSlots().get(2).getPower(),"");
    assertEquals(tradingPostSlots.get(3).getPower(),tradingSlot.getTradingPostSlots().get(3).getPower(),"");
  }
}