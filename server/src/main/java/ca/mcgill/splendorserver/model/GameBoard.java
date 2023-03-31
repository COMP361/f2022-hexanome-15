package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cards.CardStatus;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.DeckType;
import ca.mcgill.splendorserver.model.cards.OrientCard;
import ca.mcgill.splendorserver.model.cards.TokenBonusAmount;
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
  private boolean unlockedNoble;
  private boolean unlockedCity;

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
    this.unlockedNoble = false;
    this.unlockedCity = false;
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
    moveCache.add(move);
    switch (move.getAction()) {
      case PURCHASE_DEV:
        pendingAction = performPurchaseDev(move, player, inventory);
        if (pendingAction != null) {
          actionPending = pendingAction;
          return pendingAction;
        } else {
          if (inventory.hasPower(Power.PURCHASE_CARD_TAKE_TOKEN)) {
            actionPending = Action.TAKE_EXTRA_TOKEN;
            return actionPending;
          } else {
            return null;
          }
        }
      case RESERVE_DEV:
        performReserveDev(move, inventory);
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
        performReserveNoble(move, inventory);
        return null;
      case TAKE_TOKEN:
        performTakeToken(move, inventory);
        if (moveCache.size() == 1) {
          actionPending = Action.TAKE_TOKEN;
          return actionPending;
        } else if (moveCache.size() == 2) {
          //selected two of the same token
          if (moveCache.get(0).getSelectedTokenTypes() != null) {
            if (moveCache.get(0).getSelectedTokenTypes() == move.getSelectedTokenTypes()) {
              if (inventory.hasPower(Power.TAKE_2_GEMS_SAME_COL_AND_TAKE_1_GEM_DIF_COL)) {
                actionPending = Action.TAKE_EXTRA_TOKEN;
                return actionPending;
              }
              actionPending = null;
              return null;
            } else {
              actionPending = Action.TAKE_TOKEN;
              return actionPending;
            }
          } else {
            actionPending = null;
            return null;
          }
        } else {
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
        performDiscardSecondCard(move, inventory);
        return null;
      case RET_TOKEN:
        performReturnToken(move, inventory);
        if (requiresReturnTokens(inventory, move)) {
          actionPending = Action.RET_TOKEN;
          return actionPending;
        }
        return null;
      case RECEIVE_NOBLE:
        if (move.getNoble() != null) {
          performClaimNobleAction(move.getNoble(), inventory);
          unlockedNoble = true;
        }
        return null;
      case TAKE_EXTRA_TOKEN:
        performTakeToken(move, inventory);
        return null;
      case RECEIVE_CITY:
        if (move.getCity() != null) {
          performClaimCityAction(move.getCity(), inventory);
          unlockedCity = true;
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

    if (!unlockedNoble) {
      List<Noble> candidateNobles = new ArrayList<>();
      for (Noble noble : nobles) {
        if (inventory.canBeVisitedByNoble(noble) && noble.getStatus() == NobleStatus.ON_BOARD) {
          candidateNobles.add(noble);
        }
      }
      for (Noble noble : inventory.getNobles()) {
        if (inventory.canBeVisitedByNoble(noble) && noble.getStatus() == NobleStatus.RESERVED) {
          candidateNobles.add(noble);
        }
      }
      if (candidateNobles.size() == 1) {
        performClaimNobleAction(nobles.get(0), inventory);
      } else if (candidateNobles.size() > 1) {
        actionPending = Action.RECEIVE_NOBLE;
        return Action.RECEIVE_NOBLE;
      }
    }

    for (TradingPostSlot tradingPostSlot : tradingPostSlots) {
      if (inventory.canReceivePower(tradingPostSlot)) {
        performPlaceCoatOfArms(tradingPostSlot, inventory);
      }
    }

    if (!unlockedCity) {
      List<City> candidateCities = new ArrayList<>();

      for (City city : cities) {
        if (inventory.canReceiveCity(city)) {
          candidateCities.add(city);
        }
      }
      if (candidateCities.size() == 1) {
        performClaimCityAction(cities.get(0), inventory);
      } else if (candidateCities.size() > 1) {
        actionPending = Action.RECEIVE_CITY;
        return Action.RECEIVE_CITY;
      }
    }

    return requiresReturnTokens(inventory, move) ? Action.RET_TOKEN : null;
  }

  private boolean requiresReturnTokens(UserInventory inventory, Move move) {
    int ntokens = 0;
    for (Entry<TokenType, TokenPile> entry : inventory.getTokenPiles().entrySet()) {
      ntokens += entry.getValue().getSize();
    }
    if (ntokens > 10) {
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
   * Draws a card from the top of the deck with the specified deck level.
   *
   * @param deckLevel the level of the deck
   * @return the card from the top of the deck
   */
  private Card getCardByDeckLevel(DeckType deckLevel) {
    assert deckLevel != null;
    Card card = null;
    for (Deck deck : decks) {
      if (deck.getType() == deckLevel) {
        if (!deck.isEmpty()) {
          card = deck.draw();
          break;
        }
      }
    }
    return card;
  }

  /**
   * Performs a reserve development type action routine.
   *
   * @param move      the move to perform
   * @param inventory the inventory to apply the move side effects to
   */
  private void performReserveDev(Move move, UserInventory inventory) {
    assert move.getCard() != null || move.getDeckType() != null;
    // if we're taking from the table, replenish table from the deck
    Card selectedCard;
    if (move.getCard() != null) {
      // remove the selected card from the board and replenish from same deck type
      selectedCard = move.getCard();
      int ix = cardField.indexOf(selectedCard);
      cardField.remove(selectedCard);
      replenishTakenCardFromDeck(move.getCard().getDeckType(), ix);
    } else {
      selectedCard = getCardByDeckLevel(move.getDeckType());
    }
    if (selectedCard != null) {
      if (!noGoldTokens()) {
        Token gold = this.tokenPiles.get(TokenType.GOLD).removeToken();
        inventory.addToken(gold);
      }
      // add to inventory as reserved card now
      inventory.addReservedCard(selectedCard);
    }
  }

  /**
   * Checks if there is a card of a specific level on the gameboard.
   *
   * @param level the level of the deck (either 1 or 2)
   * @return a boolean determining if there is a card of a specific level on the gameboard
   */
  private boolean deckLevelExists(int level) {
    for (Card card : cardField) {
      if (level == 1 && (card.getDeckType() == DeckType.BASE1
                           || card.getDeckType() == DeckType.ORIENT1)) {
        return true;
      } else if (level == 2 && (card.getDeckType() == DeckType.BASE2
                                || card.getDeckType() == DeckType.ORIENT2)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the bonus actions of an Orient card.
   *
   * @param actions the bonus actions
   * @param inventory the player's inventory
   * @return the bonus actions of an Orient card
   */
  private Action getBonusAction(List<Action> actions, UserInventory inventory) {
    if (actions.size() > 0) {
      if (actions.get(0) == Action.RESERVE_NOBLE) {
        if (!nobles.isEmpty()) {
          return actions.get(0);
        }
      }
      if (actions.get(0) == Action.CASCADE_LEVEL_1) {
        if (deckLevelExists(1)) {
          return actions.get(0);
        }
      }
      if (actions.get(0) == Action.CASCADE_LEVEL_2) {
        if (deckLevelExists(2)) {
          return actions.get(0);
        }
      }
      if (actions.get(0) == Action.DISCARD_FIRST_WHITE_CARD
            || actions.get(0) == Action.DISCARD_FIRST_BLUE_CARD
            || actions.get(0) == Action.DISCARD_FIRST_GREEN_CARD
            || actions.get(0) == Action.DISCARD_FIRST_RED_CARD
            || actions.get(0) == Action.DISCARD_FIRST_BLACK_CARD) {
        TokenType tokenType = null;
        switch (actions.get(0)) {
          case DISCARD_FIRST_WHITE_CARD:
            tokenType = TokenType.DIAMOND;
            break;
          case DISCARD_FIRST_BLUE_CARD:
            tokenType = TokenType.SAPPHIRE;
            break;
          case DISCARD_FIRST_GREEN_CARD:
            tokenType = TokenType.EMERALD;
            break;
          case DISCARD_FIRST_RED_CARD:
            tokenType = TokenType.RUBY;
            break;
          case DISCARD_FIRST_BLACK_CARD:
            tokenType = TokenType.ONYX;
            break;
          default:
            tokenType = null;
        }
        if (tokenType != null) {
          int spiceCount = inventory.getNumSpiceCardsByType(tokenType);
          if (spiceCount >= 2) {
            inventory.discardSpiceCard(tokenType);
            inventory.discardSpiceCard(tokenType);
            return null;
          } else if (spiceCount == 1) {
            inventory.discardSpiceCard(tokenType);
            return actions.get(1);
          } else if (spiceCount == 0) {
            return actions.get(0);
          }
        }
      } else {
        return actions.get(0);
      }
    }
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
    assert move.getCard() != null
             && (inventory.hasCardReserved(move.getCard()) || cardField.contains(move.getCard()));
    // checking to see whether they're buying from reserved card in hand / table or from deck
    Card selectedCard = move.getCard();
    // check to make sure that the card they wish to purchase from their hand is valid
    if (inventory.hasCardReserved(selectedCard)) {
      // they have the card, and it has been reserved, so they can legally buy it
      // add the card to their inventory as a purchased card
      // return the required tokens to purchase the card from user to board
      returnTokensToBoard(inventory.purchaseCard(selectedCard));
    } else {
      // purchase card, take it from the face up table and replace that card on table
      int ix = cardField.indexOf(selectedCard);
      returnTokensToBoard(inventory.purchaseCard(cardField.remove(ix)));
      replenishTakenCardFromDeck(selectedCard.getDeckType(), ix);
    }

    if (selectedCard instanceof OrientCard) {
      List<Action> actions = ((OrientCard) selectedCard).getBonusActions();
      return getBonusAction(actions, inventory);
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
    assert move.getCard() != null && inventory.hasCard(move.getCard())
             && inventory.getUnpairedSpiceCard() != null;
    OrientCard spiceCard = inventory.getUnpairedSpiceCard();
    spiceCard.pairWithCard(move.getCard());
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
    assert tradingPostSlot != null && !tradingPostSlot.isFull();
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
  private void performReserveNoble(Move move, UserInventory inventory) {
    assert move.getNoble() != null && move.getNoble().getStatus() == NobleStatus.ON_BOARD;
    inventory.addReservedNoble(move.getNoble());
    nobles.remove(move.getNoble());
  }

  /**
   * Performs a discard card type action routine.
   *
   * @param move the move to be performed
   * @param inventory the inventory to apply the move side effects to
   */
  private void performDiscardSecondCard(Move move, UserInventory inventory) {
    assert move.getCard() != null;

    inventory.discardCard(move.getCard());

    List<Noble> discardedNobles = new ArrayList<>();
    for (Noble noble : inventory.getNobles()) {
      if (noble.getStatus() == NobleStatus.VISITING && !inventory.canBeVisitedByNoble(noble)) {
        discardedNobles.add(noble);
      }
    }
    for (Noble noble : discardedNobles) {
      nobles.add(inventory.removeNoble(noble));
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
  }

  /**
   * Performs a discard card type action routine.
   *
   * @param move the move to be performed
   * @param inventory the inventory to apply the move side effects to
   */
  private Action performDiscardFirstCard(Move move, UserInventory inventory) {
    assert move.getCard() != null;

    inventory.discardCard(move.getCard());

    List<Noble> discardedNobles = new ArrayList<>();
    for (Noble noble : inventory.getNobles()) {
      if (noble.getStatus() == NobleStatus.VISITING && !inventory.canBeVisitedByNoble(noble)) {
        discardedNobles.add(noble);
      }
    }
    for (Noble noble : discardedNobles) {
      nobles.add(inventory.removeNoble(noble));
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

    if (move.getCard().getTokenBonusAmount() == 2) {
      return null;
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
    assert move.getCard() != null
             && (move.getCard().getDeckType() == DeckType.ORIENT1
                   || move.getCard().getDeckType() == DeckType.BASE1)
             && move.getCard().getCardStatus() == CardStatus.NONE;

    Card levelOneCard = move.getCard();
    inventory.addCascadeLevelOne(levelOneCard);
    int ix = cardField.indexOf(levelOneCard);
    cardField.remove(ix);
    replenishTakenCardFromDeck(levelOneCard.getDeckType(), ix);

    if (levelOneCard instanceof OrientCard) {
      List<Action> actions = ((OrientCard) levelOneCard).getBonusActions();
      return getBonusAction(actions, inventory);
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
    assert move.getCard() != null
             && (move.getCard().getDeckType() == DeckType.ORIENT2
                   || move.getCard().getDeckType() == DeckType.BASE2)
             && move.getCard().getCardStatus() == CardStatus.NONE;

    Card levelTwoCard = move.getCard();
    inventory.addCascadeLevelTwo(levelTwoCard);
    int ix = cardField.indexOf(levelTwoCard);
    cardField.remove(ix);
    replenishTakenCardFromDeck(levelTwoCard.getDeckType(), ix);

    if (levelTwoCard instanceof OrientCard) {
      List<Action> actions = ((OrientCard) levelTwoCard).getBonusActions();
      return getBonusAction(actions, inventory);
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
      if (deck.getType() == deckType) {
        if (!deck.isEmpty()) {
          // we know draw is ok cause of the check for emptiness above
          cardField.add(replenishIndex, deck.draw());
        } else {
          // If the deck is empty, we're going to replace the missing card
          // with a card that is not purchasable/reservable and has an id of -1
          Card card = new Card(-1, 0, null, null, null,
              new CardCost(100, 100, 100, 100, 100));
          card.setCardStatus(CardStatus.PURCHASED);
          cardField.add(replenishIndex, card);
        }
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
    return tokenPiles.get(TokenType.GOLD).getSize() == 0;
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
    return inventories.equals(gameBoard.inventories)
             && decks.equals(gameBoard.decks)
             && cardField.equals(gameBoard.cardField)
             && tokenPiles.equals(gameBoard.tokenPiles)
             && nobles.equals(gameBoard.nobles)
             && tradingPostSlots.equals(gameBoard.tradingPostSlots)
             && cities.equals(gameBoard.cities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inventories, decks, cardField, tokenPiles,
        nobles, tradingPostSlots, cities);
  }
}
