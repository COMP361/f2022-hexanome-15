package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import java.util.Optional;

/**
 * Represents an Orient expansion development card in Splendor.
 * Extends the Card class but has additional fields of spiceBag and bonusAction.
 */
public class OrientCard extends Card {

  private final boolean spiceBag;
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
  public OrientCard(int id, int prestige, TokenType tokenBonusType, DeckType deckType,
                    TokenBonusAmount tokenBonusAmount,
                    CardCost cardCost, Action bonusAction, boolean spiceBag) {
    super(id, prestige, tokenBonusType, deckType, tokenBonusAmount, cardCost);
    assert bonusAction != null;
    this.bonusAction = Optional.ofNullable(bonusAction);
    this.spiceBag = spiceBag;
  }

  /**
   * Pairs the given spice bag Orient card with a card in the user inventory.
   * The spice bag card gains a bonus with the same type as the paired card.
   *
   * @param card The card to be paired with the spice bag card
   */
  public void pairWithCard(Card card) {
    assert this.spiceBag;
    setBonusAmount(TokenBonusAmount.ONE);
    setBonusType(card.getTokenBonusType());
  }
}
