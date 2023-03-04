package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import java.util.List;

/**
 * Represents an Orient expansion development card in Splendor.
 * Extends the Card class but has additional fields of spiceBag and bonusAction.
 */
public class OrientCard extends Card {

  private final boolean spiceBag;
  private final List<Action> bonusActions;

  /**
   * Creates a card.
   *
   * @param id               The id of the card
   * @param prestige         The prestige earned upon purchasing the card
   * @param tokenBonusType   The bonus received upon purchasing the card
   * @param deckType         The type of card
   * @param tokenBonusAmount The amount of bonus gems a card is worth
   * @param cardCost         The cost of the card
   * @param bonusActions     The bonus actions that may occur when this Orient card is purchased
   * @param spiceBag         Whether a card is a spice bag card or not
   */
  public OrientCard(int id, int prestige, TokenType tokenBonusType, DeckType deckType,
                    TokenBonusAmount tokenBonusAmount,
                    CardCost cardCost, boolean spiceBag,
                    List<Action> bonusActions) {
    super(id, prestige, tokenBonusType, deckType, tokenBonusAmount, cardCost);
    assert bonusActions != null;
    this.bonusActions = bonusActions;
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

  /**
   * Checks if this Orient card is a spice bag card.
   *
   * @return a boolean determining if this Orient card is a spice bag card
   */
  public boolean isSpiceBag() {
    return this.spiceBag;
  }

  /**
   * Returns the list of bonus actions provided by the Orient card.
   *
   * @return bonusActions The list of bonus actions provided by the Orient card
   */
  public List<Action> getBonusActions() {
    return bonusActions;
  }
}

