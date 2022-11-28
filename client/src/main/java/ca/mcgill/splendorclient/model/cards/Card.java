package ca.mcgill.splendorclient.model.cards;

import ca.mcgill.splendorclient.model.tokens.TokenType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javafx.scene.paint.Color;

/**
 * Represents a Splendor Card with id, prestige, tokenBonus, cardType, discount
 * and cost This class implements the Flyweight design pattern.
 */
public class Card {
  private static final ArrayList<Card> cards = new ArrayList<>();
  // this eventually will be the png that is on the actual card
  private final int id;
  private final int prestige;
  private final TokenType tokenBonus;
  private final CardType cardType;
  // IDK if we need this, but orient pairing can increase discount from 1 to 2 so
  // maybe
  private final int discount;
  private final int[] cost;
  private Color borderColor;
  private Color fillColor;

  /**
   * Creates a card.
   *
   * @param id         The id of the card
   * @param prestige   The prestige earned upon purchasing the card
   * @param tokenBonus The bonus received upon purchasing the card
   * @param cardType   The type of card
   * @param discount   The card's discount
   * @param cost       The cost of the card
   */
  public Card(int id, int prestige, TokenType tokenBonus,
              CardType cardType, int discount, int[] cost) {
    this.id = id;
    this.prestige = prestige;
    this.tokenBonus = tokenBonus;
    this.cardType = cardType;
    this.discount = discount;
    this.cost = cost;
  }
  // bad design ill get back to this

  public Card getCard(int id) {
    return cards.get(id);
  }

  /**
   * Creates a deck made of cards of a certain type of cards.
   *
   * @param type The type of Card and Deck
   */
  public static List<Card> makeDeck(CardType type) {
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
  public int getDiscount() {
    return discount;
  }

  /**
   * Returns the cost of this card.
   *
   * @return the cost of this card
   */
  public int[] getCost() {
    return cost;
  }

  /**
   * Returns the type of this card.
   *
   * @return the type of this card
   */
  public CardType getCardType() {
    return cardType;
  }

  /**
   * Returns the token bonus type of this card.
   *
   * @return the token bonus type of this card
   */
  public TokenType getTokenType() {
    return tokenBonus;
  }

  // dw I didn't write this its generated
  /**
   * Generates cards for the decks.
   */
  private static void generateCards() {
    cards.add(new Card(0, 1, TokenType.DIAMOND, CardType.BASE1, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(1, 1, TokenType.SAPPHIRE, CardType.BASE1, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(2, 1, TokenType.EMERALD, CardType.BASE1, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(3, 1, TokenType.RUBY, CardType.BASE1, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(4, 1, TokenType.ONYX, CardType.BASE1, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(5, 1, TokenType.DIAMOND, CardType.BASE1, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(6, 1, TokenType.SAPPHIRE, CardType.BASE1, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(7, 1, TokenType.EMERALD, CardType.BASE1, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(8, 1, TokenType.RUBY, CardType.BASE1, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(9, 1, TokenType.ONYX, CardType.BASE1, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(10, 1, TokenType.DIAMOND, CardType.BASE1, 1, new int[] { 0, 0, 4, 0, 0 }));
    cards.add(new Card(11, 1, TokenType.SAPPHIRE, CardType.BASE1, 1, new int[] { 0, 0, 0, 4, 0 }));
    cards.add(new Card(12, 1, TokenType.EMERALD, CardType.BASE1, 1, new int[] { 0, 0, 0, 0, 4 }));
    cards.add(new Card(13, 1, TokenType.RUBY, CardType.BASE1, 1, new int[] { 4, 0, 0, 0, 0 }));
    cards.add(new Card(14, 1, TokenType.ONYX, CardType.BASE1, 1, new int[] { 0, 4, 0, 0, 0 }));

    cards.add(new Card(15, 1, TokenType.DIAMOND, CardType.BASE1, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(16, 1, TokenType.SAPPHIRE, CardType.BASE1, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(17, 1, TokenType.EMERALD, CardType.BASE1, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(18, 1, TokenType.RUBY, CardType.BASE1, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(19, 1, TokenType.ONYX, CardType.BASE1, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(20, 1, TokenType.DIAMOND, CardType.BASE1, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(21, 1, TokenType.SAPPHIRE, CardType.BASE1, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(22, 1, TokenType.EMERALD, CardType.BASE1, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(23, 1, TokenType.RUBY, CardType.BASE1, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(24, 1, TokenType.ONYX, CardType.BASE1, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(25, 1, TokenType.DIAMOND, CardType.BASE1, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(26, 1, TokenType.SAPPHIRE, CardType.BASE1, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(27, 1, TokenType.EMERALD, CardType.BASE1, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(28, 1, TokenType.RUBY, CardType.BASE1, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(29, 1, TokenType.ONYX, CardType.BASE1, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(30, 1, TokenType.DIAMOND, CardType.BASE1, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(31, 1, TokenType.SAPPHIRE, CardType.BASE1, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(32, 1, TokenType.EMERALD, CardType.BASE1, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(33, 1, TokenType.RUBY, CardType.BASE1, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(34, 1, TokenType.ONYX, CardType.BASE1, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(35, 1, TokenType.DIAMOND, CardType.BASE1, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(36, 1, TokenType.SAPPHIRE, CardType.BASE1, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(37, 1, TokenType.EMERALD, CardType.BASE1, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(38, 1, TokenType.RUBY, CardType.BASE1, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(39, 1, TokenType.ONYX, CardType.BASE1, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(40, 1, TokenType.DIAMOND, CardType.BASE2, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(41, 1, TokenType.SAPPHIRE, CardType.BASE2, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(42, 1, TokenType.EMERALD, CardType.BASE2, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(43, 1, TokenType.RUBY, CardType.BASE2, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(44, 1, TokenType.ONYX, CardType.BASE2, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(45, 1, TokenType.DIAMOND, CardType.BASE2, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(46, 1, TokenType.SAPPHIRE, CardType.BASE2, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(47, 1, TokenType.EMERALD, CardType.BASE2, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(48, 1, TokenType.RUBY, CardType.BASE2, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(49, 1, TokenType.ONYX, CardType.BASE2, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(50, 1, TokenType.DIAMOND, CardType.BASE2, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(51, 1, TokenType.SAPPHIRE, CardType.BASE2, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(52, 1, TokenType.EMERALD, CardType.BASE2, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(53, 1, TokenType.RUBY, CardType.BASE2, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(54, 1, TokenType.ONYX, CardType.BASE2, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(55, 1, TokenType.DIAMOND, CardType.BASE2, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(56, 1, TokenType.SAPPHIRE, CardType.BASE2, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(57, 1, TokenType.EMERALD, CardType.BASE2, 1, new int[] { 0, 0, 0, 0, 4 }));
    cards.add(new Card(58, 1, TokenType.RUBY, CardType.BASE2, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(59, 1, TokenType.ONYX, CardType.BASE2, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(60, 1, TokenType.DIAMOND, CardType.BASE2, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(61, 1, TokenType.SAPPHIRE, CardType.BASE2, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(62, 1, TokenType.EMERALD, CardType.BASE2, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(63, 1, TokenType.RUBY, CardType.BASE2, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(64, 1, TokenType.ONYX, CardType.BASE2, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(65, 1, TokenType.DIAMOND, CardType.BASE2, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(66, 1, TokenType.SAPPHIRE, CardType.BASE2, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(67, 1, TokenType.EMERALD, CardType.BASE2, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(68, 1, TokenType.RUBY, CardType.BASE2, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(69, 1, TokenType.ONYX, CardType.BASE2, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(70, 1, TokenType.DIAMOND, CardType.BASE3, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(71, 1, TokenType.SAPPHIRE, CardType.BASE3, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(72, 1, TokenType.EMERALD, CardType.BASE3, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(73, 1, TokenType.RUBY, CardType.BASE3, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(74, 1, TokenType.ONYX, CardType.BASE3, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(75, 1, TokenType.DIAMOND, CardType.BASE3, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(76, 1, TokenType.SAPPHIRE, CardType.BASE3, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(77, 1, TokenType.EMERALD, CardType.BASE3, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(78, 1, TokenType.RUBY, CardType.BASE3, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(79, 1, TokenType.ONYX, CardType.BASE3, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(80, 1, TokenType.DIAMOND, CardType.BASE3, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(81, 1, TokenType.SAPPHIRE, CardType.BASE3, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(82, 1, TokenType.EMERALD, CardType.BASE3, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(83, 1, TokenType.RUBY, CardType.BASE3, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(84, 1, TokenType.ONYX, CardType.BASE3, 1, new int[] { 0, 1, 0, 0, 0 }));

    cards.add(new Card(85, 1, TokenType.DIAMOND, CardType.BASE3, 1, new int[] { 0, 0, 1, 0, 0 }));
    cards.add(new Card(86, 1, TokenType.SAPPHIRE, CardType.BASE3, 1, new int[] { 0, 0, 0, 1, 0 }));
    cards.add(new Card(87, 1, TokenType.EMERALD, CardType.BASE3, 1, new int[] { 0, 0, 0, 0, 1 }));
    cards.add(new Card(88, 1, TokenType.RUBY, CardType.BASE3, 1, new int[] { 1, 0, 0, 0, 0 }));
    cards.add(new Card(89, 1, TokenType.ONYX, CardType.BASE3, 1, new int[] { 0, 1, 0, 0, 0 }));
  }

  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof Card)) {
      return false;
    }
    Card card = (Card) object;
    return id == card.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  // TODO: need things like cost, associated gem discount, and prestige.
  // Robillard might want these to be flyweights.

}
