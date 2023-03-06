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
  Map<TokenType, Integer> costMap = new HashMap<>();
  CardCost cardCost = new CardCost(1,2,3,4,5);

  @Test
  void testCostByTokenType() {
    assertEquals(1, cardCost.costByTokenType(DIAMOND));

  }

  @Test
  void testToString() {
  }

  @Test
  void entrySet() {
  }
}