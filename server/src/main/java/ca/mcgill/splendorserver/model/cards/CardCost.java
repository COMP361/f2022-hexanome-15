package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.tokens.TokenType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable container holding the costs associated with one card.
 *
 * @author Zachary Hayden
 */
public class CardCost {
  private final Map<TokenType, Integer> costMap;

  /**
   * Immutable container of the costs associated with a given card.
   *
   * @param diamondCost  cost in diamond tokens
   * @param sapphireCost cost in sapphire
   * @param emeraldCost  cost in emeralds
   * @param rubyCost     cost in rubies
   * @param onyxCost     cost in onyx
   */
  public CardCost(int diamondCost, int sapphireCost, int emeraldCost, int rubyCost, int onyxCost) {
    assert diamondCost >= 0 && sapphireCost >= 0 && emeraldCost >= 0 && rubyCost >= 0
        && onyxCost >= 0 : "Cannot have negative costs associated with a card";
    costMap = new HashMap<>(5);
    costMap.put(TokenType.DIAMOND, diamondCost);
    costMap.put(TokenType.SAPPHIRE, sapphireCost);
    costMap.put(TokenType.EMERALD, emeraldCost);
    costMap.put(TokenType.RUBY, rubyCost);
    costMap.put(TokenType.ONYX, onyxCost);
  }

  /**
   * Retrieves the cost in terms of the passed token type.
   *
   * @param tokenType the token type of which to find the price of with this card. can't be null
   * @return the cost for the given type.
   */
  public int costByTokenType(TokenType tokenType) {
    assert tokenType != null;
    return costMap.get(tokenType);
  }

  /**
   * Returns the cost map.
   *
   * @return the cost map
   */
  public Set<Map.Entry<TokenType, Integer>> entrySet() {
    return costMap.entrySet();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CardCost cardCost)) return false;
    return Objects.equals(costMap, cardCost.costMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(costMap);
  }
}
