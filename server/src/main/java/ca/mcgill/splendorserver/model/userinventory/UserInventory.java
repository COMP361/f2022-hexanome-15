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
  private final Optional<CoatOfArmsPile> coatOfArmsPile;


  /**
   * Initialize User Inventory Model.
   *
   * @param pile The token piles in a user's inventory
   * @param name This player's username
   * @param coatOfArmsType The color of coat of arms this player is using
   */
  public UserInventory(List<TokenPile> pile, PlayerWrapper name,
                       Optional<CoatOfArmsType> coatOfArmsType) {
    assert pile != null && name != null;
    cards          = new ArrayList<>();
    tokenPiles     = new EnumMap<>(List.copyOf(pile)
                                       .stream()
                                       .collect(
                                           Collectors.toMap(
                                               TokenPile::getType,
                                               tokens -> tokens
                                           )));
    playerWrapper  = name;
    prestigeWon    = 0;
    visitingNobles = new ArrayList<>();
    acquiredPowers = new ArrayList<>();
    if (coatOfArmsType.isPresent()) {
      coatOfArmsPile = Optional.of(new CoatOfArmsPile(coatOfArmsType.get()));
    } else {
      coatOfArmsPile = Optional.empty();
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
   * Returns the coat of arms pile belonging to this player.
   *
   * @return the coat of arms pile belonging to this player
   */
  public Optional<CoatOfArmsPile> getCoatOfArmsPile() {
    return coatOfArmsPile;
  }

  /**
   * Returns the token types of all the non-empty token piles in the user inventory.
   *
   * @return the list of token types of all the non-empty token piles in the user inventory
   */
  public List<TokenType> getTokenTypes() {
    return tokenPiles.values()
                     .stream()
                     .filter(tokens -> tokens.getSize() != 0)
                     .map(TokenPile::getType)
                     .toList();
  }

  /**
   * Returns a boolean determining if the token pile of a given token pile
   * is empty in this user inventory.
   *
   * @return the given boolean
   */
  public boolean hasTokenType(TokenType tokenType) {
    assert tokenType != null;
    return tokenPiles.values()
                     .stream()
                     .anyMatch(tokens -> tokens.getType() == tokenType);
  }

  /**
   * Adds tokens to the token pile in the user inventory with the same type.
   *
   * @param token the tokens to be added
   */
  public void addTokens(Token... token) {
    for (Token t : token) {
      tokenPiles.get(t.getType())
                .addToken(t);
    }
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

  /*public Optional<Token> removeTokenOpt(Token token) {
    if (tokenPiles.containsKey(token.getType()) && tokenPiles.get(token.getType()) != null) {
      return tokenPiles.get(token.getType())
                       .removeTokenOpt();
    }
    return Optional.empty();
  }*/

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
   * Number of purchased cards in the user inventory.
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
   * Returns an unpaired spice card in the user inventory.
   *
   * @return an unpaired spice card
   */
  public OrientCard getUnpairedSpiceCard() {
    for (Card card : cards) {
      if (card instanceof OrientCard
            && ((OrientCard) card).isSpiceBag()
            && card.getTokenBonusType().isEmpty()
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
   * Checks if the current player can afford a card with their current card bonuses and tokens.
   *
   * @param card The card the player would like to purchase
   * @return a boolean determining whether the player can afford a card
   */
  public boolean canAffordCard(Card card) {
    assert card != null;
    int currentGoldTokenCount = getGoldTokenCount();
    for (Map.Entry<TokenType, Integer> entry : card.getCardCost()
                                                   .entrySet()) {
      amountGoldTokensNeeded(entry.getKey(), entry.getValue(), currentGoldTokenCount);
      if (currentGoldTokenCount < 0) {
        return false;
      }
    }
    return true;
  }

  private int getGoldTokenCount() {
    return tokenPiles.computeIfAbsent(TokenType.GOLD, TokenPile::new)
                     .getSize();

  }

  /**
   * Calculates the amount of gold tokens needed to pay the cost associated with this token type.
   * If negative then the card cannot be purchased.
   *
   * @return the amount of gold tokens needed
   */
  private void amountGoldTokensNeeded(TokenType tokenType, int cost, int currentGoldTokenCount) {
    assert tokenType != null && cost >= 0;
    int bonusDiscount = cards.stream()
                             .filter(card -> card.getTokenBonusType().get() == tokenType)
                             .map(Card::getTokenBonusAmount)
                             .reduce(0, Integer::sum);
    int actualCost = cost - bonusDiscount;
    currentGoldTokenCount = actualCost - tokenPiles.get(tokenType).getSize();
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
              "Noble cannot be reserved if it "
                + "has already been reserved or is currently visiting a player");
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
    }
  }

  /**
   * Removes card from deck based on bonus colour, prioritizes spice bag cards.
   *
   * @param tokenType the token type of the element to remove
   * @throws AssertionError if tokenType == null
   */
  public void discardByBonusType(TokenType tokenType) {
    assert tokenType != null;
    for (int i = 0; i < cards.size(); i++) {
      Card current = cards.get(i);

      if (current.getTokenBonusType().get() == tokenType
            && current.getCardStatus() != CardStatus.RESERVED
            && ((OrientCard) current).isSpiceBag()) {
        cards.remove(i);
        return;
      }
    }
    for (int j = 0; j < cards.size(); j++) {
      if (cards.get(j).getTokenBonusType().get() == tokenType
            && cards.get(j).getCardStatus() != CardStatus.RESERVED) {
        cards.remove(j);
        return;
      }
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
    cards.add(card);

    // accredit the prestige points won by purchasing this card
    addPrestige(card.getPrestige());

    // collect the tokens expended to purchase this card
    List<Token> costs = new ArrayList<>();
    // loop over all token costs and deduct the correct amount
    // taking into consideration the discounts
    for (Map.Entry<TokenType, Integer> entry : card.getCardCost()
                                                   .entrySet()) {
      // bonusDiscount = sum(tokenBonusAmount)
      // for owned cards that match the current cost token in iteration
      int bonusDiscount = cards.stream()
                               .filter(
                                   c -> c.getTokenBonusType().get()
                                          == entry.getKey() && c.isPurchased())
                               .map(Card::getTokenBonusAmount)
                               .reduce(0, Integer::sum);
      int actualCost = entry.getValue() - bonusDiscount;
      costs.addAll(removeTokensByTokenType(entry.getKey(), actualCost));
    }
    return costs;
  }

  private void addPrestige(int prestige) {
    assert prestige >= 0;
    prestigeWon += prestige;
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

    // loop over the visit requirements and see if bonuses in this inventory are sufficient
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
   * Determines if purchasing the given card in addition to whatever bonuses
   * are already in the inventory is sufficient to receive a visit from the given noble.
   *
   * @param noble noble to check if it will visit
   * @param card  additional card to consider in checking if noble will visit
   * @return if the card bonuses as well as
   *     the currently owned bonuses yield a visit from given noble
   */
  public boolean canBeVisitedByNobleWithCardPurchase(Noble noble, Card card) {
    assert noble != null && card != null;
    // cannot be visited by noble that is already visiting someone else
    if (noble.getStatus() == NobleStatus.VISITING) {
      return false;
    }

    // loop over the visit requirements and see if bonuses in this inventory are sufficient
    // in addition to those gained by the potential purchase of a card
    for (Map.Entry<TokenType, Integer> entry : noble.getVisitRequirements()
                                                    .entrySet()) {
      if (card.getTokenBonusType().get() == entry.getKey() && notEnoughBonusesFor(
          entry.getKey(),
          entry.getValue()
              - card.getTokenBonusAmount()
      )) {
        return false;
      } else if (notEnoughBonusesFor(entry.getKey(), entry.getValue())) {
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
  }

  private boolean notEnoughBonusesFor(TokenType tokenType, int amount) {
    assert tokenType != null && amount >= 0;
    // gets all cards that are purchased and have matching bonus token type
    // accumulate the result and see if enough for the given amount
    return cards.stream()
                .filter(card -> card.getTokenBonusType().get() == tokenType && card.isPurchased())
                .map(Card::getTokenBonusAmount)
                .reduce(0, Integer::sum) < amount;
  }

  private List<Token> removeTokensByTokenType(TokenType tokenType, int n) {
    assert tokenType != null && n >= 0;
    List<Token> removed = new ArrayList<>(n);
    //Removing gold tokens
    int goldTokensNeeded = n - tokenPiles.get(tokenType).getSize();
    if (goldTokensNeeded > 0) {
      for (int i = 0; i < goldTokensNeeded; i++) {
        removed.add(removeTokenByTokenType(TokenType.GOLD));
      }
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

  /* @Override
  public void onAction(CardView cardView) {
    boolean affordable = true;
    for (int i = 0; i < cardView.getCard().get().getCost().length; i++) {
      for (TokenPile tokenPile : tokenPiles) {
        if (tokenPile.getType().ordinal() == i) {
          if (cardView.getCard().get().getCost()[i] > 0
                && tokenPile.getSize() < cardView.getCard().get().getCost()[i]) {
            affordable = false;
          }
        }
      }
    }
    if (affordable) {
      notifyObservers(cardView.getCard().get());
      cards.add(cardView.getCard().get());
      for (int i = 0; i < cardView.getCard().get().getCost().length; i++) {
        for (TokenPile tokenPile : tokenPiles) {
          if (tokenPile.getType().ordinal() == i) {
            if (cardView.getCard().get().getCost()[i] > 0
                  && tokenPile.getSize() >= cardView.getCard().get().getCost()[i]) {
              for (int j = 0; j < cardView.getCard().get().getCost()[i]; j++) {
                tokenPile.removeToken();
              }
            }
          }
        }
      }
    } else {
      cardView.revokePurchaseAttempt();
    }
  }*/


  /**
   * Adds a token pile to the user inventory.
   *
   * @param pile the pile to be added
   */
  public void addPile(TokenPile pile) {
    assert pile != null;
    if (!tokenPiles.containsKey(pile.getType())) {
      tokenPiles.put(pile.getType(), pile);
    }
  }

  /**
   * Adds a power to the user inventory.
   *
   * @param power the power to be added
   */
  public void addPower(Power power) {
    assert power != null;
    acquiredPowers.add(power);
  }

  /**
   * Checks if the player can receive a power.
   *
   * @return a boolean determining if the player can receive a power
   */
  public boolean canReceivePower(Power power) {
    assert power != null;
    return !acquiredPowers.contains(power);
  }


  @Override
  public Iterator<Card> iterator() {

    return cards.iterator();
  }

}
