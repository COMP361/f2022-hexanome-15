package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.tokens.TokenType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Zachary Hayden
 * Date: 2/4/23
 */
public class CardCost {
  private final Map<TokenType, Integer> costMap;

  public CardCost(int diomondCost, int sapphireCost, int emeraldCost, int rubyCost, int onyxCost) {
    costMap = new HashMap<>(5);
    costMap.put(TokenType.DIAMOND, diomondCost);
    costMap.put(TokenType.SAPPHIRE, sapphireCost);
    costMap.put(TokenType.EMERALD, emeraldCost);
    costMap.put(TokenType.RUBY, rubyCost);
    costMap.put(TokenType.ONYX, onyxCost);
  }

  public int costByTokenType(TokenType tokenType) {
    assert tokenType != null;
    return costMap.get(tokenType);
  }

  @Override
  public String toString() {
    return "CardCost{" + costMap + '}';
  }

  public Set<Map.Entry<TokenType, Integer>> entrySet() {
    return costMap.entrySet();
  }
}
