package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.tokens.TokenType;
import org.junit.jupiter.api.Test;

import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static org.junit.jupiter.api.Assertions.*;
import ca.mcgill.splendorserver.model.cards.CardCost;

import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;

class CardCostTest {
  private CardCost cardCost = new CardCost(1,2,3,4,5);

  @Test
  void testCostByTokenType() {
    assertEquals(1, cardCost.costByTokenType(DIAMOND));
  }

  @Test
  void testEntrySet() {
    Map<TokenType, Integer> costMap = new HashMap<>(5);
    costMap.put(TokenType.DIAMOND, 1);
    costMap.put(TokenType.SAPPHIRE, 2);
    costMap.put(TokenType.EMERALD, 3);
    costMap.put(TokenType.RUBY, 4);
    costMap.put(TokenType.ONYX, 5);
    assertEquals(costMap.entrySet(), cardCost.entrySet());
  }

  @Test
  void testEqualsNotCardCost() {
    assertFalse(cardCost.equals(null));
  }
}