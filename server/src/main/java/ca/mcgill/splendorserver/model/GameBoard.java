package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorclient.model.users.User;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardStatus;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.DeckType;
import ca.mcgill.splendorserver.model.cards.OrientCard;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.nobles.NobleStatus;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.Power;
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
   * @param tradingPostSlots The trading post slots in the game
   * @param cities      The cities in the game
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
   * @return a possible bonus action
   */
  public Action applyMove(Move move, PlayerWrapper player) {
    assert move != null && player != null;
    // getting players inventory or throws exception if not there
    UserInventory inventory = getInventoryByPlayerName(player.getName()).orElseThrow(
        () -> new IllegalArgumentException(
            "player (" + player.getName() + ") wasn't found in this current game board"));

    Action pendingAction;
    switch (move.getAction()) {
      case PURCHASE_DEV -> {
        pendingAction = performPurchaseDev(move, player, inventory);
      }
      case PAIR_SPICE_CARD -> {
        pendingAction = performPairSpiceCard(move, inventory);
      }
      case CASCADE_LEVEL_1 -> {
        pendingAction = performCascadeLevelOne(move, inventory);
      }
      case CASCADE_LEVEL_2 -> {
        pendingAction = performCascadeLevelTwo(move, inventory);
      }
      default -> {
        pendingAction = null;
      }
    }
    if (pendingAction != null) {
      actionPending = pendingAction;
      return pendingAction;
    } else {
      return null;
    }
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

  /**
   * Returns a pending action.
   *
   * @return a pending action
   */
  public Action getPendingAction() {
    return actionPending;
  }

  /**
   * Returns possible end of turn actions.
   *
   * @param move the move that was just performed
   * @param inventory the current player's inventory
   * @return possible end of turn actions
   */
  public Action getEndOfTurnActions(Move move, UserInventory inventory) {
    List<Noble> nobles = new ArrayList<>();
    //TODO: add visiting reserved nobles
    for (Noble noble : nobles) {
      if (inventory.canBeVisitedByNoble(noble)) {
        moveCache.add(move);
        nobles.add(noble);
      }
    }
    if (nobles.size() == 1) {
      performClaimNobleAction(nobles.get(0), inventory);
      return Action.RECEIVE_NOBLE;
    }

    for (TradingPostSlot tradingPostSlot : tradingPostSlots) {
      if (inventory.canReceivePower(tradingPostSlot)) {
        moveCache.add(move);
        return Action.PLACE_COAT_OF_ARMS;
      }
    }
    return null;
  }

  /**
   * Ends the current player's turn.
   */
  public void endTurn() {
    moveCache.clear();
    actionPending = null;
    pendingAction = false;
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
              "If move is to take 3 gems of different colors and return %d token, "
                + "then selected gems needs to be of size 3 and returned gems of size %d",
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
              "If move is to take 2 gems of same color and return %d, "
                + "then gems to return needs to be of size %d",
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

    // all good, add the selected token to their inventory twice
    // we know there is only 1 element in this array
    inventory.addTokens(drawTokenByTokenType(move.getSelectedTokenTypes()
                                                 .get()[0]));
    inventory.addTokens(drawTokenByTokenType(move.getSelectedTokenTypes()
                                                 .get()[0]));
  }

  private void performTake1GemReturn(Move move, UserInventory inventory) {
    checkConditionsForTake1GemReturn(move);

    // all good, add the selected token, and return the token from inventory to the table
    // we know only 1 element in this array
    TokenType selected = move.getSelectedTokenTypes()
                           .get()[0];
    inventory.addTokens(drawTokenByTokenType(selected));
    // return token(s)
    selected = move.getReturnedTokenTypes().get()[0];
    returnTokensToBoardFromInventory(inventory, selected);
  }

  private void checkConditionsForTake1GemReturn(Move move) {
    if (move.getSelectedTokenTypes()
          .isEmpty() || move.getSelectedTokenTypes()
                          .get().length != 1) {
      throw new IllegalGameStateException(
        "If move is to take 1 gem, then gems needs to be of size 1");
    }

    // check that the token to return is not empty and proper size
    if (move.getReturnedTokenTypes()
          .isEmpty() || move.getReturnedTokenTypes()
                          .get().length != 1) {
      throw new IllegalGameStateException(
        String.format(
          "If move is to take 1 gem and return %d, "
            + "then gems to return needs to be of size %d",
          1, 1
        ));
    }
  }

  private void performTake1Gem(Move move, UserInventory inventory) {
    // if there are no token types then throw error
    if (move.getSelectedTokenTypes()
          .isEmpty()) {
      throw new IllegalGameStateException(
        "If move is to take 1 gem of same color, then gems cannot be empty");
    }

    // the length of the array should be 1, for the one token type which they're taking 2 of
    if (move.getSelectedTokenTypes()
          .get().length != 1) {
      throw new IllegalGameStateException(
        "Expected to see only one token type selected, instead found: "
          + move.getSelectedTokenTypes()
              .get().length);
    }

    // all good, add the selected token to their inventory
    // we know there is only 1 element in this array
    inventory.addTokens(drawTokenByTokenType(move.getSelectedTokenTypes()
                                               .get()[0]));
  }

  /**
   * Performs a reserve development type action routine.
   *
   * @param move      the move to perform
   * @param inventory the inventory to apply the move side effects to
   */
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

  /**
   * Performs a purchase development card action routine.
   *
   * @param move      the move to perform
   * @param player    the player performing the move
   * @param inventory the inventory to apply the move side effects to
   * @return any bonus actions that need to be performed
   */
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

  /**
   * Performs a purchase development card action routine.
   *
   * @param move      the move to perform
   * @param inventory the inventory to apply the move side effects to
   * @return any bonus actions that need to be performed
   */
  private Action performPairSpiceCard(Move move, UserInventory inventory) {
    if (move.getCard().isEmpty()) {
      throw new IllegalGameStateException(
        "If move to pair a spice card, then card cannot be empty");
    }
    if (!inventory.hasCard(move.getCard().get())) {
      throw new IllegalGameStateException(
        "If move to pair a spice card, then card has to be in user inventory");
    }
    OrientCard spiceCard = inventory.getUnpairedSpiceCard();
    if (spiceCard == null) {
      throw new IllegalGameStateException(
        "If move to pair a spice card, then an unpaired spice card has to be in user inventory");
    }
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
    return null;
  }

  /**
   * Performs a claim noble type action routine.
   *
   * @param noble      the noble to be claimed
   * @param inventory the inventory to apply the move side effects to
   */
  private void performClaimNobleAction(Noble noble, UserInventory inventory) {
    if (inventory.canBeVisitedByNoble(noble)) {
      // add prestige and tile to inventory
      inventory.receiveVisitFrom(noble);
    }
  }

  /**
   * Performs a place coat of arms type action routine.
   *
   * @param move      the move to perform
   * @param inventory the inventory to apply the move side effects to
   */
  private void performPlaceCoatOfArms(Move move, UserInventory inventory) {
    // See if the player has unlocked the power associated with this trading post slot
    if (move.getTradingPostSlot().isEmpty()) {
      throw new IllegalGameStateException("If move is to place coat of arms,"
                                            + "then trading post slot cannot be empty");
    }
    if (move.getTradingPostSlot().get().isFull()) {
      throw new IllegalGameStateException("If move is to place coat of arms,"
                                            + "then trading post slot cannot be full");
    }
    if (inventory.canReceivePower(move.getTradingPostSlot().get())) {
      inventory.addPower(move.getTradingPostSlot().get().getPower());
      move.getTradingPostSlot().get()
        .addCoatOfArms(inventory.getCoatOfArmsPile().removeCoatOfArms());
    }
  }

  /**
   * Performs reserving of noble action from Orient expansion bonus actions.
   *
   * @param move the move to perform
   * @param inventory the inventory to apply the move side effects to
   */
  private void performReserveNoble(Move move, UserInventory inventory) {
    if (move.getNoble().isEmpty()) {
      throw new IllegalGameStateException("If move to reserve noble, "
                                            + "then noble cannot be empty");
    }
    if (move.getNoble().get().getStatus() != NobleStatus.ON_BOARD) {
      throw new IllegalGameStateException(
              "Noble cannot be reserved if it has already been "
                      + "reserved or is currently visiting a player");
    }
    inventory.addReservedNoble(move.getNoble().get());
  }

  /**
   * Performs the cascade action with a level one Orient card for the bonus action.
   *
   * @param move the move to perform
   * @param inventory the inventory to apply the move side effects to
   */
  private Action performCascadeLevelOne(Move move, UserInventory inventory) {
    if (move.getCard().isEmpty()) {
      throw new IllegalGameStateException("If move is to choose a cascade level one card,"
                                            + "then card cannot be empty");
    }
    if (move.getCard().get().getDeckType() != DeckType.ORIENT1) {
      throw new IllegalGameStateException("If move is to choose a cascade level one card,"
                                            + "then card must be from Orient 1 deck");
    }
    if (move.getCard().get().getCardStatus() != CardStatus.NONE) {
      throw new IllegalGameStateException(
        "Card cannot be taken if it has already been "
          + "reserved or purchased by a player");
    }

    OrientCard levelOneCard = (OrientCard) move.getCard().get();
    inventory.addCascadeLevelOne(levelOneCard);

    if (levelOneCard instanceof OrientCard) {
      List<Action> actions = ((OrientCard) levelOneCard).getBonusActions();
      if (actions.size() > 0) {
        moveCache.add(move);
        return actions.get(0);
      }
    }
    return null;
  }

  /**
   * Performs the cascade action with a level two Orient card for the bonus action.
   *
   * @param move the move to perform
   * @param inventory the inventory to apply the move side effects to
   */
  private Action performCascadeLevelTwo(Move move, UserInventory inventory) {
    if (move.getCard().isEmpty()) {
      throw new IllegalGameStateException("If move is to choose a cascade level two card,"
                                            + "then card cannot be empty");
    }
    if (move.getCard().get().getDeckType() != DeckType.ORIENT2) {
      throw new IllegalGameStateException("If move is to choose a cascade level two card,"
                                            + "then card must be from Orient 2 deck");
    }
    if (move.getCard().get().getCardStatus() != CardStatus.NONE) {
      throw new IllegalGameStateException(
        "Card cannot be taken if it has already been "
          + "reserved or purchased by a player");
    }

    OrientCard levelTwoCard = (OrientCard) move.getCard().get();
    inventory.addCascadeLevelTwo(levelTwoCard);

    if (levelTwoCard instanceof OrientCard) {
      List<Action> actions = ((OrientCard) levelTwoCard).getBonusActions();
      if (actions.size() > 0) {
        moveCache.add(move);
        return actions.get(0);
      }
    }
    return null;
  }

  /**
   * Draws a card from the top of the deck and adds it to the game board.
   * This is used after a card is purchased or reserved.
   *
   * @param deckType the type of deck to deal from
   * @param replenishIndex the index of the card that was removed from the game board
   */
  private void replenishTakenCardFromDeck(DeckType deckType, int replenishIndex) {
    // empty card field means we cant replenish because otherwise it would have been replenished alr
    for (Deck deck : decks) {
      if (deck.getType() == deckType && !deck.isEmpty()) {
        // we know draw is ok cause of the check for emptiness above
        cardField.add(replenishIndex, deck.draw());
      }
    }
  }

  /**
   * Draws tokens from the token bank and adds them to the user inventory.
   *
   * @param inventory the current player's inventory
   * @param selected the types of tokens selected
   */
  private void moveTokensToUserInventory(UserInventory inventory, TokenType[] selected) {
    for (TokenType tokenType : selected) {
      inventory.addTokens(drawTokenByTokenType(tokenType));
    }
  }

  /**
   * Returns tokens from the user inventory and adds them to the token bank.
   *
   * @param inventory the current player's inventory
   * @param selected the types of tokens selected
   */
  private void returnTokensToBoardFromInventory(UserInventory inventory, TokenType... selected) {
    for (TokenType tokenType : selected) {
      tokenPiles.get(tokenType)
                .addToken(inventory.removeTokenByTokenType(tokenType));
    }
  }

  /**
   * Returns tokens from the user inventory and adds them to the token bank.
   * Used when purchasing cards.
   *
   * @param tokens the returned tokens
   */
  private void returnTokensToBoard(List<Token> tokens) {
    for (Token token : tokens) {
      tokenPiles.get(token.getType())
                .addToken(token);
    }
  }

  /**
   * Draws a card from the top of the deck with the specified deck level.
   *
   * @param deckLevel the level of the deck
   * @return the card from the top of the deck
   */
  private Card getCardByDeckLevel(DeckType deckLevel) {
    assert deckLevel != null;
    for (Deck deck : decks) {
      if (deck.getType() == deckLevel) {
        if (!deck.isEmpty()) {
          return deck.draw();
        }
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

  /**
   * Returns the list of inventories in the game board.
   *
   * @return the list of inventories in the game board
   */
  public List<UserInventory> getInventories() {
    return inventories;
  }

  /**
   * Returns the list of decks in the game board.
   *
   * @return the list of decks in the game board
   */
  public List<Deck> getDecks() {
    return decks;
  }

  /**
   * Returns the list of cards dealt onto the game board.
   *
   * @return the list of cards dealt onto the game board
   */
  public List<Card> getCards() {
    return cardField;
  }

  /**
   * Returns the list of trading posts in the game board.
   *
   * @return the list of trading posts in the game board
   */
  public List<TradingPostSlot> getTradingPostSlots() {
    return tradingPostSlots;
  }

  /**
   * Returns the list of cities in the game board.
   *
   * @return the list of cities in the game board
   */
  public List<City> getCities() {
    return cities;
  }


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

  /**
   * Returns the map of token piles in the game board.
   *
   * @return the map of token piles in the game board
   */
  public EnumMap<TokenType, TokenPile> getTokenPiles() {
    return tokenPiles;
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

  /**
   * Returns the list of nobles in the game board.
   *
   * @return the list of nobles in the game board
   */
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
