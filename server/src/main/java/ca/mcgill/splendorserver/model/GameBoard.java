package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.DeckType;
import ca.mcgill.splendorserver.model.cards.OrientCard;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;


/**
 * Model of the gameboard. Necessary due to permanence requirement.
 * Only ever used for initial setup, the rest can be taken care of by the actions.
 *
 * @author lawrenceberardelli
 */
@Embeddable
public class GameBoard {

  private final List<UserInventory>           inventories;
  private final List<Deck>                    decks;
  private final List<Card>                    cardField;
  private final EnumMap<TokenType, TokenPile> tokenPiles;
  private final List<Noble>                   nobles;


  /*
  The following fields are used for actions that require an additional decision
  following the action itself (compound action / move).
  For example: choosing which tokens to return, choosing
  which noble to be visited by in case there are more than 1.
  moveCache := move which involved a compound action and is currently being waited on
  pendingAction := if the game is waiting for an action to be made
   */
  private List<Move> moveCache = new ArrayList<>();
  private boolean pendingAction;
  private Action actionPending;
  private List<TradingPostSlot> tradingPostSlots;
  private final List<City> cities;

  /**
   * Creates a game board.
   *
   * @param inventories The player inventories in the game
   * @param decks       The decks in the game
   * @param cardField   The cards dealt onto the game board
   * @param tokenPiles  The token piles that are on the game board
   * @param nobles      The nobles in the game
   */
  public GameBoard(List<UserInventory> inventories, List<Deck> decks, List<Card> cardField,
                   List<TokenPile> tokenPiles, List<Noble> nobles,
                   List<TradingPostSlot> tradingPostSlots, List<City> cities
  ) {
    this.inventories   = inventories;
    this.decks         = decks;
    this.cardField     = cardField;
    this.tokenPiles    = new EnumMap<>(tokenPiles.stream()
                                                 .collect(Collectors.toMap(
                                                     TokenPile::getType,
                                                     tokens -> tokens
                                                 )));
    this.nobles        = nobles;
    this.pendingAction = false;
    this.tradingPostSlots = tradingPostSlots;
    this.cities = cities;
  }

  /**
   * Applies the given move to this game board model. Note that we can assume that for any move that
   * requires drawing from a deck, that the deck will not be empty otherwise it wouldn't have been
   * offered as a valid move.
   *
   * @param move   selected move, must be valid move, cannot be null
   * @param player player whose turn it is, cannot be null
   */
  public Action applyMove(Move move, PlayerWrapper player) {
    assert move != null && player != null;
    // getting players inventory or throws exception if not there
    UserInventory inventory = getInventoryByPlayerName(player.getName()).orElseThrow(
        () -> new IllegalArgumentException(
            "player (" + player.getName() + ") wasn't found in this current game board"));

    Action pendingAction;
    return switch (move.getAction()) {
      case PURCHASE_DEV -> {
        pendingAction = performPurchaseDev(move, player, inventory);
        if (pendingAction != null) {
          actionPending = pendingAction;
          yield pendingAction;
        } else {
          yield null;
        }
      }
      case PAIR_SPICE_CARD -> {
        pendingAction = performPairSpiceCard(move, inventory);
        if (pendingAction != null) {
          actionPending = pendingAction;
          yield pendingAction;
        } else {
          yield null;
        }
      }
      case TAKE_TOKEN -> {
        performTakeToken(move, inventory);
        if (moveCache.isEmpty()) {
          moveCache.add(move);
          actionPending = Action.TAKE_TOKEN;
          this.pendingAction = true;
          yield actionPending;
        }
        else if (moveCache.size() == 1) {
          //selected two of the same token
          if (moveCache.get(0).getSelectedTokenTypes() == move.getSelectedTokenTypes()) {
            moveCache.clear();
            actionPending = null;
            this.pendingAction = false;
            yield null;
          }
          else {
            moveCache.add(move);
            actionPending = Action.TAKE_TOKEN;
            this.pendingAction = true;
            yield actionPending;
          }
        }
        else {
          moveCache.clear();
          actionPending = null;
          this.pendingAction = false;
          yield null;
        }
      }
      default -> {
        yield null;
      }
    };
  }

  private boolean waitingForAction(Action action) {
    return pendingAction;
  }

  private void cacheAction(Action action) {
    this.pendingAction = true;
  }

  private void unCacheAction() {
    this.pendingAction = false;
  }

  private void setPendingAction(boolean isPending) {
    pendingAction = isPending;
  }
  
  public Action getPendingAction() {
    return actionPending;
  }
  
  private void performTakeToken(Move move, UserInventory inventory) {
    TokenType type = move.getSelectedTokenTypes().get();
    TokenPile pile = this.tokenPiles.get(type);
    Token token = pile.removeToken();
    inventory.addTokens(token);
  }



  private void performReserveDev(Move move, UserInventory inventory) {
    // no gold token (joker) will be received, just the reserved card
    Card selectedCard = getSelectedCardOrThrow(move);
    // if we're taking from the table, replenish table from the deck
    if (move.getCard()
            .isPresent()) {
      // remove the selected card from the board and replenish from same deck type
      int ix = cardField.indexOf(selectedCard);
      cardField.remove(selectedCard);
      replenishTakenCardFromDeck(
          move.getCard()
              .get()
              .getDeckType(),
          ix
      );
    }
    // add to inventory as reserved card now
    inventory.addReservedCard(selectedCard);
  }

  private Card getSelectedCardOrThrow(Move move) {
    // throws an error if a card nor deck level to choose from weren't chosen
    Card selectedCard = move.getCard()
                            .orElse(getCardByDeckLevel(move.getDeckType()
                                                           .orElseThrow(
                                                               () -> new IllegalGameStateException(
                                                                   "If move is to reserve dev "
                                                                     + "card, a card "
                                                                     + "or deck level to draw "
                                                                     + "a card from "
                                                                     + "must be chosen"))));
    return selectedCard;
  }

  private Action performPurchaseDev(Move move, PlayerWrapper player, UserInventory inventory) {
    // checking to see whether they're buying from reserved card in hand / table or from deck
    Card selectedCard = move.getCard()
                            .orElseThrow(() -> new IllegalGameStateException(
                                "if selected move is to purchase a dev card, "
                                  + "there needs to be a card selected"));
    // check to make sure that the card they wish to purchase from their hand is valid
    if (inventory.hasCardReserved(selectedCard)) {
      // they have the card, and it has been reserved, so they can legally buy it
      // add the card to their inventory as a purchased card
      // return the required tokens to purchase the card from user to board
      returnTokensToBoard(inventory.purchaseCard(selectedCard));
      System.out.println(player + " purchased a reserved dev card: " + selectedCard);
    } else if (cardField.contains(selectedCard)) { // purchase face-up dev card
      // purchase card which is face-up on the board
      // purchase card, take it from the face up table and replace that card on table
      int ix = cardField.indexOf(selectedCard);
      returnTokensToBoard(inventory.purchaseCard(cardField.remove(ix)));
      replenishTakenCardFromDeck(selectedCard.getDeckType(), ix);

      System.out.println(
          player + " purchased a face-up dev card from game board: " + selectedCard
      );
    } else {
      // cannot purchase card if it's not reserved in hand or face-up
      System.out.println(
          "A card has been attempted to be purchased "
            + "but it wasn't reserved in inventory nor face-up on game board"
      );
      throw new IllegalGameStateException(
          "Cannot purchase card which isn't reserved or face-up");
    }
    
    if (selectedCard instanceof OrientCard) {
      List<Action> actions = ((OrientCard) selectedCard).getBonusActions();
      if (actions.size() > 0) {
        moveCache.add(move);
        return actions.get(0);
      }
    }
    
    return null;

  }

  private Action performPairSpiceCard(Move move, UserInventory inventory) {
    OrientCard spiceCard = inventory.getUnpairedSpiceCard();
    if (move.getCard().isPresent()
          && inventory.hasCard(move.getCard().get()) && spiceCard != null) {
      ((OrientCard) spiceCard).pairWithCard(move.getCard().get());
      moveCache.add(move);
      for (Action bonusAction : spiceCard.getBonusActions()) {
        boolean doneAction = false;
        for (Move pastMove : moveCache) {
          if (bonusAction == pastMove.getAction()) {
            doneAction = true;
          }
        }
        if (!doneAction) {
          return bonusAction;
        }
      }
    }
    moveCache.clear();
    actionPending = null;
    pendingAction = false;
    return null;
  }

  private void performClaimNobleAction(Move move, UserInventory inventory) {
    // see if player has been visited by a noble and if so that this is valid
    if (move.getNoble()
            .isPresent() && inventory.canBeVisitedByNoble(move.getNoble()
                                                              .get())) {
      // add prestige and tile to inventory
      inventory.receiveVisitFrom(move.getNoble()
                                     .get());
      // TODO: check and see about settlements
    }
  }

  private void performPlaceCoatOfArms(Move move, UserInventory inventory) {
    // See if the player has unlocked the power associated with this trading post slot
    if (move.getTradingPostSlot().isPresent()
          && !move.getTradingPostSlot().get().isFull()
          && !inventory.canReceivePower(move.getTradingPostSlot().get().getPower())) {
      inventory.addPower(move.getTradingPostSlot().get().getPower());
      move.getTradingPostSlot().get()
        .addCoatOfArms(inventory.getCoatOfArmsPile().removeCoatOfArms());
    }
  }

  private void replenishTakenCardFromDeck(DeckType deckType, int replenishIndex) {
    // empty card field means we cant replenish because otherwise it would have been replenished alr
    for (Deck deck : decks) {
      if (deck.getType() == deckType && !deck.isEmpty()) {
        // we know draw is ok cause of the check for emptiness above
        cardField.add(replenishIndex, deck.draw());
      }
    }
  }

  private void moveTokensToUserInventory(UserInventory inventory, TokenType[] selected) {
    for (TokenType tokenType : selected) {
      inventory.addTokens(drawTokenByTokenType(tokenType));
    }
  }

  private void returnTokensToBoardFromInventory(UserInventory inventory, TokenType... selected) {
    for (TokenType tokenType : selected) {
      tokenPiles.get(tokenType)
                .addToken(inventory.removeTokenByTokenType(tokenType));
    }
  }

  private void returnTokensToBoard(List<Token> tokens) {
    for (Token token : tokens) {
      tokenPiles.get(token.getType())
                .addToken(token);
    }
  }

  private Card getCardByDeckLevel(DeckType deckLevel) {
    assert deckLevel != null;
    for (Deck deck : decks) {
      if (deck.getType() == deckLevel) {
        return deck.draw();
      }
    }

    throw new IllegalGameStateException(
        "DeckLevel: " + deckLevel + " wasn't found in decks on game board");
  }

  /**
   * Assumes that drawing from token pile won't be a concern.
   *
   * @param tokenType token type to draw
   * @return a token from selected token type
   */
  private Token drawTokenByTokenType(TokenType tokenType) {
    assert tokenType != null;
    return tokenPiles.get(tokenType)
                     .removeToken();
  }

  /**
   * Assumes that there are gold tokens to take.
   *
   * @return gold token selected from the game board.
   */
  private Token drawGoldToken() {
    return tokenPiles.get(TokenType.GOLD)
                     .removeToken();
  }

  /**
   * Returns the user inventory belonging to the player with the given username.
   *
   * @param playerName the player's username
   * @return the given player's inventory
   */
  public Optional<UserInventory> getInventoryByPlayerName(String playerName) {
    for (UserInventory userInventory : inventories) {
      if (userInventory.getPlayer()
                       .getName()
                       .equals(playerName)) {
        return Optional.of(userInventory);
      }
    }
    return Optional.empty();
  }

  public List<UserInventory> getInventories() {
    return inventories;
  }

  public List<Deck> getDecks() {
    return decks;
  }

  public List<Card> getCards() {
    return cardField;
  }

  /*public List<TokenPile> getTokenPiles() {
    return tokenPiles.values()
                     .stream()
                     .toList();
  }*/

  /**
   * Retrieves the token piles on the game board except for the gold token pile.
   *
   * @return the token piles on the game board
   */
  public List<TokenPile> getTokenPilesNoGold() {
    return tokenPiles.values()
                     .stream()
                     .filter(tokens -> tokens.getType() != TokenType.GOLD)
                     .toList();
  }
  
  public EnumMap<TokenType, TokenPile> getTokenPiles() {
    return tokenPiles;
  }
  
  public List<Move> getMoveCache() {
    return moveCache;
  }

  /**
   * If there are gold tokens left.
   *
   * @return if there are no gold tokens left.
   */
  public boolean noGoldTokens() {
    return tokenPiles.get(TokenType.GOLD)
                     .getSize() == 0;
  }

  public List<Noble> getNobles() {
    return nobles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GameBoard gameBoard)) {
      return false;
    }
    return Objects.equals(inventories, gameBoard.inventories)
             && Objects.equals(decks, gameBoard.decks)
             && Objects.equals(cardField, gameBoard.cardField)
             && Objects.equals(tokenPiles, gameBoard.tokenPiles)
             && Objects.equals(nobles, gameBoard.nobles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inventories, decks, cardField, tokenPiles, nobles);
  }
}
