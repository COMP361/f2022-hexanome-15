package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.tokens.TokenType;

import java.util.Optional;

public class OrientCard extends Card {

  private final Optional<Action> bonusAction;
  /**
   * Creates a card.
   *
   * @param id               The id of the card
   * @param prestige         The prestige earned upon purchasing the card
   * @param tokenBonusType   The bonus received upon purchasing the card
   * @param deckType         The type of card
   * @param tokenBonusAmount The amount of bonus gems a card is worth
   * @param cardCost         The cost of the card
   * @param bonusAction      The bonus action that may occur when this Orient card is purchased
   */
  public OrientCard(int id, int prestige, TokenType tokenBonusType, DeckType deckType, TokenBonusAmount tokenBonusAmount, CardCost cardCost, Action bonusAction) {
    super(id, prestige, tokenBonusType, deckType, tokenBonusAmount, cardCost);
    assert bonusAction != null;
    this.bonusAction = Optional.ofNullable(bonusAction);
  }
}
