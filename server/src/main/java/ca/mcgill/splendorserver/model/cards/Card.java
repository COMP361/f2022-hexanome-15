package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.tokens.TokenType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Splendor Card with id, prestige, tokenBonus, deckType, discount
 * and cost This class implements the Flyweight design pattern.
 */
public class Card implements Comparable<Card> {
  private static final List<Card> cards = new ArrayList<>();
  // this eventually will be the png that is on the actual card
  private final        int             id;
  private final        int             prestige;
  private final        TokenType       tokenBonusType;
  private final TokenBonusAmount tokenBonusAmount;
  private final        DeckType        deckType;
  private final        CardCost        cardCost;
  private              CardStatus      cardStatus;

  /**
   * Creates a card.
   *
   * @param id               The id of the card
   * @param prestige         The prestige earned upon purchasing the card
   * @param tokenBonusType   The bonus received upon purchasing the card
   * @param deckType         The type of card
   * @param tokenBonusAmount The amount of bonus gems a card is worth
   * @param cardCost         The cost of the card
   */
  public Card(int id, int prestige, TokenType tokenBonusType, DeckType deckType,
              TokenBonusAmount tokenBonusAmount, CardCost cardCost
  ) {
    assert prestige >=0 && tokenBonusType != null && deckType != null && tokenBonusAmount != null && cardCost != null;
    this.id               = id;
    this.prestige         = prestige;
    this.tokenBonusType   = tokenBonusType;
    this.deckType         = deckType;
    this.tokenBonusAmount = tokenBonusAmount;
    this.cardCost         = cardCost;
  }

  public boolean isReserved() {
    return cardStatus == CardStatus.RESERVED;
  }

  public boolean isPurchased() {
    return cardStatus == CardStatus.PURCHASED;
  }

  /**
   * Gets a card based on their id.
   *
   * @param id The id of the card
   * @return the requested card
   */
  public Card getCard(int id) {
    return cards.get(id);
  }

  /**
   * Creates a deck made of cards of a certain type of cards.
   *
   * @param type The type of Card and Deck
   * @return the list of cards in the deck
   */
  public static List<Card> makeDeck(DeckType type) {
    assert type != null;
    if (cards.size() == 0) {
      generateCards();
    }
    List<Card> toSend;
    switch (type) {
      case BASE1:
        toSend = new ArrayList<>(cards.subList(0, 40));
        break;
      case BASE2:
        toSend = new ArrayList<>(cards.subList(40, 70));
        break;
      case BASE3:
        toSend = new ArrayList<>(cards.subList(70, 90));
        break;
      default:
        return null;
    }
    Collections.shuffle(toSend);
    return toSend;
  }

  /**
   * Returns the id of this card.
   *
   * @return the id of this card
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the prestige of this card.
   *
   * @return the prestige of this card
   */
  public int getPrestige() {
    return prestige;
  }

  /**
   * Returns the discount of this card.
   *
   * @return the discount of this card
   */
  public int getTokenBonusAmount() {
    if (tokenBonusAmount == TokenBonusAmount.ONE) {
      return 1;
    }
    else {
      return 2;
    }
  }

  /**
   * Returns the cost of this card.
   *
   * @return the cost of this card
   */
  public CardCost getCost() {
    return cardCost;
  }

  /**
   * Returns the type of this card.
   *
   * @return the type of this card
   */
  public DeckType getDeckType() {
    return deckType;
  }

  /**
   * Returns the token bonus type of this card.
   *
   * @return the token bonus type of this card
   */
  public TokenType getTokenBonusType() {
    return tokenBonusType;
  }

  /**
   * Generates cards for the decks.
   */
  private static void generateCards() {
    cards.add(new Card(0, 0, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(1, 1, 1, 1, 0)));
    cards.add(new Card(1, 0, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(1, 2, 1, 1, 0)));
    cards.add(new Card(2, 0, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(2, 2, 0, 1, 0)));
    cards.add(new Card(3, 0, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 0, 1, 3, 1)));
    cards.add(new Card(4, 0, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 0, 2, 1, 0)));
    cards.add(new Card(5, 0, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 0, 3, 0, 0)));
    cards.add(new Card(6, 1, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 4, 0, 0, 0)));

    cards.add(new Card(7, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(1, 0, 1, 1, 1)));
    cards.add(new Card(8, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(1, 0, 1, 2, 1)));
    cards.add(new Card(9, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(1, 0, 2, 2, 0)));
    cards.add(new Card(10, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 1, 3, 1, 0)));
    cards.add(new Card(11, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(1, 0, 0, 0, 2)));
    cards.add(new Card(12, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 0, 2, 0, 2)));
    cards.add(new Card(13, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 1, 3, 1, 0)));
    cards.add(new Card(14, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 0, 3)));
    cards.add(new Card(15, 1, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 4, 0)));

    cards.add(new Card(16, 0, TokenType.DIAMOND, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 1, 1, 1, 1)));
    cards.add(new Card(17, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 1, 2, 1, 1)));
    cards.add(new Card(18, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 2, 2, 0, 1)));
    cards.add(new Card(19, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(3, 1, 0, 0, 1)));
    cards.add(new Card(20, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 2, 1)));
    cards.add(new Card(21, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 2, 0, 0, 2)));
    cards.add(new Card(22, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 3, 0, 0, 0)));
    cards.add(new Card(23, 1, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 0, 4, 0, 0)));

    cards.add(new Card(24, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(1, 1, 0, 1, 1)));
    cards.add(new Card(25, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(1, 1, 0, 1, 2)));
    cards.add(new Card(26, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 1, 0, 2, 2)));
    cards.add(new Card(27, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(1, 3, 1, 0, 0)));
    cards.add(new Card(28, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(2, 1, 0, 0, 0)));
    cards.add(new Card(29, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 2, 0, 2, 0)));
    cards.add(new Card(30, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 3, 0)));
    cards.add(new Card(31, 1, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 0, 4)));

    cards.add(new Card(32, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(1, 1, 1, 0, 1)));
    cards.add(new Card(33, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(2, 1, 1, 0, 1)));
    cards.add(new Card(34, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(2, 0, 1, 0, 2)));
    cards.add(new Card(35, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(1, 0, 0, 1, 3)));
    cards.add(new Card(36, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(0, 2, 1, 0, 0)));
    cards.add(new Card(37, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(2, 0, 0, 2, 0)));
    cards.add(new Card(38, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(3, 0, 0, 0, 0)));
    cards.add(new Card(39, 1, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE, new CardCost(4, 0, 0, 0, 0)));

    cards.add(new Card(40, 1, TokenType.ONYX, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(3, 2, 2, 0, 0)));
    cards.add(new Card(41, 1, TokenType.ONYX, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(3, 0, 3, 0, 2)));
    cards.add(new Card(42, 2, TokenType.ONYX, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 1, 4, 2, 0)));
    cards.add(new Card(43, 2, TokenType.ONYX, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 0, 5, 3, 0)));
    cards.add(new Card(44, 2, TokenType.ONYX, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(5, 0, 0, 0, 0)));
    cards.add(new Card(45, 3, TokenType.ONYX, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 0, 6)));

    cards.add(new Card(46, 1, TokenType.SAPPHIRE, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 2, 2, 3, 0)));
    cards.add(new Card(47, 1, TokenType.SAPPHIRE, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 2, 3, 0, 3)));
    cards.add(new Card(48, 2, TokenType.SAPPHIRE, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(5, 3, 0, 0, 0)));
    cards.add(new Card(49, 2, TokenType.SAPPHIRE, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(2, 0, 0, 1, 4)));
    cards.add(new Card(50, 2, TokenType.SAPPHIRE, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 5, 0, 0, 0)));
    cards.add(new Card(51, 3, TokenType.SAPPHIRE, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 6, 0, 0, 0)));

    cards.add(new Card(52, 1, TokenType.DIAMOND, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 0, 3, 2, 2)));
    cards.add(new Card(53, 1, TokenType.DIAMOND, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(2, 3, 0, 3, 0)));
    cards.add(new Card(54, 2, TokenType.DIAMOND, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 0, 1, 4, 2)));
    cards.add(new Card(55, 2, TokenType.DIAMOND, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 5, 3)));
    cards.add(new Card(56, 2, TokenType.DIAMOND, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 5, 0)));
    cards.add(new Card(57, 3, TokenType.DIAMOND, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(6, 0, 0, 0, 0)));

    cards.add(new Card(58, 1, TokenType.EMERALD, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(3, 0, 2, 3, 0)));
    cards.add(new Card(59, 1, TokenType.EMERALD, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(2, 3, 0, 0, 2)));
    cards.add(new Card(60, 2, TokenType.EMERALD, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(4, 2, 0, 0, 1)));
    cards.add(new Card(61, 2, TokenType.EMERALD, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 5, 3, 0, 0)));
    cards.add(new Card(62, 2, TokenType.EMERALD, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 0, 5, 0, 0)));
    cards.add(new Card(63, 3, TokenType.EMERALD, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 0, 6, 0, 0)));

    cards.add(new Card(64, 1, TokenType.RUBY, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(2, 0, 0, 2, 3)));
    cards.add(new Card(65, 1, TokenType.RUBY, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 3, 0, 2, 3)));
    cards.add(new Card(66, 2, TokenType.RUBY, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(1, 4, 2, 0, 0)));
    cards.add(new Card(67, 2, TokenType.RUBY, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(3, 0, 0, 0, 5)));
    cards.add(new Card(68, 2, TokenType.RUBY, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 0, 5)));
    cards.add(new Card(69, 3, TokenType.RUBY, DeckType.BASE2, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 6, 0)));

    cards.add(new Card(70, 3, TokenType.ONYX, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(3, 3, 5, 3, 0)));
    cards.add(new Card(71, 4, TokenType.ONYX, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 7, 0)));
    cards.add(new Card(72, 4, TokenType.ONYX, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(0, 0, 3, 6, 3)));
    cards.add(new Card(73, 5, TokenType.ONYX, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 7, 3)));

    cards.add(new Card(74, 3, TokenType.SAPPHIRE, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(3, 0, 3, 3, 5)));
    cards.add(new Card(75, 4, TokenType.SAPPHIRE, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(7, 0, 0, 0, 0)));
    cards.add(new Card(76, 4, TokenType.SAPPHIRE, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(6, 3, 0, 0, 3)));
    cards.add(new Card(77, 5, TokenType.SAPPHIRE, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(7, 3, 0, 0, 0)));

    cards.add(new Card(78, 3, TokenType.DIAMOND, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(0, 3, 3, 5, 3)));
    cards.add(new Card(79, 4, TokenType.DIAMOND, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(0, 0, 0, 0, 7)));
    cards.add(new Card(80, 4, TokenType.DIAMOND, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(3, 0, 0, 3, 6)));
    cards.add(new Card(81, 5, TokenType.DIAMOND, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(3, 0, 0, 0, 7)));

    cards.add(new Card(82, 3, TokenType.EMERALD, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(5, 3, 0, 3, 3)));
    cards.add(new Card(83, 4, TokenType.EMERALD, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(0, 7, 0, 0, 0)));
    cards.add(new Card(84, 4, TokenType.EMERALD, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(3, 6, 3, 0, 0)));
    cards.add(new Card(85, 5, TokenType.EMERALD, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(0, 7, 3, 0, 0)));

    cards.add(new Card(86, 3, TokenType.RUBY, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(3, 5, 3, 0, 3)));
    cards.add(new Card(87, 4, TokenType.RUBY, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(0, 0, 7, 0, 0)));
    cards.add(new Card(88, 4, TokenType.RUBY, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(0, 3, 6, 3, 0)));
    cards.add(new Card(89, 5, TokenType.RUBY, DeckType.BASE3, TokenBonusAmount.ONE, new CardCost(0, 0, 7, 3, 0)));

  }

  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof Card card)) {
      return false;
    }
    return id == card.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public int compareTo(Card card) {
    assert card != null;
    return this.getTokenBonusType()
               .compareTo(card.getTokenBonusType());
  }

  public CardCost getCardCost() {
    return cardCost;
  }

  public CardStatus getCardStatus() {
    return cardStatus;
  }

  public void setCardStatus(CardStatus cardStatus) {
    this.cardStatus = cardStatus;
  }

  @Override
  public String toString() {
    return "Card{" +
        "prestige=" + prestige +
        ", tokenBonus=" + tokenBonusType +
        ", deckType=" + deckType +
        ", discount=" + tokenBonusAmount +
        ", cardCost=" + cardCost +
        '}';
  }

  // TODO: need things like cost, associated gem discount, and prestige.
  // Robillard might want these to be flyweights.

}
