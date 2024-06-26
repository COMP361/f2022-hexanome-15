package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.control.LocalGameStorage;
import ca.mcgill.splendorserver.control.TerminalGameStateManager;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.IllegalGameStateException;
import ca.mcgill.splendorserver.model.SplendorGame;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.action.MoveInfo;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardStatus;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.DeckType;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.nobles.NobleStatus;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Manages the move discovery, selection, and propagation of moves made by specific players in
 * specific game sessions. Singleton by default.
 *
 * @author Zachary Hayden
 */
@RestController
public class ActionManager {
  private final Logger logger = Logger.getAnonymousLogger();

  /**
   * Creates an ActionManager.
   */
  public ActionManager() {
  }

  /**
   * Sends a message to the server to perform the given action.
   *
   * @param gameid The game id of the game being played
   * @param playerName The name of the current player
   * @param actionMd5 The given action, written as a String
   * @param accessToken The access token
   * @return If there is a pending action it indicates, otherwise it indicates the next player up.
   */
  @PutMapping(value = "/api/games/{gameid}/players/{player}/actions/{actionMD5}")
  public ResponseEntity<String> performAction(@PathVariable(name = "gameid") long gameid,
                                              @PathVariable(name = "player") String playerName,
                                              @PathVariable(name = "actionMD5") String actionMd5,
                                              @RequestParam(name = "access_token")
                                              String accessToken
  ) {
    // if the given gameid doesn't exist or isn't active then throw an error
    System.out.println("In here performAction");
    if (!LocalGameStorage.exists(gameid)) {
      throw new IllegalArgumentException(
        "gameid: " + gameid + " is invalid or represents a game which is not currently active");
    }

    // authorize the player and their access token
    //AuthTokenAuthenticator.authenticate(playerName, accessToken);

    // get the game manager instance
    SplendorGame splendorGame = LocalGameStorage.getActiveGame(gameid)
                                  .orElseThrow();
    Optional<PlayerWrapper> playerWrapper = splendorGame.getPlayerByName(playerName);
    Map<String, Move>       moves         = findMoves(splendorGame, playerWrapper);
    System.out.println("PlayerWrapper name: " + playerWrapper.get().getName());
    // throw error if the action MD5 isn't valid
    if (!moves.containsKey(actionMd5)) {
      System.out.println("Could not find move: " + actionMd5);
    }

    // pass the move along so that the game states are appropriately updated
    Move selectedMove = moves.get(actionMd5);
    logger.log(
        Level.INFO, playerName + " played: " + selectedMove); // log the move that was selected
    // Checking if the move is null
    if (selectedMove != null) {
      // apply the selected move to game board
      Action pendingBonusAction = splendorGame.getBoard()
                                    .applyMove(selectedMove, playerWrapper.get());
      UserInventory inventory = splendorGame.getBoard()
                                  .getInventoryByPlayerName(playerName).get();

      // need to handle potential compound actions
      if (pendingBonusAction != null) {
        System.out.println(new Gson().toJson(pendingBonusAction));
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                 .body(pendingBonusAction.toString());
      } else {
        Action endOfTurnAction =
            splendorGame.getBoard().getEndOfTurnActions(selectedMove, inventory);

        if (endOfTurnAction != null) {
          return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                   .body(endOfTurnAction.toString());
        }

        splendorGame.getBoard().endTurn();

        // check for terminal game state after action has been performed
        if (!splendorGame.isFinished()
              && TerminalGameStateManager.isTerminalGameState(splendorGame)) {
          logger.log(Level.INFO, "Terminal game state reached");
        }
        // advance to the next players turn
        PlayerWrapper whoseUpNext = splendorGame.endTurn(playerWrapper.get());
        return ResponseEntity.status(HttpStatus.OK)
                 .body(whoseUpNext.getName());
      }
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }


  /**
   * Method to discover and retrieve all the possible moves
   * for the specific player in specific game session.
   *
   * @param gameid     game ID of the game being played
   * @param playerName player whose turn it currently is
   * @param accessToken the access token
   * @return the possible moves that can be made in a map (key, value) = (hash, raw move objec)
   */
  @GetMapping(value = "/api/games/{gameid}/players/{player}/actions",
      produces = "application/json; charset=utf-8")
  public ResponseEntity<String>
      getAvailableActions(@PathVariable(name = "gameid") long gameid,
                          @PathVariable(name = "player") String playerName,
                          @RequestParam(name = "access_token") String accessToken
  ) {
    System.out.println("in here actions");
    try {
      // if the given gameid doesn't exist or isn't active then throw an error
      if (!LocalGameStorage.exists(gameid)) {
        System.out.println("Game doesn't exist?");
        throw new IllegalArgumentException(
          "gameid: " + gameid + " is invalid or represents a game which is not active");
      }

      // validates the player and their access token
      //AuthTokenAuthenticator.authenticate(playerName, accessToken);

      // get the game manager instance
      SplendorGame splendorGame = LocalGameStorage.getActiveGame(gameid).get();
      Optional<PlayerWrapper> playerWrapper = splendorGame.getPlayerByName(playerName);
      Map<String, Move> moveMap = findMoves(splendorGame, playerWrapper);
      Map<String, MoveInfo> simplifiedMap = prepareMoveMapForTransmission(moveMap);
      String serializedMoves = new Gson().toJson(simplifiedMap);
      return ResponseEntity.status(HttpStatus.OK)
               .body(serializedMoves);
    } catch (IllegalArgumentException | IllegalStateException e) {
      /*
      - if we get here either the game doesnt exist or we've reached a state error somewhere
      - a state error indicates that a game rule / state has been broken ie player has too many
      devs etc
      - send a bad response back indicating the error
       */
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
               .body(e.getMessage());
    }
  }

  private Map<String, MoveInfo> prepareMoveMapForTransmission(Map<String, Move> moveMap) {
    Map<String, MoveInfo> simplifiedMap = new HashMap<String, MoveInfo>();
    for (String md5 : moveMap.keySet()) {
      Move move = moveMap.get(md5);
      String cardId = move.getCard() != null ? Integer.toString(move.getCard().getId()) : null;
      String tokenType =
          move.getSelectedTokenTypes() != null ? move.getSelectedTokenTypes().toString() : null;
      String noble = move.getNoble() != null ? Integer.toString(move.getNoble().getId()) : null;
      String city = move.getCity() != null ? Integer.toString(move.getCity().getId()) : null;
      String deckLevel = move.getDeckType() != null ? move.getDeckType().toString() : null;
      simplifiedMap.put(md5, new MoveInfo(move.getPlayerName(),
          move.getAction().toString(), cardId, tokenType, noble, city, deckLevel));
    }
    return simplifiedMap;
  }


  /**
   * Finds the moves able to be made for the given player in the given gameboard.
   *
   * @param splendorGame game board.
   * @param player       player.
   * @return the possible moves for the player based on the game state.
   * @throws AssertionError if splendorGame or player are null.
   */
  private Map<String, Move> findMoves(SplendorGame splendorGame,
                                      Optional<PlayerWrapper> player
  ) {
    assert splendorGame != null;

    // handling an empty player which means player is not in the that game
    if (player.isEmpty()) {
      logger.log(Level.INFO, "empty player -> no moves to be made");
      return new LinkedHashMap<>();
    }

    // extracting the player wrapper since we know if we get here it exists
    PlayerWrapper playerWrapper = player.get();

    // if game finished return empty set := no moves
    if (splendorGame.isFinished()) {
      logger.log(Level.INFO, "game is finished, no moves can be made");
      return new LinkedHashMap<>();
      // if its not the players turn then return no moves
    } else if (!splendorGame.whoseTurn()
                  .getName()
                  .equals(playerWrapper.getName())) {
      logger.log(Level.INFO, "not " + playerWrapper.getName() + " turn -> no moves can be made");
      return new LinkedHashMap<>();
    }

    // now search through the gameboard
    // and create a mapping of viable moves the player can make given their state
    //  scan over their tokens and cards to ascertain what moves are possible
    GameBoard gameBoard = splendorGame.getBoard();
    // we know that the player is in the game if we make it to this point
    UserInventory userInventory = gameBoard.getInventoryByPlayerName(playerWrapper.getName())
                                    .orElseThrow();
    Map<String, Move> moveMap = new LinkedHashMap<>();
    if (splendorGame.getBoard().getPendingAction() != null) {
      switch (splendorGame.getBoard().getPendingAction()) {
        case DISCARD_FIRST_WHITE_CARD:
          getDiscardFirstMoves(moveMap, userInventory,
              gameBoard, playerWrapper, TokenType.DIAMOND);
          break;
        case DISCARD_FIRST_BLUE_CARD:
          getDiscardFirstMoves(moveMap, userInventory,
              gameBoard, playerWrapper, TokenType.SAPPHIRE);
          break;
        case DISCARD_FIRST_GREEN_CARD:
          getDiscardFirstMoves(moveMap, userInventory,
              gameBoard, playerWrapper, TokenType.EMERALD);
          break;
        case DISCARD_FIRST_RED_CARD:
          getDiscardFirstMoves(moveMap, userInventory,
              gameBoard, playerWrapper, TokenType.RUBY);
          break;
        case DISCARD_FIRST_BLACK_CARD:
          getDiscardFirstMoves(moveMap, userInventory,
              gameBoard, playerWrapper, TokenType.ONYX);
          break;
        case DISCARD_SECOND_WHITE_CARD:
          getDiscardSecondMoves(moveMap, userInventory,
              gameBoard, playerWrapper, TokenType.DIAMOND);
          break;
        case DISCARD_SECOND_BLUE_CARD:
          getDiscardSecondMoves(moveMap, userInventory,
              gameBoard, playerWrapper, TokenType.SAPPHIRE);
          break;
        case DISCARD_SECOND_GREEN_CARD:
          getDiscardSecondMoves(moveMap, userInventory,
              gameBoard, playerWrapper, TokenType.EMERALD);
          break;
        case DISCARD_SECOND_RED_CARD:
          getDiscardSecondMoves(moveMap, userInventory,
              gameBoard, playerWrapper, TokenType.RUBY);
          break;
        case DISCARD_SECOND_BLACK_CARD:
          getDiscardSecondMoves(moveMap, userInventory,
              gameBoard, playerWrapper, TokenType.ONYX);
          break;
        case PAIR_SPICE_CARD:
          getPairSpiceCardMoves(moveMap, userInventory, gameBoard, playerWrapper);
          break;
        case CASCADE_LEVEL_2:
          getCascadeLevelTwoMoves(moveMap, userInventory, gameBoard, playerWrapper);
          break;
        case CASCADE_LEVEL_1:
          getCascadeLevelOneMoves(moveMap, userInventory, gameBoard, playerWrapper);
          break;
        case RESERVE_NOBLE:
          getReserveNobleMoves(moveMap, userInventory, gameBoard, playerWrapper);
          break;
        case RECEIVE_NOBLE:
          getPossibleNobleVisitors(moveMap, userInventory, gameBoard, playerWrapper);
          break;
        case TAKE_TOKEN:
          getRemainingTokenMoves(moveMap, userInventory, gameBoard, playerWrapper);
          break;
        case RET_TOKEN:
          getReturnTokenMoves(moveMap, userInventory, gameBoard, playerWrapper);
          break;
        case TAKE_EXTRA_TOKEN:
          getTakeExtraTokenMoves(moveMap, userInventory, gameBoard, playerWrapper);
          break;
        case RECEIVE_CITY:
          getReceiveCityMoves(moveMap, userInventory, gameBoard, playerWrapper);
          break;
        default:
          break;
      }
    } else {
      //this means we are starting a new base move
      getBuyDevMoves(moveMap, userInventory, gameBoard, playerWrapper);
      getReserveDevMoves(moveMap, userInventory, gameBoard, playerWrapper);
      getAvailableTokenMoves(moveMap, gameBoard, playerWrapper);
    }

    return moveMap;
  }

  private void getTakeExtraTokenMoves(Map<String, Move> moveMap,
                                      UserInventory inventory,
                                      GameBoard gameBoard, PlayerWrapper player) {
    TokenType restrictedType = null;
    if (gameBoard.getMoveCache().get(0).getAction() == Action.TAKE_TOKEN) {
      //in this case we need to take a token of the type not already taken
      restrictedType = gameBoard.getMoveCache().get(0).getSelectedTokenTypes();
    }
    for (Entry<TokenType, TokenPile> entry : gameBoard.getTokenPiles().entrySet()) {
      if (entry.getValue().getSize() > 0 && gameBoard.getTokenCount() > 0
            && restrictedType == null ? true : entry.getKey() != restrictedType) {
        Move move =
            new Move(Action.TAKE_EXTRA_TOKEN, null, player, null, null, null, entry.getKey(), null);
        String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                           .toUpperCase();
        moveMap.put(moveMd5, move);
      }
    }
  }

  private void getReturnTokenMoves(Map<String, Move> moveMap,
                                   UserInventory inventory,
                                   GameBoard gameBoard, PlayerWrapper player) {
    for (Entry<TokenType, TokenPile> entry : inventory.getTokenPiles().entrySet()) {
      if (entry.getValue().getSize() > 0) {
        Move move = new Move(Action.RET_TOKEN, null, player, null,
            null, null, entry.getKey(), null);
        String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                           .toUpperCase();
        moveMap.put(moveMd5, move);
      }
    }
  }

  private void getRemainingTokenMoves(Map<String, Move> moveMap,
                                      UserInventory inventory,
                                      GameBoard gameBoard, PlayerWrapper player) {
    List<Move> moveCache = gameBoard.getMoveCache();
    if (moveCache.size() == 1) {
      Move pastMove = moveCache.get(0);
      TokenType pastType = pastMove.getSelectedTokenTypes();
      int npileswithtokens = 0;
      for (Entry<TokenType, TokenPile> gbpile : gameBoard.getTokenPiles().entrySet()) {
        if (gbpile.getValue().getSize() > 0 && gbpile.getKey() != pastType) {
          npileswithtokens++;
        }
      }
      for (TokenType type : gameBoard.getTokenPiles().keySet()) {
        if (type == TokenType.GOLD) {
          continue;
        }
        TokenPile pile = gameBoard.getTokenPiles().get(type);
        if (type == pastType) {
          if (pile.getSize() >= 3) {
            Move move = new Move(Action.TAKE_TOKEN, null, player,
                null, null, null, pile.getType(), null);
            String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                               .toUpperCase();
            moveMap.put(moveMd5, move);
          }
        } else {
          if (pile.getSize() > 0 && npileswithtokens >= 2) {
            Move move = new Move(Action.TAKE_TOKEN,
                null, player, null,
                null, null, pile.getType(), null);
            String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                               .toUpperCase();
            moveMap.put(moveMd5, move);
          }
        }
      }
    } else if (moveCache.size() == 2) {
      for (TokenType type : gameBoard.getTokenPiles().keySet()) {
        if (moveCache.get(0).getSelectedTokenTypes() == type
              || moveCache.get(1).getSelectedTokenTypes() == type
              || type == TokenType.GOLD) {
          continue;
        }
        TokenPile pile = gameBoard.getTokenPiles().get(type);
        if (pile.getSize() > 0) {
          Move move = new Move(Action.TAKE_TOKEN,
              null, player, null,
              null, null, pile.getType(), null);
          String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                             .toUpperCase();
          moveMap.put(moveMd5, move);
        }
      }
    }
  }

  private void getBuyDevMoves(Map<String, Move> moveMap, UserInventory inventory,
                              GameBoard gameBoard, PlayerWrapper player) {
    // player can buy dev card from face-up on the table or reserved in their hand

    // cards face-up on table
    for (Card faceUp : gameBoard.getCards()) {
      // cannot offer a move involving a card already purchased
      if (inventory.canAffordCard(faceUp) && faceUp.getCardStatus() == CardStatus.NONE) {
        Move move = new Move(Action.PURCHASE_DEV, faceUp, player, null,
            null, null, null, null);
        String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move)).toUpperCase();
        moveMap.put(moveMd5, move);
      }
    }

    // cards in players inventory if any
    if (inventory.hasCardsReserved()) {
      // now check if they can afford them
      for (Card card : inventory) {
        if (card.isReserved() && inventory.canAffordCard(card)) {
          Move move = new Move(Action.PURCHASE_DEV, card, player, null,
              null, null, null, null);
          String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                             .toUpperCase();
          moveMap.put(moveMd5, move);
        }
      }
    }
  }

  private void getPossibleNobleVisitors(Map<String, Move> moveMap, UserInventory inventory,
                                        GameBoard gameBoard, PlayerWrapper player) {
    for (Noble noble : gameBoard.getNobles()) {
      if (inventory.canBeVisitedByNoble(noble) && noble.getStatus() == NobleStatus.ON_BOARD) {
        Move move = new Move(Action.RECEIVE_NOBLE, null, player, null,
            noble, null, null, null);
        String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                           .toUpperCase();
        moveMap.put(moveMd5, move);
      }
    }
    for (Noble noble : inventory.getNobles()) {
      if (noble.getStatus() == NobleStatus.RESERVED) {
        Move move = new Move(Action.RECEIVE_NOBLE, null, player, null,
            noble, null, null, null);
        String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                           .toUpperCase();
        moveMap.put(moveMd5, move);
      }
    }
  }

  private void getReceiveCityMoves(Map<String, Move> moveMap, UserInventory inventory,
                                   GameBoard gameBoard, PlayerWrapper player) {
    for (City city : gameBoard.getCities()) {
      if (inventory.canReceiveCity(city)) {
        Move move = new Move(Action.RECEIVE_CITY, null, player, null,
            null, null, null, city);
        String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                           .toUpperCase();
        moveMap.put(moveMd5, move);
      }
    }
  }

  private void getReserveDevMoves(Map<String, Move> moveMap, UserInventory inventory,
                                  GameBoard gameBoard, PlayerWrapper player) {
    // players may not have more than three reserved cards in hand
    final int maxNumReservedCards = 3;
    if (inventory.reservedCardCount() >= maxNumReservedCards) {
      return;
    }

    Action action = Action.RESERVE_DEV;

    // here looking at face up cards
    for (Card card : gameBoard.getCards()) {
      if (card.getCardStatus() == CardStatus.NONE) {
        Move takeFaceUp = new Move(action, card, player, null, null,
            null, null, null);
        String takeFaceUpMd5 = DigestUtils.md2Hex(new Gson().toJson(takeFaceUp))
                                 .toUpperCase();
        moveMap.put(takeFaceUpMd5, takeFaceUp);
      }
    }
    // or can take from one of the decks, but they won't be able to see the card, so it'll be null
    // ,but they will see the different deck levels (1, 2, 3)
    for (Deck deck : gameBoard.getDecks()) {
      // can only legally take from the given deck if it isn't empty
      if (!deck.isEmpty()) {
        Move takeFromDeck = new Move(action, null, player, deck.getType(),
            null, null, null, null
        );
        String takeFromDeckMd5 = DigestUtils.md2Hex(new Gson().toJson(takeFromDeck))
                                   .toUpperCase();
        moveMap.put(takeFromDeckMd5, takeFromDeck);
      }
    }
  }

  private void getPairSpiceCardMoves(Map<String, Move> moveMap, UserInventory inventory,
                                     GameBoard gameBoard, PlayerWrapper player) {
    for (Card card : inventory.getCards()) {
      if (inventory.getCards().size() > 0 && inventory.getUnpairedSpiceCard() != null) {
        if (card.getTokenBonusType() != null && card.getTokenBonusType() != TokenType.GOLD) {
          Move move = new Move(Action.PAIR_SPICE_CARD, card, player, null,
              null, null, null, null);
          String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                             .toUpperCase();
          moveMap.put(moveMd5, move);
        }
      }
    }
  }

  private void getAvailableTokenMoves(Map<String, Move> moveMap,
                                      GameBoard gameBoard, PlayerWrapper player) {
    List<TokenPile> piles = gameBoard.getTokenPilesNoGold();
    int ntokenpileswithtokens = 0;
    for (TokenPile pile : piles) {
      if (pile.getSize() > 0) {
        ++ntokenpileswithtokens;
      }
    }
    for (TokenPile pile : piles) {
      if (pile.getSize() > 0 && ntokenpileswithtokens >= 3) {
        Move takeTokenMove = new Move(Action.TAKE_TOKEN, null, player,
            null, null, null, pile.getType(), null);
        String takeTokenMoveMd5 = DigestUtils.md2Hex(new Gson().toJson(takeTokenMove))
                                    .toUpperCase();
        moveMap.put(takeTokenMoveMd5, takeTokenMove);
      } else if (pile.getSize() > 3) {
        Move takeTokenMove = new Move(Action.TAKE_TOKEN, null, player,
            null, null, null, pile.getType(), null);
        String takeTokenMoveMd5 = DigestUtils.md2Hex(new Gson().toJson(takeTokenMove))
                                    .toUpperCase();
        moveMap.put(takeTokenMoveMd5, takeTokenMove);
      }
    }
  }


  /**
   * Calculates the moves of reserving nobles available to the player and adds them to move map.
   *
   * @param moveMap map of possible moves for the player based on game state
   * @param inventory the inventory of the player
   * @param gameBoard the game board
   * @param player the current player
   */
  private void getReserveNobleMoves(Map<String, Move> moveMap, UserInventory inventory,
                                    GameBoard gameBoard, PlayerWrapper player) {

    for (Noble noble : gameBoard.getNobles()) {
      Move move = new Move(Action.RESERVE_NOBLE, null, player,
          null, noble, null, null, null);
      String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                         .toUpperCase();
      moveMap.put(moveMd5, move);
    }

  }

  /**
   * Calculates the moves of the level one cascade bonus action available
   * to the player and adds them to move map.
   *
   * @param moveMap map of possible moves for the player based on game state
   * @param inventory the inventory of the player
   * @param gameBoard the game board
   * @param player the current player
   */
  private void getCascadeLevelOneMoves(Map<String, Move> moveMap, UserInventory inventory,
                                       GameBoard gameBoard, PlayerWrapper player) {
    for (Card card : gameBoard.getCards()) {
      if (card.getCardStatus() == CardStatus.NONE
            && (card.getDeckType() == DeckType.ORIENT1
                  || card.getDeckType() == DeckType.BASE1)) {
        Move move = new Move(Action.CASCADE_LEVEL_1, card, player,
            DeckType.ORIENT1, null, null, null, null);
        String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                           .toUpperCase();
        moveMap.put(moveMd5, move);
      }
    }
  }

  /**
   * Calculates the moves of the level two cascade bonus action available
   * to the player and adds them to move map.
   *
   * @param moveMap map of possible moves for the player based on game state
   * @param inventory the inventory of the player
   * @param gameBoard the game board
   * @param player the current player
   */
  private void getCascadeLevelTwoMoves(Map<String, Move> moveMap, UserInventory inventory,
                                       GameBoard gameBoard, PlayerWrapper player) {
    for (Card card : gameBoard.getCards()) {
      if (card.getCardStatus() == CardStatus.NONE
            && (card.getDeckType() == DeckType.ORIENT2
                  || card.getDeckType() == DeckType.BASE2)) {
        Move move = new Move(Action.CASCADE_LEVEL_2, card, player,
            DeckType.ORIENT2, null, null, null, null);
        String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                           .toUpperCase();
        moveMap.put(moveMd5, move);
      }
    }
  }

  private void getDiscardFirstMoves(Map<String, Move> moveMap, UserInventory inventory,
                                    GameBoard gameBoard, PlayerWrapper player,
                                    TokenType tokenType) {

    Action action = null;

    switch (tokenType) {
      case DIAMOND -> {
        action = Action.DISCARD_FIRST_WHITE_CARD;
      }
      case SAPPHIRE -> {
        action = Action.DISCARD_FIRST_BLUE_CARD;
      }
      case EMERALD -> {
        action = Action.DISCARD_FIRST_GREEN_CARD;
      }
      case RUBY -> {
        action = Action.DISCARD_FIRST_RED_CARD;
      }
      case ONYX -> {
        action = Action.DISCARD_FIRST_BLACK_CARD;
      }
      default -> {
        action = null;
      }
    }
    if (action != null) {
      for (Card card : inventory.getCards()) {
        if (card.getTokenBonusType() == tokenType && card.isPurchased()) {
          Move move = new Move(action, card, player, null, null,
              null, null, null);
          String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                             .toUpperCase();
          moveMap.put(moveMd5, move);
        }
      }
    }
  }

  private void getDiscardSecondMoves(Map<String, Move> moveMap, UserInventory inventory,
                                     GameBoard gameBoard,
                                     PlayerWrapper player, TokenType tokenType) {

    Action action = null;

    switch (tokenType) {
      case DIAMOND -> {
        action = Action.DISCARD_SECOND_WHITE_CARD;
      }
      case SAPPHIRE -> {
        action = Action.DISCARD_SECOND_BLUE_CARD;
      }
      case EMERALD -> {
        action = Action.DISCARD_SECOND_GREEN_CARD;
      }
      case RUBY -> {
        action = Action.DISCARD_SECOND_RED_CARD;
      }
      case ONYX -> {
        action = Action.DISCARD_SECOND_BLACK_CARD;
      }
      default -> {
        action = null;
      }
    }
    if (action != null) {
      for (Card card : inventory.getCards()) {
        if (card.getTokenBonusType() == tokenType && card.isPurchased()) {
          Move move = new Move(action, card, player, null, null,
              null, null, null);
          String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                             .toUpperCase();
          moveMap.put(moveMd5, move);
        }
      }
    }
  }
}
