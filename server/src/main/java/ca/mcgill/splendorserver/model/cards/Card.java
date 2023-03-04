package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//TODO: Make cards flyweights

/**
 * Represents a Splendor Card with id, prestige, tokenBonus, deckType, discount
 * and cost This class implements the Flyweight design pattern.
 */
public class Card implements Comparable<Card> {
  private static final List<Card> cards = new ArrayList<>();
  // this eventually will be the png that is on the actual card
  private final        int             id;
  private final        int             prestige;
  private TokenType tokenBonusType;
  private TokenBonusAmount tokenBonusAmount;
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
    assert prestige >= 0 && deckType != null && tokenBonusAmount != null && cardCost != null;
    this.id               = id;
    this.prestige         = prestige;
    this.tokenBonusType   = tokenBonusType;
    this.deckType         = deckType;
    this.tokenBonusAmount = tokenBonusAmount;
    this.cardCost         = cardCost;
  }

  /**
   * Checks if the card is reserved.
   *
   * @return a boolean determining if the card is reserved
   */
  public boolean isReserved() {
    return cardStatus == CardStatus.RESERVED;
  }

  /**
   * Checks if the card is purchased.
   *
   * @return a boolean determining if the card is purchased
   */
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
      case ORIENT1:
        toSend = new ArrayList<>(cards.subList(90, 100));
        break;
      case ORIENT2:
        toSend = new ArrayList<>(cards.subList(100, 110));
        break;
      case ORIENT3:
        toSend = new ArrayList<>(cards.subList(110, 120));
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
    } else if (tokenBonusAmount  == TokenBonusAmount.TWO) {
      return 2;
    } else {
      return 0;
    }
  }

  /**
   * Sets the token bonus amount to the given amount.
   * Only used when a spice bag card is paired with a card in the user inventory.
   *
   * @param tokenBonusAmount the amount to be set
   */
  public void setBonusAmount(TokenBonusAmount tokenBonusAmount) {
    assert tokenBonusAmount != null;
    this.tokenBonusAmount = tokenBonusAmount;
  }

  /**
   * Sets the token bonus type to the given type.
   * Only used when a spice bag card is paired with a card in the user inventory.
   *
   * @param type the token type to be set
   */
  public void setBonusType(TokenType type) {
    assert type != null;
    this.tokenBonusType = type;
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
    cards.add(new Card(0, 0, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(1, 1, 1, 1, 0)));
    cards.add(new Card(1, 0, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(1, 2, 1, 1, 0)));
    cards.add(new Card(2, 0, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(2, 2, 0, 1, 0)));
    cards.add(new Card(3, 0, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 0, 1, 3, 1)));
    cards.add(new Card(4, 0, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 0, 2, 1, 0)));
    cards.add(new Card(5, 0, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 0, 3, 0, 0)));
    cards.add(new Card(6, 1, TokenType.ONYX, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 4, 0, 0, 0)));

    cards.add(new Card(7, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(1, 0, 1, 1, 1)));
    cards.add(new Card(8, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(1, 0, 1, 2, 1)));
    cards.add(new Card(9, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(1, 0, 2, 2, 0)));
    cards.add(new Card(10, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 1, 3, 1, 0)));
    cards.add(new Card(11, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(1, 0, 0, 0, 2)));
    cards.add(new Card(12, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 0, 2, 0, 2)));
    cards.add(new Card(13, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 1, 3, 1, 0)));
    cards.add(new Card(14, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 0, 3)));
    cards.add(new Card(15, 1, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 4, 0)));

    cards.add(new Card(16, 0, TokenType.DIAMOND, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 1, 1, 1, 1)));
    cards.add(new Card(17, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 1, 2, 1, 1)));
    cards.add(new Card(18, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 2, 2, 0, 1)));
    cards.add(new Card(19, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(3, 1, 0, 0, 1)));
    cards.add(new Card(20, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 2, 1)));
    cards.add(new Card(21, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 2, 0, 0, 2)));
    cards.add(new Card(22, 0, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 3, 0, 0, 0)));
    cards.add(new Card(23, 1, TokenType.SAPPHIRE, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 0, 4, 0, 0)));

    cards.add(new Card(24, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(1, 1, 0, 1, 1)));
    cards.add(new Card(25, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(1, 1, 0, 1, 2)));
    cards.add(new Card(26, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 1, 0, 2, 2)));
    cards.add(new Card(27, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(1, 3, 1, 0, 0)));
    cards.add(new Card(28, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(2, 1, 0, 0, 0)));
    cards.add(new Card(29, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 2, 0, 2, 0)));
    cards.add(new Card(30, 0, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 3, 0)));
    cards.add(new Card(31, 1, TokenType.EMERALD, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 0, 4)));

    cards.add(new Card(32, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(1, 1, 1, 0, 1)));
    cards.add(new Card(33, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(2, 1, 1, 0, 1)));
    cards.add(new Card(34, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(2, 0, 1, 0, 2)));
    cards.add(new Card(35, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(1, 0, 0, 1, 3)));
    cards.add(new Card(36, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(0, 2, 1, 0, 0)));
    cards.add(new Card(37, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(2, 0, 0, 2, 0)));
    cards.add(new Card(38, 0, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(3, 0, 0, 0, 0)));
    cards.add(new Card(39, 1, TokenType.RUBY, DeckType.BASE1, TokenBonusAmount.ONE,
        new CardCost(4, 0, 0, 0, 0)));

    cards.add(new Card(40, 1, TokenType.ONYX, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(3, 2, 2, 0, 0)));
    cards.add(new Card(41, 1, TokenType.ONYX, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(3, 0, 3, 0, 2)));
    cards.add(new Card(42, 2, TokenType.ONYX, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 1, 4, 2, 0)));
    cards.add(new Card(43, 2, TokenType.ONYX, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 0, 5, 3, 0)));
    cards.add(new Card(44, 2, TokenType.ONYX, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(5, 0, 0, 0, 0)));
    cards.add(new Card(45, 3, TokenType.ONYX, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 0, 6)));

    cards.add(new Card(46, 1, TokenType.SAPPHIRE, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 2, 2, 3, 0)));
    cards.add(new Card(47, 1, TokenType.SAPPHIRE, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 2, 3, 0, 3)));
    cards.add(new Card(48, 2, TokenType.SAPPHIRE, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(5, 3, 0, 0, 0)));
    cards.add(new Card(49, 2, TokenType.SAPPHIRE, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(2, 0, 0, 1, 4)));
    cards.add(new Card(50, 2, TokenType.SAPPHIRE, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 5, 0, 0, 0)));
    cards.add(new Card(51, 3, TokenType.SAPPHIRE, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 6, 0, 0, 0)));

    cards.add(new Card(52, 1, TokenType.DIAMOND, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 0, 3, 2, 2)));
    cards.add(new Card(53, 1, TokenType.DIAMOND, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(2, 3, 0, 3, 0)));
    cards.add(new Card(54, 2, TokenType.DIAMOND, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 0, 1, 4, 2)));
    cards.add(new Card(55, 2, TokenType.DIAMOND, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 5, 3)));
    cards.add(new Card(56, 2, TokenType.DIAMOND, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 5, 0)));
    cards.add(new Card(57, 3, TokenType.DIAMOND, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(6, 0, 0, 0, 0)));

    cards.add(new Card(58, 1, TokenType.EMERALD, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(3, 0, 2, 3, 0)));
    cards.add(new Card(59, 1, TokenType.EMERALD, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(2, 3, 0, 0, 2)));
    cards.add(new Card(60, 2, TokenType.EMERALD, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(4, 2, 0, 0, 1)));
    cards.add(new Card(61, 2, TokenType.EMERALD, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 5, 3, 0, 0)));
    cards.add(new Card(62, 2, TokenType.EMERALD, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 0, 5, 0, 0)));
    cards.add(new Card(63, 3, TokenType.EMERALD, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 0, 6, 0, 0)));

    cards.add(new Card(64, 1, TokenType.RUBY, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(2, 0, 0, 2, 3)));
    cards.add(new Card(65, 1, TokenType.RUBY, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 3, 0, 2, 3)));
    cards.add(new Card(66, 2, TokenType.RUBY, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(1, 4, 2, 0, 0)));
    cards.add(new Card(67, 2, TokenType.RUBY, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(3, 0, 0, 0, 5)));
    cards.add(new Card(68, 2, TokenType.RUBY, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 0, 5)));
    cards.add(new Card(69, 3, TokenType.RUBY, DeckType.BASE2, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 6, 0)));

    cards.add(new Card(70, 3, TokenType.ONYX, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(3, 3, 5, 3, 0)));
    cards.add(new Card(71, 4, TokenType.ONYX, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 7, 0)));
    cards.add(new Card(72, 4, TokenType.ONYX, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(0, 0, 3, 6, 3)));
    cards.add(new Card(73, 5, TokenType.ONYX, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 7, 3)));

    cards.add(new Card(74, 3, TokenType.SAPPHIRE, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(3, 0, 3, 3, 5)));
    cards.add(new Card(75, 4, TokenType.SAPPHIRE, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(7, 0, 0, 0, 0)));
    cards.add(new Card(76, 4, TokenType.SAPPHIRE, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(6, 3, 0, 0, 3)));
    cards.add(new Card(77, 5, TokenType.SAPPHIRE, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(7, 3, 0, 0, 0)));

    cards.add(new Card(78, 3, TokenType.DIAMOND, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(0, 3, 3, 5, 3)));
    cards.add(new Card(79, 4, TokenType.DIAMOND, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 0, 7)));
    cards.add(new Card(80, 4, TokenType.DIAMOND, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(3, 0, 0, 3, 6)));
    cards.add(new Card(81, 5, TokenType.DIAMOND, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(3, 0, 0, 0, 7)));

    cards.add(new Card(82, 3, TokenType.EMERALD, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(5, 3, 0, 3, 3)));
    cards.add(new Card(83, 4, TokenType.EMERALD, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(0, 7, 0, 0, 0)));
    cards.add(new Card(84, 4, TokenType.EMERALD, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(3, 6, 3, 0, 0)));
    cards.add(new Card(85, 5, TokenType.EMERALD, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(0, 7, 3, 0, 0)));

    cards.add(new Card(86, 3, TokenType.RUBY, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(3, 5, 3, 0, 3)));
    cards.add(new Card(87, 4, TokenType.RUBY, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(0, 0, 7, 0, 0)));
    cards.add(new Card(88, 4, TokenType.RUBY, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(0, 3, 6, 3, 0)));
    cards.add(new Card(89, 5, TokenType.RUBY, DeckType.BASE3, TokenBonusAmount.ONE,
        new CardCost(0, 0, 7, 3, 0)));

    cards.add(new OrientCard(90, 0, TokenType.GOLD, DeckType.ORIENT1, TokenBonusAmount.TWO,
        new CardCost(0, 0, 3, 0, 0), false,
        new ArrayList<>()));
    cards.add(new OrientCard(91, 0, TokenType.GOLD, DeckType.ORIENT1, TokenBonusAmount.TWO,
        new CardCost(0, 3, 0, 0, 0), false,
        new ArrayList<>()));
    cards.add(new OrientCard(92, 0, TokenType.GOLD, DeckType.ORIENT1, TokenBonusAmount.TWO,
        new CardCost(3, 0, 0, 0, 0), false,
        new ArrayList<>()));
    cards.add(new OrientCard(93, 0, TokenType.GOLD, DeckType.ORIENT1, TokenBonusAmount.TWO,
        new CardCost(0, 0, 0, 0, 3), false,
        new ArrayList<>()));
    cards.add(new OrientCard(94, 0, TokenType.GOLD, DeckType.ORIENT1, TokenBonusAmount.TWO,
        new CardCost(0, 0, 0, 3, 0), false,
        new ArrayList<>()));
    cards.add(new OrientCard(95, 0, null, DeckType.ORIENT1, TokenBonusAmount.ZERO,
        new CardCost(0, 3, 2, 0, 0), true,
        new ArrayList<>(List.of(Action.PAIR_SPICE_CARD))));
    cards.add(new OrientCard(96, 0, null, DeckType.ORIENT1, TokenBonusAmount.ZERO,
        new CardCost(0, 0, 3, 0, 2), true,
        new ArrayList<>(List.of(Action.PAIR_SPICE_CARD))));
    cards.add(new OrientCard(97, 0, null, DeckType.ORIENT1, TokenBonusAmount.ZERO,
        new CardCost(2, 0, 0, 0, 3), true,
        new ArrayList<>(List.of(Action.PAIR_SPICE_CARD))));
    cards.add(new OrientCard(98, 0, null, DeckType.ORIENT1, TokenBonusAmount.ZERO,
        new CardCost(0, 2, 0, 3, 0), true,
        new ArrayList<>(List.of(Action.PAIR_SPICE_CARD))));
    cards.add(new OrientCard(99, 0, null, DeckType.ORIENT1, TokenBonusAmount.ZERO,
        new CardCost(3, 0, 0, 2, 0), true,
        new ArrayList<>(List.of(Action.PAIR_SPICE_CARD))));

    cards.add(new OrientCard(100, 1, TokenType.DIAMOND, DeckType.ORIENT2, TokenBonusAmount.ONE,
        new CardCost(0, 2, 2, 2, 2), false,
        new ArrayList<>(List.of(Action.RESERVE_NOBLE))));
    cards.add(new OrientCard(101, 1, TokenType.EMERALD, DeckType.ORIENT2, TokenBonusAmount.ONE,
        new CardCost(2, 2, 0, 2, 2), false,
        new ArrayList<>(List.of(Action.RESERVE_NOBLE))));
    cards.add(new OrientCard(102, 1, TokenType.RUBY, DeckType.ORIENT2, TokenBonusAmount.ONE,
        new CardCost(2, 2, 2, 0, 2), false,
        new ArrayList<>(List.of(Action.RESERVE_NOBLE))));
    cards.add(new OrientCard(103, 0, null, DeckType.ORIENT2, TokenBonusAmount.ZERO,
        new CardCost(0, 0, 4, 3, 1), true,
        new ArrayList<>(List.of(Action.PAIR_SPICE_CARD, Action.CASCADE_LEVEL_1))));
    cards.add(new OrientCard(104, 0, null, DeckType.ORIENT2, TokenBonusAmount.ZERO,
        new CardCost(4, 3, 0, 0, 1), true,
        new ArrayList<>(List.of(Action.PAIR_SPICE_CARD, Action.CASCADE_LEVEL_1))));
    cards.add(new OrientCard(105, 0, TokenType.DIAMOND, DeckType.ORIENT2, TokenBonusAmount.TWO,
        new CardCost(0, 4, 3, 0, 0), false,
        new ArrayList<>()));
    cards.add(new OrientCard(106, 0, TokenType.SAPPHIRE, DeckType.ORIENT2, TokenBonusAmount.TWO,
        new CardCost(3, 0, 0, 0, 4), false,
        new ArrayList<>()));
    cards.add(new OrientCard(107, 0, TokenType.EMERALD, DeckType.ORIENT2, TokenBonusAmount.TWO,
        new CardCost(4, 0, 0, 3, 0), false,
        new ArrayList<>()));
    cards.add(new OrientCard(108, 0, TokenType.RUBY, DeckType.ORIENT2, TokenBonusAmount.TWO,
        new CardCost(0, 0, 4, 0, 3), false,
        new ArrayList<>()));
    cards.add(new OrientCard(109, 0, TokenType.ONYX, DeckType.ORIENT2, TokenBonusAmount.TWO,
        new CardCost(0, 3, 0, 4, 0), false,
        new ArrayList<>()));

    cards.add(new OrientCard(110, 0, TokenType.DIAMOND, DeckType.ORIENT3, TokenBonusAmount.ONE,
        new CardCost(1, 6, 3, 0, 0), false,
        new ArrayList<>(List.of(Action.CASCADE_LEVEL_2))));
    cards.add(new OrientCard(111, 0, TokenType.SAPPHIRE, DeckType.ORIENT3, TokenBonusAmount.ONE,
        new CardCost(0, 1, 6, 3, 0), false,
        new ArrayList<>(List.of(Action.CASCADE_LEVEL_2))));
    cards.add(new OrientCard(112, 0, TokenType.EMERALD, DeckType.ORIENT3, TokenBonusAmount.ONE,
        new CardCost(0, 0, 1, 6, 3), false,
        new ArrayList<>(List.of(Action.CASCADE_LEVEL_2))));
    cards.add(new OrientCard(113, 0, TokenType.RUBY, DeckType.ORIENT3, TokenBonusAmount.ONE,
        new CardCost(3, 0, 0, 1, 6), false,
        new ArrayList<>(List.of(Action.CASCADE_LEVEL_2))));
    cards.add(new OrientCard(114, 0, TokenType.ONYX, DeckType.ORIENT3, TokenBonusAmount.ONE,
        new CardCost(6, 3, 0, 0, 1), false,
        new ArrayList<>(List.of(Action.CASCADE_LEVEL_2))));
    cards.add(new OrientCard(115, 3, TokenType.DIAMOND, DeckType.ORIENT3, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 0, 0), false,
        new ArrayList<>(List.of(Action.DISCARD_2_BLACK_CARDS))));
    cards.add(new OrientCard(116, 3, TokenType.SAPPHIRE, DeckType.ORIENT3, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 0, 0), false,
        new ArrayList<>(List.of(Action.DISCARD_2_WHITE_CARDS))));
    cards.add(new OrientCard(117, 3, TokenType.EMERALD, DeckType.ORIENT3, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 0, 0), false,
        new ArrayList<>(List.of(Action.DISCARD_2_BLUE_CARDS))));
    cards.add(new OrientCard(118, 3, TokenType.RUBY, DeckType.ORIENT3, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 0, 0), false,
        new ArrayList<>(List.of(Action.DISCARD_2_GREEN_CARDS))));
    cards.add(new OrientCard(119, 3, TokenType.ONYX, DeckType.ORIENT3, TokenBonusAmount.ONE,
        new CardCost(0, 0, 0, 0, 0), false,
        new ArrayList<>(List.of(Action.DISCARD_2_RED_CARDS))));

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

  /**
   * Returns the cost of this card.
   *
   * @return the cost of this card
   */
  public CardCost getCardCost() {
    return cardCost;
  }

  /**
   * Returns the status of this card.
   *
   * @return the status of this card
   */
  public CardStatus getCardStatus() {
    return cardStatus;
  }

  /**
   * Sets the status of this card to the given status.
   *
   * @param cardStatus the given status
   */
  public void setCardStatus(CardStatus cardStatus) {
    assert cardStatus != null;
    this.cardStatus = cardStatus;
  }

  @Override
  public String toString() {
    return "Card{"
             + "prestige=" + prestige
             + ", tokenBonus=" + tokenBonusType
             + ", deckType=" + deckType
             + ", discount=" + tokenBonusAmount
             + ", cardCost=" + cardCost
             + '}';
  }

}
