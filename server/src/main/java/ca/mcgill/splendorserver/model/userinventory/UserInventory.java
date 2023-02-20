package ca.mcgill.splendorserver.model.userinventory;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.IllegalGameStateException;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardStatus;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.nobles.NobleStatus;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
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


  /**
   * Initialize User Inventory Model.
   *
   * @param pile The token piles in a user's inventory
   * @param name This player's username
   */
  public UserInventory(List<TokenPile> pile, PlayerWrapper name) {
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
  }

  public int getPrestigeWon() {
    return prestigeWon;
  }


  public List<TokenType> getTokenTypes() {
    return tokenPiles.values()
                     .stream()
                     .filter(tokens -> tokens.getSize() != 0)
                     .map(TokenPile::getType)
                     .toList();
  }

  public boolean hasTokenType(TokenType tokenType) {
    return tokenPiles.values()
                     .stream()
                     .anyMatch(tokens -> tokens.getType() == tokenType);
  }

  public void addTokens(Token... token) {
    for (Token t : token) {
      tokenPiles.get(t.getType())
                .addToken(t);
    }
  }

  /**
   * Assumes that the inventory contains at least one token of the given type.
   *
   * @param token token to remove, cannot be null
   * @return the removed token, could be null if this inventory doesn't have any of that specific token
   */
  public Token removeTokenByTokenType(TokenType token) {
    return tokenPiles.get(token)
                     .removeToken();
  }

  public Optional<Token> removeTokenOpt(Token token) {
    if (tokenPiles.containsKey(token.getType()) && tokenPiles.get(token.getType()) != null) {
      return tokenPiles.get(token.getType())
                       .removeTokenOpt();
    }
    return Optional.empty();
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
   * Number of purchased cards.
   *
   * @return number of purchased cards.
   */
  public int purchasedCardCount() {
    return (int) cards
        .stream()
        .filter(Card::isPurchased)
        .count();
  }

  public boolean hasCardReserved(Card card) {
    assert card != null;
    return cards.contains(card) && card.getCardStatus() == CardStatus.RESERVED;
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

  public boolean canAffordCard(Card card) {
    for (Map.Entry<TokenType, Integer> entry : card.getCardCost()
                                                   .entrySet()) {
      if (!hasEnoughTokensFor(entry.getKey(), entry.getValue())) {
        return false;
      }
    }
    return true;
  }

  private int getGoldTokenCount() {
    return tokenPiles.computeIfAbsent(TokenType.GOLD, TokenPile::new)
                     .getSize();

  }

  private boolean hasEnoughTokensFor(TokenType tokenType, int cost) {
    int goldTokenCount = getGoldTokenCount();
    int bonusDiscount = cards.stream()
                             .filter(card -> card.getTokenBonusType() == tokenType)
                             .map(Card::getTokenBonusAmount)
                             .reduce(0, Integer::sum);
    return tokenPiles.get(tokenType)
                     .getSize() + goldTokenCount >= (cost - bonusDiscount);
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
   * Assumes that it is legal to buy the given card. Adds the card to the users inventory as
   * purchased and deducts the appropriate amount of tokens from their inventory also taking
   * into consideration any token type bonuses they may have from owned cards.
   *
   * @param card purchased card, cannot be null
   * @return TokenTypes corresponding to the tokens which were removed in order to purchase the
   * given card taking into consideration any token bonuses for owned cards.
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
    // loop over all token costs and deduct the correct amount taking into consideration the discounts
    for (Map.Entry<TokenType, Integer> entry : card.getCardCost()
                                                   .entrySet()) {
      // bonusDiscount = sum(tokenBonusAmount) for owned cards that match the current cost token in iteration
      int bonusDiscount = cards.stream()
                               .filter(
                                   c -> c.getTokenBonusType() == entry.getKey() && c.isPurchased())
                               .map(Card::getTokenBonusAmount)
                               .reduce(0, Integer::sum);
      int actualCost = entry.getValue() - bonusDiscount;
      costs.addAll(removeTokensByTokenType(entry.getKey(), actualCost));
    }
    return costs;
  }

  private void addPrestige(int prestige) {
    prestigeWon += prestige;
  }

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
   * Determines if purchasing the given card in addition to whatever bonuses are already in the inventory is
   * sufficient to receive a visit from the given noble.
   *
   * @param noble noble to check if it will visit
   * @param card  additional card to consider in checking if noble will visit
   * @return if the card bonuses as well as the currently owned bonuses yield a visit from given noble
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
      if (card.getTokenBonusType() == entry.getKey() && notEnoughBonusesFor(
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
   * Assumes that it is legal for the noble to be visiting this inventory based on visit requirement.
   *
   * @param noble noble which is visiting this inventory, cannot be null
   */
  public void receiveVisitFrom(Noble noble) {
    assert noble != null;
    addPrestige(noble.getPrestige());
    visitingNobles.add(noble);
  }

  private boolean notEnoughBonusesFor(TokenType tokenType, int amount) {
    // gets all cards that are purchased and have matching bonus token type
    // accumulate the result and see if enough for the given amount
    return cards.stream()
                .filter(card -> card.getTokenBonusType() == tokenType && card.isPurchased())
                .map(Card::getTokenBonusAmount)
                .reduce(0, Integer::sum) < amount;
  }

  private List<Token> removeTokensByTokenType(TokenType tokenType, int n) {
    List<Token> removed = new ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      removed.add(removeTokenByTokenType(tokenType));
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

//  @Override
//  public void onAction(CardView cardView) {
//    boolean affordable = true;
//    for (int i = 0; i < cardView.getCard().get().getCost().length; i++) {
//      for (TokenPile tokenPile : tokenPiles) {
//        if (tokenPile.getType().ordinal() == i) {
//          if (cardView.getCard().get().getCost()[i] > 0
//                && tokenPile.getSize() < cardView.getCard().get().getCost()[i]) {
//            affordable = false;
//          }
//        }
//      }
//    }
//    if (affordable) {
//      notifyObservers(cardView.getCard().get());
//      cards.add(cardView.getCard().get());
//      for (int i = 0; i < cardView.getCard().get().getCost().length; i++) {
//        for (TokenPile tokenPile : tokenPiles) {
//          if (tokenPile.getType().ordinal() == i) {
//            if (cardView.getCard().get().getCost()[i] > 0
//                  && tokenPile.getSize() >= cardView.getCard().get().getCost()[i]) {
//              for (int j = 0; j < cardView.getCard().get().getCost()[i]; j++) {
//                tokenPile.removeToken();
//              }
//            }
//          }
//        }
//      }
//    } else {
//      cardView.revokePurchaseAttempt();
//    }
//  }


  /**
   * Adds a token pile to the user inventory.
   *
   * @param pile the pile to be added
   */
  public void addPile(TokenPile pile) {
    if (!tokenPiles.containsKey(pile.getType())) {
      tokenPiles.put(pile.getType(), pile);
    }
  }


  @Override
  public Iterator<Card> iterator() {
    return cards.iterator();
  }

}
