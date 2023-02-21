package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.DeckType;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

/**
 * Model of the gameboard. Necessary due to permanence requirement.
 * Only ever used for initial setup, the rest can be taken care of by the actions.
 *
 * @author lawrenceberardelli
 */
public class GameBoard {

  private final List<UserInventory>           inventories;
  private final List<Deck>                    decks;
  private final List<Card>                    cardField;
  private final EnumMap<TokenType, TokenPile> tokenPiles;
  private final List<Noble>                   nobles;
  private final Logger                        logger = Logger.getAnonymousLogger();


  /*
  The following fields are used for actions that require an additional decision
  following the action itself (compound action / move). For example: choosing which tokens to return, choosing
  which noble to be visited by in case there are more than 1.
  moveCache := move which involved a compound action and is currently being waited on
  pendingAction := if the game is waiting for an action to be made
   */
  private Optional<Action> actionCache;
  private boolean          pendingAction;
  private List<TradingPostSlot> tradingPostSlots;

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
                   List<TokenPile> tokenPiles, List<Noble> nobles
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
    this.actionCache   = Optional.empty();
  }

  /**
   * Applies the given move to this game board model. Note that we can assume that for any move that
   * requires drawing from a deck, that the deck will not be empty otherwise it wouldn't have been
   * offered as a valid move.
   *
   * @param move   selected move, must be valid move, cannot be null
   * @param player player whose turn it is, cannot be null
   */
  public HttpStatus applyMove(Move move, PlayerWrapper player) {
    assert move != null && player != null;
    // getting players inventory or throws exception if not there
    UserInventory inventory = getInventoryByPlayerName(player.getName()).orElseThrow(
        () -> new IllegalArgumentException(
            "player (" + player.getName() + ") wasn't found in this current game board"));

    return switch (move.getAction()) {
      case PURCHASE_DEV -> {
        performPurchaseDev(move, player, inventory);
        yield HttpStatus.OK;
      }
      case PURCHASE_DEV_RECEIVE_NOBLE -> {
        // check if we're waiting for this or not
        if (waitingForAction(Action.PURCHASE_DEV_RECEIVE_NOBLE)) {
          performPurchaseDev(move, player, inventory);
          performClaimNobleAction(move, inventory);
          unCacheAction();
          yield HttpStatus.OK;
        } else {
          // this is compound action, request further actions
          cacheAction(Action.PURCHASE_DEV_RECEIVE_NOBLE);
          yield HttpStatus.PARTIAL_CONTENT;
        }
      }
      case RESERVE_DEV -> {
        performReserveDev(move, inventory);
        yield HttpStatus.OK;
      }
      case RESERVE_DEV_TAKE_JOKER -> {
        // gold token added and card reserved
        performReserveDev(move, inventory);
        inventory.addTokens(drawGoldToken());
        yield HttpStatus.OK;
      }
      case TAKE_2_GEM_TOKENS_SAME_COL -> {
        performTake2GemsSameColor(move, inventory);
        yield HttpStatus.OK;
      }
      case TAKE_2_GEM_TOKENS_SAME_COL_RET_1 -> {
        if (waitingForAction(Action.TAKE_2_GEM_TOKENS_SAME_COL_RET_1)) {
          performTake2GemsSameColorReturn(move, inventory, 1);
          unCacheAction();
          yield HttpStatus.OK;
        } else {
          cacheAction(Action.TAKE_2_GEM_TOKENS_SAME_COL_RET_1);
          yield HttpStatus.PARTIAL_CONTENT;
        }
      }
      case TAKE_2_GEM_TOKENS_SAME_COL_RET_2 -> {
        if (waitingForAction(Action.TAKE_2_GEM_TOKENS_SAME_COL_RET_2)) {
          performTake2GemsSameColorReturn(move, inventory, 2);
          unCacheAction();
          yield HttpStatus.OK;
        } else {
          cacheAction(Action.TAKE_2_GEM_TOKENS_SAME_COL_RET_2);
          yield HttpStatus.PARTIAL_CONTENT;
        }
      }
      case TAKE_3_GEM_TOKENS_DIFF_COL -> {
        performTake3GemsDiffColor(move, inventory);
        yield HttpStatus.OK;
      }
      case TAKE_3_GEM_TOKENS_DIFF_COL_RET_1 -> {
        if (waitingForAction(Action.TAKE_3_GEM_TOKENS_DIFF_COL_RET_1)) {
          performTake3GemsDiffColorReturn(move, inventory, 1);
          unCacheAction();
          yield HttpStatus.OK;
        } else {
          cacheAction(Action.TAKE_3_GEM_TOKENS_DIFF_COL_RET_1);
          yield HttpStatus.PARTIAL_CONTENT;
        }
      }
      case TAKE_3_GEM_TOKENS_DIFF_COL_RET_2 -> {
        if (waitingForAction(Action.TAKE_3_GEM_TOKENS_DIFF_COL_RET_2)) {
          performTake3GemsDiffColorReturn(move, inventory, 2);
          unCacheAction();
          yield HttpStatus.OK;
        } else {
          cacheAction(Action.TAKE_3_GEM_TOKENS_DIFF_COL_RET_2);
          yield HttpStatus.PARTIAL_CONTENT;
        }
      }
      case TAKE_3_GEM_TOKENS_DIFF_COL_RET_3 -> {
        if (waitingForAction(Action.TAKE_3_GEM_TOKENS_DIFF_COL_RET_3)) {
          performTake3GemsDiffColorReturn(move, inventory, 3);
          unCacheAction();
          yield HttpStatus.OK;
        } else {
          cacheAction(Action.TAKE_3_GEM_TOKENS_DIFF_COL_RET_3);
          yield HttpStatus.PARTIAL_CONTENT;
        }
      }
    };
  }

  private boolean waitingForAction(Action action) {
    return pendingAction && actionCache.isPresent() && actionCache.get() == action;
  }

  private void cacheAction(Action action) {
    this.actionCache   = Optional.of(action);
    this.pendingAction = true;
  }

  private void unCacheAction() {
    this.actionCache   = Optional.empty();
    this.pendingAction = false;
  }

  private void setPendingAction(boolean isPending) {
    pendingAction = isPending;
  }

  private void performTake3GemsDiffColorReturn(Move move, UserInventory inventory, int n) {
    // check that tokens selected is not empty
    if (move.getSelectedTokenTypes()
            .isEmpty() || move.getSelectedTokenTypes()
                              .get().length != n) {
      throw new IllegalGameStateException(
          "If move is to take 3 gems of different colors, then gems needs to be of size 3");
    }

    // check that the return token is selected
    if (move.getReturnedTokenTypes()
            .isEmpty() || move.getReturnedTokenTypes()
                              .get().length != n) {
      throw new IllegalGameStateException(
          String.format(
              "If move is to take 3 gems of different colors and return %d token, then selected gems needs to be of size 3 and returned gems of size %d",
              n, n
          ));
    }

    // all good, add the selected tokens
    TokenType[] selected = move.getSelectedTokenTypes()
                               .get();
    moveTokensToUserInventory(inventory, selected);

    // return the selected
    returnTokensToBoardFromInventory(inventory, move.getReturnedTokenTypes()
                                                    .get());

  }

  private void performTake3GemsDiffColor(Move move, UserInventory inventory) {
    // check that tokens selected is not empty
    if (move.getSelectedTokenTypes()
            .isEmpty() || move.getSelectedTokenTypes()
                              .get().length != 3) {
      throw new IllegalGameStateException(
          "If move is to take 3 gems of different colors, then gems needs to be of size 3");
    }

    // all good, add the selected tokens
    TokenType[] selected = move.getSelectedTokenTypes()
                               .get();
    moveTokensToUserInventory(inventory, selected);
  }

  /**
   * Performs take 2 gems of same color and return n tokens action routine.
   *
   * @param move      the move to perform
   * @param inventory the inventory to apply the move side effects to
   * @param n         number of tokens there are in the action to return.
   */
  private void performTake2GemsSameColorReturn(Move move, UserInventory inventory, int n) {
    checkConditionsForTake2GemsSameColorReturn(move, n);

    // all good, add the selected token twice, and return the token from inventory to the table
    // we know only 1 element in this array
    TokenType selected = move.getSelectedTokenTypes()
                             .get()[0];
    inventory.addTokens(drawTokenByTokenType(selected));
    inventory.addTokens(drawTokenByTokenType(selected));
    // return token(s)
    for (int i = 0; i < n; i++) {
      selected = move.getReturnedTokenTypes()
                     .get()[i];
      returnTokensToBoardFromInventory(inventory, selected);
    }
  }

  private void checkConditionsForTake2GemsSameColorReturn(Move move, int n) {
    if (move.getSelectedTokenTypes()
            .isEmpty() || move.getSelectedTokenTypes()
                              .get().length != 1) {
      throw new IllegalGameStateException(
          "If move is to take 2 gems of same color, then gems needs to be of size 1");
    }

    // check that the token to return is not empty and proper size
    if (move.getReturnedTokenTypes()
            .isEmpty() || move.getReturnedTokenTypes()
                              .get().length != n) {
      throw new IllegalGameStateException(
          String.format(
              "If move is to take 2 gems of same color and return %d, then gems to return needs to be of size %d",
              n, n
          ));
    }
  }

  private void performTake2GemsSameColor(Move move, UserInventory inventory) {
    // if there are no token types then throw error
    if (move.getSelectedTokenTypes()
            .isEmpty()) {
      throw new IllegalGameStateException(
          "If move is to take 2 gems of same color, then gems cannot be empty");
    }

    // the length of the array should be 1, for the one token type which they're taking 2 of
    if (move.getSelectedTokenTypes()
            .get().length != 1) {
      throw new IllegalGameStateException(
          "Expected to see only one token type selected, instead found: "
              + move.getSelectedTokenTypes()
                    .get().length);
    }

    // all good, add the selected tokens to their inventory twice
    // we know there is only 1 element in this array
    inventory.addTokens(drawTokenByTokenType(move.getSelectedTokenTypes()
                                                 .get()[0]));
    inventory.addTokens(drawTokenByTokenType(move.getSelectedTokenTypes()
                                                 .get()[0]));
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
                                                                   "If move is to reserve dev card, a card or deck level to draw a card from must be chosen"))));
    return selectedCard;
  }

  private void performPurchaseDev(Move move, PlayerWrapper player, UserInventory inventory) {
    // checking to see whether they're buying from reserved card in hand / table or from deck
    Card selectedCard = move.getCard()
                            .orElseThrow(() -> new IllegalGameStateException(
                                "if selected move is to purchase a dev card, there needs to be a card selected"));
    // check to make sure that the card they wish to purchase from their hand is valid
    if (inventory.hasCardReserved(selectedCard)) {
      // they have the card, and it has been reserved, so they can legally buy it
      // add the card to their inventory as a purchased card
      // return the required tokens to purchase the card from user to board
      returnTokensToBoard(inventory.purchaseCard(selectedCard));
      logger.log(Level.INFO, player + " purchased a reserved dev card: " + selectedCard);
    } else if (cardField.contains(selectedCard)) { // purchase face-up dev card
      // purchase card which is face-up on the board
      // purchase card, take it from the face up table and replace that card on table
      int ix = cardField.indexOf(selectedCard);
      returnTokensToBoard(inventory.purchaseCard(cardField.remove(ix)));
      replenishTakenCardFromDeck(selectedCard.getDeckType(), ix);

      logger.log(
          Level.INFO,
          player + " purchased a face-up dev card from game board: " + selectedCard
      );
    } else {
      // cannot purchase card if it's not reserved in hand or face-up
      logger.log(
          Level.SEVERE,
          "A card has been attempted to be purchased but it wasn't reserved in inventory nor face-up on game board"
      );
      throw new IllegalGameStateException(
          "Cannot purchase card which isn't reserved or face-up");
    }

    performClaimNobleAction(move, inventory);

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

  public List<TokenPile> getTokenPiles() {
    return tokenPiles.values()
                     .stream()
                     .toList();
  }

  public List<TokenPile> getTokenPilesNoGold() {
    return tokenPiles.values()
                     .stream()
                     .filter(tokens -> tokens.getType() != TokenType.GOLD)
                     .toList();
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
    return Objects.equals(inventories, gameBoard.inventories) &&
        Objects.equals(decks, gameBoard.decks) && Objects.equals(cardField, gameBoard.cardField) &&
        Objects.equals(tokenPiles, gameBoard.tokenPiles) &&
        Objects.equals(nobles, gameBoard.nobles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inventories, decks, cardField, tokenPiles, nobles);
  }
}
