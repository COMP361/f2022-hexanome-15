package ca.mcgill.splendorserver.model.userinventory;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.IllegalGameStateException;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardStatus;
import ca.mcgill.splendorserver.model.cards.DeckType;
import ca.mcgill.splendorserver.model.cards.OrientCard;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.nobles.NobleStatus;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsPile;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
import ca.mcgill.splendorserver.model.tradingposts.Power;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Represents the inventory of a Splendor player.
 * Contains cards and token piles.
 * Observes CardView to assess whether a card is affordable.
 * Observed by CardColumnView to add the card to the inventory.
 * Observed by MoveManagerDepr to create the current move
 */
public class UserInventory implements Iterable<Card> {

  private final List<Card>                    cards;
  private final EnumMap<TokenType, TokenPile> tokenPiles;
  private final PlayerWrapper                 playerWrapper;
  private       int                           prestigeWon;
  private final List<Noble>                   visitingNobles;
  private final List<Power> acquiredPowers;
  private final CoatOfArmsPile coatOfArmsPile;


  /**
   * Initialize User Inventory Model.
   *
   * @param name This player's username
   * @param coatOfArmsType The color of coat of arms this player is using
   */
  public UserInventory(PlayerWrapper name, Optional<CoatOfArmsType> coatOfArmsType) {
    assert name != null;
    cards          = new ArrayList<>();
    playerWrapper  = name;
    prestigeWon    = 0;
    visitingNobles = new ArrayList<>();
    acquiredPowers = new ArrayList<>();

    tokenPiles = new EnumMap<TokenType, TokenPile>(TokenType.class);
    TokenPile white = new TokenPile(TokenType.DIAMOND);
    tokenPiles.put(TokenType.DIAMOND, white);
    TokenPile blue = new TokenPile(TokenType.SAPPHIRE);
    tokenPiles.put(TokenType.SAPPHIRE, blue);
    TokenPile green = new TokenPile(TokenType.EMERALD);
    tokenPiles.put(TokenType.EMERALD, green);
    TokenPile red = new TokenPile(TokenType.RUBY);
    tokenPiles.put(TokenType.RUBY, red);
    TokenPile black = new TokenPile(TokenType.ONYX);
    tokenPiles.put(TokenType.ONYX, black);
    TokenPile gold = new TokenPile(TokenType.GOLD);
    tokenPiles.put(TokenType.GOLD, gold);

    if (coatOfArmsType.isPresent()) {
      coatOfArmsPile = new CoatOfArmsPile(coatOfArmsType.get());
    } else {
      coatOfArmsPile = null;
    }
  }

  /**
   * Returns the amount of prestige won by this player.
   *
   * @return the amount of prestige won by this player
   */
  public int getPrestigeWon() {

    return prestigeWon;
  }

  /**
   * Returns the list of cards in the user inventory.
   *
   * @return the list of cards in the user inventory
   */
  public List<Card> getCards() {

    return cards;
  }

  /**
   * Returns the token piles in the user inventory.
   *
   * @return the token piles in the user inventory
   */
  public EnumMap<TokenType, TokenPile> getTokenPiles() {

    return tokenPiles;
  }

  /**
   * Returns the list of nobles in the user inventory.
   *
   * @return the list of nobles in the user inventory
   */
  public List<Noble> getNobles() {

    return visitingNobles;
  }

  /**
   * Returns the list of powers in the user inventory.
   *
   * @return the list of powers in the user inventory
   */
  public List<Power> getPowers() {

    return acquiredPowers;
  }

  /**
   * Returns the coat of arms pile in the user inventory.
   *
   * @return the coat of arms pile in the user inventory
   */
  public CoatOfArmsPile getCoatOfArmsPile() {

    return coatOfArmsPile;
  }

  /**
   * Adds a token to the user inventory.
   *
   * @param token the token to be added
   */
  public void addToken(Token token) {
    assert token != null;
    tokenPiles.get(token.getType()).addToken(token);
  }

  /**
   * Assumes that the inventory contains at least one token of the given type.
   *
   * @param type type of token to remove, cannot be null
   * @return the removed token,
   *     could be null if this inventory doesn't have any of that specific token
   */
  public Token removeTokenByTokenType(TokenType type) {
    assert type != null;
    return tokenPiles.get(type)
                     .removeToken();
  }

  /**
   * Gets the number of cards in inventory.
   *
   * @return the number of cards in this inventory.
   */
  public int reservedCardCount() {
    return (int) cards.stream()
                      .filter(Card::isReserved)
                      .count();
  }

  /**
   * Returns the number of purchased cards in the user inventory.
   *
   * @return number of purchased cards in the user inventory
   */
  public int purchasedCardCount() {
    return (int) cards
        .stream()
        .filter(Card::isPurchased)
        .count();
  }

  /**
   * Returns the number of purchased cards of a certain token type in the user inventory.
   *
   * @param type the token bonus type of the purchased cards
   * @return the number of purchased cards of a certain token type in the user inventory
   */
  public int purchasedCardCountByType(TokenType type) {
    return (int) cards
                   .stream()
                   .filter(card -> card.isPurchased() && card.getTokenBonusType() == type)
                   .count();
  }

  /**
   * Returns an unpaired spice card in the user inventory.
   *
   * @return an unpaired spice card
   */
  public OrientCard getUnpairedSpiceCard() {
    for (Card card : cards) {
      if (card instanceof OrientCard
            && ((OrientCard) card).isSpiceBag()
            && card.getTokenBonusType() == null
            && card.getTokenBonusAmount() == 0) {
        return (OrientCard) card;
      }
    }
    return null;
  }

  /**
   * Checks if the selected card is reserved.
   *
   * @param card the selected card
   * @return a boolean determining if the selected card is reserved
   */
  public boolean hasCardReserved(Card card) {
    assert card != null;
    return cards.contains(card) && card.getCardStatus() == CardStatus.RESERVED;
  }

  /**
   * Checks if the selected card has been purchased by this player.
   *
   * @param card the selected card
   * @return a boolean determining if the selected card is purchased by this player
   */
  public boolean hasCard(Card card) {
    assert card != null;
    return cards.contains(card) && card.getCardStatus() == CardStatus.PURCHASED;
  }


  /**
   * If the inventory has cards.
   *
   * @return if they have cards or not.
   */
  public boolean hasCardsReserved() {
    return cards.stream()
                .anyMatch(card -> card.getCardStatus() == CardStatus.RESERVED);
  }

  /**
   * Returns the number of spice bag cards of a certain token type in the user inventory.
   *
   * @param type the token type of the spice bag carda
   * @return the number of spice bag cards of a certain token type in the user inventory
   */
  public int getNumSpiceCardsByType(TokenType type) {
    int sum = 0;
    for (Card card : cards) {
      if (card instanceof OrientCard) {
        if (((OrientCard) card).isSpiceBag() && card.getTokenBonusType() == type) {
          sum++;
        }
      }
    }
    return sum;
 }
 
  /**
   * Checks if the current player can afford a card with their current card bonuses and tokens.
   *
   * @param card The card the player would like to purchase
   * @return a boolean determining whether the player can afford a card
   */
  public boolean canAffordCard(Card card) {
    assert card != null;
    int currentGoldTokenCount = getGoldTokenCount() + 2 * purchasedCardCountByType(TokenType.GOLD);
    for (Map.Entry<TokenType, Integer> entry : card.getCardCost()
                                                   .entrySet()) {
      currentGoldTokenCount -= amountGoldTokensNeeded(entry.getKey(), entry.getValue());
      if (currentGoldTokenCount < 0) {
        return false;
      }
    }
    if (card instanceof OrientCard) {
      if (!((OrientCard) card).getBonusActions().isEmpty()) {
        switch (((OrientCard) card).getBonusActions().get(0)) {
          case DISCARD_FIRST_WHITE_CARD -> {
            if (purchasedCardCountByType(TokenType.DIAMOND) < 2) {
              return false;
            }
          }
          case DISCARD_FIRST_BLUE_CARD -> {
            if (purchasedCardCountByType(TokenType.SAPPHIRE) < 2) {
              return false;
            }
          }
          case DISCARD_FIRST_GREEN_CARD -> {
            if (purchasedCardCountByType(TokenType.EMERALD) < 2) {
              return false;
            }
          }
          case DISCARD_FIRST_RED_CARD -> {
            if (purchasedCardCountByType(TokenType.RUBY) < 2) {
              return false;
            }
          }
          case DISCARD_FIRST_BLACK_CARD -> {
            if (purchasedCardCountByType(TokenType.ONYX) < 2) {
              return false;
            }
          }
          case PAIR_SPICE_CARD -> {
            if (purchasedCardCount() < 1) {
              return false;
            }
          }
          default -> {
            return true;
          }
        }
      }
    }
    return true;
  }

  /**
   * Discards the given card from the user inventory.
   *
   * @param card the given card
   */
  public void discardCard(Card card) {
    assert card != null && cards.contains(card);
    removePrestige(card.getPrestige());
    cards.remove(card);
  }

  /**
   * Discards a spice card of a given token type from the user inventory.
   *
   * @param type the given token type
   */
  public void discardSpiceCard(TokenType type) {
    assert type != null;
    for (Card card : cards) {
      if (card instanceof OrientCard) {
        if (((OrientCard) card).isSpiceBag() && card.getTokenBonusType() == type) {
          cards.remove(card);
          return;
        }
      }
    }
  }

  /**
   * Checks if the user inventory has a given power.
   *
   * @param power the given power
   * @return a boolean determining if the user inventory has a given power
   */
  public boolean hasPower(Power power) {
    assert power != null;
    return acquiredPowers.contains(power);
  }

  /**
   * Returns the amount of gold tokens in the user inventory.
   *
   * @return the amount of gold tokens in the user inventory
   */
  private int getGoldTokenCount() {
    return tokenPiles.computeIfAbsent(TokenType.GOLD, TokenPile::new)
                     .getSize();

  }

  private void removeGoldCard() {
    for (Card card : cards) {
      if (card.getTokenBonusType() == TokenType.GOLD) {
        cards.remove(card);
        return;
      }
    }
  }

  private int amountGoldTokensNeeded(TokenType tokenType, int cost) {
    assert tokenType != null && cost >= 0;
    int bonusDiscount = cards.stream()
                             .filter(card -> card.getTokenBonusType() == tokenType)
                             .map(Card::getTokenBonusAmount)
                             .reduce(0, Integer::sum);
    int actualCost = cost - bonusDiscount;
    int goldTokensNeeded = 0;
    if (actualCost > tokenPiles.get(tokenType).getSize()) {
      if (acquiredPowers.contains(Power.GOLD_TOKENS_WORTH_2_GEMS_SAME_COL)) {
        goldTokensNeeded = (int) Math.ceil(((double) actualCost
                                              - (double) tokenPiles
                                                           .get(tokenType).getSize()) / 2);
      } else {
        goldTokensNeeded = actualCost - tokenPiles.get(tokenType).getSize();
      }
    }
    return goldTokensNeeded;
  }


  //TODO: do we check if the card is already in the hand or are the cards all unique???

  /**
   * Adds card to user inventory.
   *
   * @param card card to add.
   * @throws AssertionError if card == null
   */
  public void addReservedCard(Card card) {
    assert card != null;

    // checking that card is not already reserved or purchased
    if (card.getCardStatus() != CardStatus.NONE) {
      throw new IllegalGameStateException(
          "card cannot be reserved if it has already been reserved or purchased");
    }

    card.setCardStatus(CardStatus.RESERVED);
    cards.add(card);
  }

  /**
   * Reserves noble card as part of Orient card bonus action ands adds it to inventory.
   *
   * @param noble noble being reserved
   * @throws AssertionError if noble == null
   */
  public void addReservedNoble(Noble noble) {
    assert noble != null;

    if (noble.getStatus() != NobleStatus.ON_BOARD) {
      throw new IllegalGameStateException(
              "Noble cannot be reserved if it has already been "
              + "reserved or is currently visiting a player");
    }

    noble.setStatus(NobleStatus.RESERVED);
    visitingNobles.add(noble);
  }

  /**
   * Adds the chosen level one Orient card to the inventory.
   *
   * @param card orient level one card to add
   * @throws AssertionError if card == null
   */
  public void addCascadeLevelOne(OrientCard card) {
    assert card != null;

    if (card.getDeckType() == DeckType.ORIENT1) {
      card.setCardStatus(CardStatus.PURCHASED);
      cards.add(card);
      addPrestige(card.getPrestige());
    }
  }

  /**
   * Adds the chosen level two Orient card to the inventory.
   *
   * @param card orient level two card to add
   * @throws AssertionError if card == null
   */
  public void addCascadeLevelTwo(OrientCard card) {
    assert card != null;

    if (card.getDeckType() == DeckType.ORIENT2) {
      card.setCardStatus(CardStatus.PURCHASED);
      cards.add(card);
      addPrestige(card.getPrestige());
    }
  }

  /**
   * Assumes that it is legal to buy the given card. Adds the card to the users inventory as
   * purchased and deducts the appropriate amount of tokens from their inventory also taking
   * into consideration any token type bonuses they may have from owned cards.
   *
   * @param card purchased card, cannot be null
   * @return TokenTypes corresponding to the tokens which were removed in order to purchase the
   *     given card taking into consideration any token bonuses for owned cards.
   */
  public List<Token> purchaseCard(Card card) {
    assert card != null;

    // checking that card is not already purchased
    if (card.getCardStatus() == CardStatus.PURCHASED) {
      throw new IllegalGameStateException("Cannot purchase a card which is already purchased");
    }

    card.setCardStatus(CardStatus.PURCHASED);

    // accredit the prestige points won by purchasing this card
    addPrestige(card.getPrestige());

    // collect the tokens expended to purchase this card
    List<Token> costs = new ArrayList<>();
    // loop over all token costs and deduct the correct amount
    // taking into consideration the discounts
    for (Map.Entry<TokenType, Integer> entry : card.getCardCost()
                                                   .entrySet()) {
      if (entry.getValue() == 0) {
        continue;
      }
      // bonusDiscount = sum(tokenBonusAmount)
      // for owned cards that match the current cost token in iteration
      int bonusDiscount = cards.stream()
                               .filter(
                                   c -> c.getTokenBonusType()
                                          == entry.getKey() && c.isPurchased())
                               .map(Card::getTokenBonusAmount)
                               .reduce(0, Integer::sum);
      int actualCost = entry.getValue() - bonusDiscount;

      int numGoldTokensNeeded = amountGoldTokensNeeded(entry.getKey(), entry.getValue());
      int numGoldCardsNeeded = (int) Math.ceil((double) numGoldTokensNeeded / 2);
      int numGoldCardsUsed = 0;

      while (numGoldCardsNeeded > 0 && purchasedCardCountByType(TokenType.GOLD) > 0) {
        removeGoldCard();
        numGoldCardsNeeded--;
        numGoldCardsUsed++;
      }
      numGoldTokensNeeded -= 2 * numGoldCardsUsed;

      if (actualCost > 0) {
        costs.addAll(removeTokensByTokenType(entry.getKey(), actualCost));
      }
      if (numGoldTokensNeeded > 0) {
        costs.addAll(removeTokensByTokenType(TokenType.GOLD, numGoldTokensNeeded));
      }
    }
    cards.add(card);
    return costs;
  }

  /**
   * Adds prestige to the user inventory.
   *
   * @param prestige the amount of prestige to be added
   */
  private void addPrestige(int prestige) {
    assert prestige >= 0;
    prestigeWon += prestige;
  }

  /**
   * Removes prestige from the user inventory.
   *
   * @param prestige the amount of prestige to be removed
   */
  private void removePrestige(int prestige) {
    assert prestige >= 0;
    prestigeWon -= prestige;
  }

  /**
   * Checks if the current player can be visited by a noble at the end of their turn.
   *
   * @param noble the noble to be visited
   * @return a boolean determining whether the current player can be visited by this noble
   */
  public boolean canBeVisitedByNoble(Noble noble) {
    assert noble != null;
    // cannot be visited by noble that is already visiting someone else
    if (noble.getStatus() == NobleStatus.VISITING) {
      return false;
    }
    // cannot be visited by a noble that is reserved by another player
    if (!visitingNobles.contains(noble) && noble.getStatus() == NobleStatus.RESERVED) {
      return false;
    }

    // loop over the visit requirements and see if bonuses in this inventory are sufficient
    // in addition to those gained by the potential purchase of a card
    for (Map.Entry<TokenType, Integer> entry : noble.getVisitRequirements()
                                                    .entrySet()) {
      if (notEnoughBonusesFor(entry.getKey(), entry.getValue())) {
        return false;
      }
    }
    // otherwise return true
    return true;
  }

  /**
   * Assumes that it is legal for the noble
   * to be visiting this inventory based on visit requirement.
   *
   * @param noble noble which is visiting this inventory, cannot be null
   */
  public void receiveVisitFrom(Noble noble) {
    assert noble != null;
    addPrestige(noble.getPrestige());
    visitingNobles.add(noble);
    noble.setStatus(NobleStatus.VISITING);
  }

  private boolean notEnoughBonusesFor(TokenType tokenType, int amount) {
    assert tokenType != null && amount >= 0;
    // gets all cards that are purchased and have matching bonus token type
    // accumulate the result and see if enough for the given amount
    return purchasedCardCountByType(tokenType) < amount;
  }

  /**
   * Removes tokens from the user inventory.
   *
   * @param tokenType the type of tokens to be removed
   * @param n the number of tokens to be removed
   * @return the list of removed tokens
   */
  private List<Token> removeTokensByTokenType(TokenType tokenType, int n) {
    assert tokenType != null && n >= 0;
    List<Token> removed = new ArrayList<>(n);

    if (tokenPiles.get(tokenType).getSize() < n) {
      for (int i = 0; i < tokenPiles.get(tokenType).getSize(); i++) {
        removed.add(removeTokenByTokenType(tokenType));
      }
    } else {
      for (int i = 0; i < n; i++) {
        removed.add(removeTokenByTokenType(tokenType));
      }
    }
    return removed;
  }

  /**
   * Number of tokens of any type in this inventory.
   *
   * @return total number of tokens in user inventory.
   */
  public int tokenCount() {
    return tokenPiles.values()
                     .stream()
                     .map(TokenPile::getSize)
                     .reduce(0, Integer::sum);
  }

  /**
   * Returns the username of the player who owns this inventory.
   *
   * @return this player.
   */
  public PlayerWrapper getPlayer() {
    return playerWrapper;
  }

  /**
   * Adds a power to the user inventory.
   *
   * @param power the power to be added
   */
  public void addPower(Power power) {
    assert power != null;
    acquiredPowers.add(power);
    if (power == Power.GAIN_5_PRESTIGE) {
      addPrestige(5);
    } else if (power == Power.GAIN_1_PRESTIGE_FOR_EVERY_PLACED_COAT_OF_ARMS) {
      addPrestige(acquiredPowers.size());
    } else if (acquiredPowers.contains(Power.GAIN_1_PRESTIGE_FOR_EVERY_PLACED_COAT_OF_ARMS)) {
      addPrestige(1);
    }
  }

  /**
   * Checks if the player can receive a power.
   *
   * @param tradingPostSlot the tradingPostSlot being unlocked
   * @return a boolean determining if the player can receive a power
   */
  public boolean canReceivePower(TradingPostSlot tradingPostSlot) {
    assert tradingPostSlot != null;
    if (acquiredPowers.contains(tradingPostSlot.getPower())) {
      return false;
    }
    // loop over the trading route unlock requirements
    // and see if bonuses in this inventory are sufficient
    for (Map.Entry<TokenType, Integer> entry : tradingPostSlot.getCardRequirements().entrySet()) {
      if (notEnoughBonusesFor(entry.getKey(), entry.getValue())) {
        return false;
      } else if (tradingPostSlot.isRequiresNoble() && visitingNobles.size() == 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Removes a power from the list of acquired powers in the user inventory.
   * Must be done if discarding cards makes the player unable to reach the requirements.
   *
   * @param power the power to be discarded
   */
  public void removePower(Power power) {
    assert power != null && acquiredPowers.contains(power);
    int index = acquiredPowers.indexOf(power);
    if (power == Power.GAIN_5_PRESTIGE) {
      removePrestige(5);
    } else if (power == Power.GAIN_1_PRESTIGE_FOR_EVERY_PLACED_COAT_OF_ARMS) {
      removePrestige(acquiredPowers.size());
    }
    acquiredPowers.remove(index);
  }

  /**
   * Removes a noble from the list of visiting nobles in the user inventory.
   * Must be done if discarding cards make the player unable to reach the requirements.
   *
   * @param noble the noble to be discarded
   * @return the discarded noble
   */
  public Noble removeNoble(Noble noble) {
    assert noble != null && visitingNobles.contains(noble)
             && noble.getStatus() == NobleStatus.VISITING;
    int index = visitingNobles.indexOf(noble);
    removePrestige(3);
    return visitingNobles.remove(index);
  }


  @Override
  public Iterator<Card> iterator() {

    return cards.iterator();
  }


}
