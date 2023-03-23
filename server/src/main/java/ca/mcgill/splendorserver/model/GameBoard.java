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
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArms;
import ca.mcgill.splendorserver.model.tradingposts.Power;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map.Entry;
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
    this.tradingPostSlots = tradingPostSlots;
    this.cities = cities;
  }

  /**
   * Returns the move cache for pending actions.
   *
   * @return the move cache for pending actions
   */
  public List<Move> getMoveCache() {
    return moveCache;
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
      case PURCHASE_DEV:
        pendingAction = performPurchaseDev(move, player, inventory);
        if (pendingAction != null) {
          actionPending = pendingAction;
          return pendingAction;
        } else {
          return null;
        }
      case RESERVE_DEV:
        pendingAction = performReserveDev(move, inventory);
        return null;
      case PAIR_SPICE_CARD:
        pendingAction = performPairSpiceCard(move, inventory);
        if (pendingAction != null) {
          actionPending = pendingAction;
          return pendingAction;
        } else {
          return null;
        }
      case CASCADE_LEVEL_1:
        pendingAction = performCascadeLevelOne(move, inventory);
        if (pendingAction != null) {
          actionPending = pendingAction;
          return pendingAction;
        } else {
          return null;
        }
      case CASCADE_LEVEL_2:
        pendingAction = performCascadeLevelTwo(move, inventory);
        if (pendingAction != null) {
          actionPending = pendingAction;
          return pendingAction;
        } else {
          return null;
        }
      case RESERVE_NOBLE:
        pendingAction = performReserveNoble(move, inventory);
        if (pendingAction != null) {
          actionPending = pendingAction;
          return pendingAction;
        } else {
          return null;
        }
      case TAKE_TOKEN:
        performTakeToken(move, inventory);
        if (moveCache.isEmpty()) {
          moveCache.add(move);
          actionPending = Action.TAKE_TOKEN;
          return actionPending;
        } else if (moveCache.size() == 1) {
          //selected two of the same token
          if (moveCache.get(0).getSelectedTokenTypes() == move.getSelectedTokenTypes()) {
            moveCache.clear();
            actionPending = null;
            return null;
          } else {
            moveCache.add(move);
            actionPending = Action.TAKE_TOKEN;
            return actionPending;
          }
        } else {
          moveCache.clear();
          actionPending = null;
          return null;
        }
      case DISCARD_FIRST_WHITE_CARD:
      case DISCARD_FIRST_BLUE_CARD:
      case DISCARD_FIRST_GREEN_CARD:
      case DISCARD_FIRST_RED_CARD:
      case DISCARD_FIRST_BLACK_CARD:
        pendingAction = performDiscardFirstCard(move, inventory);
        if (pendingAction != null) {
          actionPending = pendingAction;
          return pendingAction;
        } else {
          return null;
        }
      case DISCARD_SECOND_WHITE_CARD:
      case DISCARD_SECOND_BLUE_CARD:
      case DISCARD_SECOND_GREEN_CARD:
      case DISCARD_SECOND_RED_CARD:
      case DISCARD_SECOND_BLACK_CARD:
        pendingAction = performDiscardSecondCard(move, inventory);
        if (pendingAction != null) {
          actionPending = pendingAction;
          return pendingAction;
        } else {
          return null;
        }
      case RET_TOKEN:
        performReturnToken(move, inventory);
        if (requiresReturnTokens(inventory, move)) {
          //i think this would get swooped up in endOfTurnActions, so maybe this is redundant.
          moveCache.add(move);
          actionPending = Action.RET_TOKEN;
          return actionPending;
        }
        return null;
      default:
        return null;
    }
  }

  /**
   * Returns a pending action.
   *
   * @return a pending action
   */
  public Action getPendingAction() {
    return actionPending;
  }
  
  private void performReturnToken(Move move, UserInventory inventory) {
    TokenType type = move.getSelectedTokenTypes();
    Token token = inventory.removeTokenByTokenType(type);
    this.tokenPiles.get(type).addToken(token);
  }
  
  private void performTakeToken(Move move, UserInventory inventory) {
    TokenType type = move.getSelectedTokenTypes();
    TokenPile pile = this.tokenPiles.get(type);
    Token token = pile.removeToken();
    inventory.addToken(token);
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

    for (Noble noble : this.nobles) {
      if (inventory.canBeVisitedByNoble(noble)) {
        moveCache.add(move);
        nobles.add(noble);
      }
    }
    for (Noble noble : inventory.getNobles()) {
      if (inventory.canBeVisitedByNoble(noble)) {
        moveCache.add(move);
        nobles.add(noble);
      }
    }
    if (nobles.size() == 1) {
      performClaimNobleAction(nobles.get(0), inventory);
      actionPending = Action.RECEIVE_NOBLE;
      return Action.RECEIVE_NOBLE;
    }

    for (TradingPostSlot tradingPostSlot : tradingPostSlots) {
      if (inventory.canReceivePower(tradingPostSlot)) {
        moveCache.add(move);
        performPlaceCoatOfArms(tradingPostSlot, inventory);
        return Action.PLACE_COAT_OF_ARMS;
      }
    }

    List<City> cities = new ArrayList<>();

    for (City city : this.cities) {
      if (inventory.canReceiveCity(city)) {
        moveCache.add(move);
        cities.add(city);
      }
    }
    if (cities.size() == 1) {
      performClaimCityAction(cities.get(0), inventory);
      actionPending = Action.RECEIVE_CITY;
      return Action.RECEIVE_CITY;
    }

    return requiresReturnTokens(inventory, move) ? Action.RET_TOKEN : null;
  }
  
  private boolean requiresReturnTokens(UserInventory inventory, Move move) {
    int ntokens = 0;
    for (Entry<TokenType, TokenPile> entry : inventory.getTokenPiles().entrySet()) {
      ntokens += entry.getValue().getSize();
    }
    if (ntokens >= 10) {
      moveCache.add(move);
      actionPending = Action.RET_TOKEN;
      return true;
    }
    return false;
  }

  /**
   * Ends the current player's turn.
   */
  public void endTurn() {
    moveCache.clear();
    actionPending = null;
  }

  /**
   * Performs a reserve development type action routine.
   *
   * @param move      the move to perform
   * @param inventory the inventory to apply the move side effects to
   */
  private Action performReserveDev(Move move, UserInventory inventory) {
    // no gold token (joker) will be received, just the reserved card
    Card selectedCard = move.getCard();
    // if we're taking from the table, replenish table from the deck
    if (move.getCard() != null) {
      // remove the selected card from the board and replenish from same deck type
      int ix = cardField.indexOf(selectedCard);
      cardField.remove(selectedCard);
      replenishTakenCardFromDeck(
          move.getCard()
              .getDeckType(),
          ix
      );
    }
    if (inventory.tokenCount() < 10 && !noGoldTokens()) {
      Token gold = this.tokenPiles.get(TokenType.GOLD).removeToken();
      inventory.addToken(gold);
    }
    // add to inventory as reserved card now
    inventory.addReservedCard(selectedCard);
    return null;
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
    Card selectedCard = move.getCard();
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
        if (actions.get(0) == Action.DISCARD_FIRST_WHITE_CARD
              || actions.get(0) == Action.DISCARD_FIRST_BLUE_CARD
              || actions.get(0) == Action.DISCARD_FIRST_GREEN_CARD
              || actions.get(0) == Action.DISCARD_FIRST_RED_CARD
              || actions.get(0) == Action.DISCARD_FIRST_BLACK_CARD) {
          int spiceCount = inventory.getNumSpiceCardsByType(move.getCard().getTokenBonusType());
          if (spiceCount >= 2) {
            inventory.discardSpiceCard(move.getCard().getTokenBonusType());
            inventory.discardSpiceCard(move.getCard().getTokenBonusType());
          } else if (spiceCount == 1) {
            inventory.discardSpiceCard(move.getCard().getTokenBonusType());
            return actions.get(1);
          } else if (spiceCount == 0) {
            moveCache.add(move);
            return actions.get(0);
          }
        } else {
          moveCache.add(move);
          return actions.get(0);
        }
      }
    }
    for (Noble noble : inventory.getNobles()) {
      if (inventory.canBeVisitedByNoble(noble)) {
        moveCache.add(move);
        return Action.RECEIVE_NOBLE;
      }
    }
    for (Noble noble : nobles) {
      if (inventory.canBeVisitedByNoble(noble)) {
        moveCache.add(move);
        return Action.RECEIVE_NOBLE;
      }
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
   * Performs a purchase development card action routine.
   *
   * @param move      the move to perform
   * @param inventory the inventory to apply the move side effects to
   * @return any bonus actions that need to be performed
   */
  private Action performPairSpiceCard(Move move, UserInventory inventory) {
    if (move.getCard() == null) {
      throw new IllegalGameStateException(
        "If move to pair a spice card, then card cannot be empty");
    }
    if (!inventory.hasCard(move.getCard())) {
      throw new IllegalGameStateException(
        "If move to pair a spice card, then card has to be in user inventory");
    }
    OrientCard spiceCard = inventory.getUnpairedSpiceCard();
    if (spiceCard == null) {
      throw new IllegalGameStateException(
        "If move to pair a spice card, then an unpaired spice card has to be in user inventory");
    }
    ((OrientCard) spiceCard).pairWithCard(move.getCard());
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
   * @param noble     the noble to claim
   * @param inventory the inventory to apply the move side effects to
   */
  private void performClaimNobleAction(Noble noble, UserInventory inventory) {
    if (inventory.canBeVisitedByNoble(noble)) {
      inventory.receiveVisitFrom(noble);
      nobles.remove(noble);
    }
  }

  /**
   * Performs a claim city type action routine.
   *
   * @param city     the city to claim
   * @param inventory the inventory to apply the move side effects to
   */
  private void performClaimCityAction(City city, UserInventory inventory) {
    if (inventory.canReceiveCity(city)) {
      inventory.addCity(city);
      cities.remove(city);
    }
  }

  /**
   * Performs a place coat of arms type action routine.
   *
   * @param tradingPostSlot the trading post slot to unlock
   * @param inventory the inventory to apply the move side effects to
   */
  private void performPlaceCoatOfArms(TradingPostSlot tradingPostSlot, UserInventory inventory) {
    assert tradingPostSlot != null;
    if (tradingPostSlot.isFull()) {
      throw new IllegalGameStateException("If move is to place coat of arms,"
                                            + "then trading post slot cannot be full");
    }
    if (inventory.canReceivePower(tradingPostSlot)) {
      inventory.addPower(tradingPostSlot.getPower());
      tradingPostSlot.addCoatOfArms(inventory.getCoatOfArmsPile().removeCoatOfArms());
    }
  }

  /**
   * Performs reserving of noble action from Orient expansion bonus actions.
   *
   * @param move the move to perform
   * @param inventory the inventory to apply the move side effects to
   */
  private Action performReserveNoble(Move move, UserInventory inventory) {
    if (move.getNoble() == null) {
      throw new IllegalGameStateException("If move is to reserve noble, "
                                            + "then noble cannot be empty");
    }
    if (move.getNoble().getStatus() != NobleStatus.ON_BOARD) {
      throw new IllegalGameStateException(
              "Noble cannot be reserved if it has already been "
                      + "reserved or is currently visiting a player");
    }
    inventory.addReservedNoble(move.getNoble());

    return null;
  }

  /**
   * Performs a discard card type action routine.
   *
   * @param move the move to be performed
   * @param inventory the inventory to apply the move side effects to
   */
  private Action performDiscardSecondCard(Move move, UserInventory inventory) {
    if (move.getCard() == null) {
      throw new IllegalGameStateException("If move is to discard card, "
                                            + "then card cannot be empty");
    }

    inventory.discardCard(move.getCard());

    for (Noble noble : inventory.getNobles()) {
      if (noble.getStatus() == NobleStatus.VISITING && !inventory.canBeVisitedByNoble(noble)) {
        nobles.add(inventory.removeNoble(noble));
      }
    }
    for (TradingPostSlot tradingPostSlot : tradingPostSlots) {
      if (inventory.hasPower(tradingPostSlot.getPower())
            && !inventory.canReceivePower(tradingPostSlot)) {
        inventory.removePower(tradingPostSlot.getPower());
        CoatOfArms coatOfArms =
            tradingPostSlot.removeCoatOfArms(inventory.getCoatOfArmsPile().getType());
        inventory.getCoatOfArmsPile().addCoatOfArms(coatOfArms);
      }
    }
    return null;
  }

  /**
   * Performs a discard card type action routine.
   *
   * @param move the move to be performed
   * @param inventory the inventory to apply the move side effects to
   */
  private Action performDiscardFirstCard(Move move, UserInventory inventory) {
    if (move.getCard() == null) {
      throw new IllegalGameStateException("If move is to discard card, "
                                            + "then card cannot be empty");
    }

    inventory.discardCard(move.getCard());

    for (Noble noble : inventory.getNobles()) {
      if (noble.getStatus() == NobleStatus.VISITING && !inventory.canBeVisitedByNoble(noble)) {
        nobles.add(inventory.removeNoble(noble));
      }
    }
    for (TradingPostSlot tradingPostSlot : tradingPostSlots) {
      if (inventory.hasPower(tradingPostSlot.getPower())
              && !inventory.canReceivePower(tradingPostSlot)) {
        inventory.removePower(tradingPostSlot.getPower());
        CoatOfArms coatOfArms =
            tradingPostSlot.removeCoatOfArms(inventory.getCoatOfArmsPile().getType());
        inventory.getCoatOfArmsPile().addCoatOfArms(coatOfArms);
      }
    }

    switch (move.getAction()) {
      case DISCARD_FIRST_WHITE_CARD -> {
        return Action.DISCARD_SECOND_WHITE_CARD;
      }
      case DISCARD_FIRST_BLUE_CARD -> {
        return Action.DISCARD_SECOND_BLUE_CARD;
      }
      case DISCARD_FIRST_GREEN_CARD -> {
        return Action.DISCARD_SECOND_GREEN_CARD;
      }
      case DISCARD_FIRST_RED_CARD -> {
        return Action.DISCARD_SECOND_RED_CARD;
      }
      case DISCARD_FIRST_BLACK_CARD -> {
        return Action.DISCARD_SECOND_BLACK_CARD;
      }
      default -> {
        return null;
      }
    }
  }

  /**
   * Performs the cascade action with a level one Orient card for the bonus action.
   *
   * @param move the move to perform
   * @param inventory the inventory to apply the move side effects to
   */
  private Action performCascadeLevelOne(Move move, UserInventory inventory) {
    if (move.getCard() == null) {
      throw new IllegalGameStateException("If move is to choose a cascade level one card,"
                                            + "then card cannot be empty");
    }
    if (move.getCard().getDeckType() != DeckType.ORIENT1) {
      throw new IllegalGameStateException("If move is to choose a cascade level one card,"
                                            + "then card must be from Orient 1 deck");
    }
    if (move.getCard().getCardStatus() != CardStatus.NONE) {
      throw new IllegalGameStateException(
        "Card cannot be taken if it has already been "
          + "reserved or purchased by a player");
    }

    OrientCard levelOneCard = (OrientCard) move.getCard();
    inventory.addCascadeLevelOne(levelOneCard);
    int ix = cardField.indexOf(levelOneCard);
    returnTokensToBoard(inventory.purchaseCard(cardField.remove(ix)));
    replenishTakenCardFromDeck(levelOneCard.getDeckType(), ix);

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
    if (move.getCard() == null) {
      throw new IllegalGameStateException("If move is to choose a cascade level two card,"
                                            + "then card cannot be empty");
    }
    if (move.getCard().getDeckType() != DeckType.ORIENT2) {
      throw new IllegalGameStateException("If move is to choose a cascade level two card,"
                                            + "then card must be from Orient 2 deck");
    }
    if (move.getCard().getCardStatus() != CardStatus.NONE) {
      throw new IllegalGameStateException(
        "Card cannot be taken if it has already been "
          + "reserved or purchased by a player");
    }

    OrientCard levelTwoCard = (OrientCard) move.getCard();
    inventory.addCascadeLevelTwo(levelTwoCard);
    int ix = cardField.indexOf(levelTwoCard);
    returnTokensToBoard(inventory.purchaseCard(cardField.remove(ix)));
    replenishTakenCardFromDeck(levelTwoCard.getDeckType(), ix);

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
