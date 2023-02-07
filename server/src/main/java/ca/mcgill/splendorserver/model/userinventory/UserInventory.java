package ca.mcgill.splendorserver.model.userinventory;

import ca.mcgill.splendorserver.games.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardStatus;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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


  /**
   * Initialize User Inventory Model.
   *
   * @param pile The token piles in a user's inventory
   * @param name This player's username
   */
  public UserInventory(List<TokenPile> pile, PlayerWrapper name) {
    cards         = new ArrayList<>();
    tokenPiles    = new EnumMap<>(List.copyOf(pile)
                                      .stream()
                                      .collect(
                                          Collectors.toMap(
                                              TokenPile::getType,
                                              tokens -> tokens
                                          )));
    playerWrapper = name;
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
    return cards.size();
  }

  /**
   * Number of purchased cards.
   *
   * @return number of purchased cards.
   */
  public int purchasedCardCount() {
    return (int) cards
        .stream()
        .filter(card -> card.getCardStatus() == CardStatus.PURCHASED)
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
      if (!hasEnoughFor(entry.getKey(), entry.getValue())) {
        return false;
      }
    }
    return true;
  }

  private int getGoldTokenCount() {
    return tokenPiles.computeIfAbsent(TokenType.GOLD, TokenPile::new)
                     .getSize();

  }

  private boolean hasEnoughFor(TokenType tokenType, int cost) {
    int goldTokenCount = getGoldTokenCount();
    int bonusDiscount = cards.stream()
                             .filter(card -> card.getTokenBonusType() == tokenType)
                             .map(card -> card.getTokenBonusAmount())
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
    card.setCardStatus(CardStatus.RESERVED);
    cards.add(card);
  }

  /**
   * Adds card when it's been purchased.
   *
   * @param card purchased card, cannot be null
   */
  public EnumMap<TokenType, Integer> addPurchasedCard(Card card) {
    assert card != null;
    card.setCardStatus(CardStatus.PURCHASED);
    cards.add(card);

    EnumMap<TokenType, Integer> costMap = new EnumMap<>(TokenType.class);
    // loop over all token costs and deduct the correct amount taking into consideration the discounts
    for (Map.Entry<TokenType, Integer> entry : card.getCardCost()
                                                   .entrySet()) {
      int bonusDiscount = cards.stream()
                               .filter(c -> c.getTokenBonusType() == entry.getKey())
                               .map(Card::getTokenBonusAmount)
                               .reduce(0, Integer::sum);
      int actualCost = entry.getValue() - bonusDiscount;
      costMap.put(entry.getKey(), actualCost);
    }
    return costMap;
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
