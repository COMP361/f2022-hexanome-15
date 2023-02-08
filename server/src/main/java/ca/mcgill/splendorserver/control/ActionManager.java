package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.IllegalGameStateException;
import ca.mcgill.splendorserver.model.SplendorGame;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Manages the move discovery, selection, and propagation of moves made by specific players in
 * specific game sessions. Singleton by default.
 *
 * @author Zachary Hayden
 * Date: 2/1/23
 */
@RestController
public class ActionManager {
  private final Logger logger = Logger.getAnonymousLogger();

  // TODO: do we need the users access token as request param to validate here???

  @PostMapping(value = "/api/games/{gameid}/players/{player}/actions/{actionMD5}")
  public ResponseEntity<String> performAction(@PathVariable(name = "gameid") long gameid,
                                              @PathVariable(name = "player") String playerName,
                                              @PathVariable(name = "actionMD5") String actionMd5,
                                              @RequestParam(name = "access_token")
                                              String accessToken
  ) {
    // if the given gameid doesn't exist or isn't active then throw an error
    if (!LocalGameStorage.exists(gameid)) {
      throw new IllegalArgumentException(
          "gameid: " + gameid + " is invalid or represents a game which is not currently active");
    }

    // authorize the player and their access token
    AuthTokenAuthenticator.authenticate(playerName, accessToken);

    // get the game manager instance
    SplendorGame splendorGame = LocalGameStorage.getActiveGame(gameid)
                                                .orElseThrow();
    Optional<PlayerWrapper> playerWrapper = splendorGame.getPlayerByName(playerName);
    Map<String, Move>       moves         = findMoves(splendorGame, playerWrapper);
    // throw error if the action MD5 isn't valid
    if (!moves.containsKey(actionMd5)) {
      throw new IllegalArgumentException(
          "Received move MD5 (" + actionMd5 + ") doesn't match any moves offered");
    }

    // pass the move along so that the game states are appropriately updated
    Move selectedMove = moves.get(actionMd5);
    logger.log(
        Level.INFO, playerName + " played: " + selectedMove); // log the move that was selected
    // apply the selected move to game board
    splendorGame.getBoard()
                .applyMove(selectedMove, playerWrapper.orElseThrow(
                    () -> new IllegalGameStateException(
                        "If a valid move has been selected, their must be a corresponding player who selected it but player was found empty")));

    // TODO: implement update all game boards via broadcasting manager for players in session

    // check for terminal game state after action has been performed
    if (TerminalGameStateManager.isTerminalGameState(splendorGame)) {
      logger.log(Level.INFO, "Terminal game state reached");
      // TODO: handle end of game, needs to go until the first players turn is up
      // TODO: handle tie game
    }

    // advance to the next players turn
    PlayerWrapper whoseUpNext = splendorGame.endTurn(playerWrapper.get());
    return ResponseEntity.status(HttpStatus.OK)
                         .body(whoseUpNext.getName());

  }


  /**
   * Method to discover and retrieve all the possible moves for the specific player in specific game session.
   *
   * @param gameid     game ID of the game being played
   * @param playerName player whose turn it currently is
   * @return the possible moves that can be made in a map w/ key = hash of move object, value = raw move object.
   */
  @GetMapping(value = "/api/games/{gameid}/players/{player}/actions",
              produces = "application/json; charset=utf-8")
  public ResponseEntity getAvailableActions(@PathVariable(name = "gameid") long gameid,
                                            @PathVariable(name = "player") String playerName,
                                            @RequestParam(name = "access_token") String accessToken
  ) {
    try {
      // if the given gameid doesn't exist or isn't active then throw an error
      if (!LocalGameStorage.exists(gameid)) {
        throw new IllegalArgumentException(
            "gameid: " + gameid + " is invalid or represents a game which is not active");
      }

      // validates the player and their access token
      AuthTokenAuthenticator.authenticate(playerName, accessToken);

      // get the game manager instance
      SplendorGame splendorGame = LocalGameStorage.getActiveGame(gameid)
                                                  .orElseThrow();
      Optional<PlayerWrapper> playerWrapper = splendorGame.getPlayerByName(playerName);
      String serializedMoves = new Gson().toJson(
          findMoves(splendorGame, playerWrapper));
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

  private List<TokenType[]> generateTokenCombinations(List<TokenType> tokenTypes, int r) {
    List<TokenType[]> combinations = new ArrayList<>();
    int[]             ixs          = new int[r]; // keep indexes

    if (r <= tokenTypes.size()) {
      for (int i = 0; (ixs[i] = i) < r - 1; i++) ;
      combinations.add(getSubset(tokenTypes, ixs));
      for (; ; ) {
        int i;
        for (i = r - 1; i >= 0 && ixs[i] == tokenTypes.size() - r + i; i--) ;
        if (i < 0) {
          break;
        }
        ixs[i]++;
        for (i++; i < r; i++) {
          ixs[i] = ixs[i - 1] + 1;
        }
        combinations.add(getSubset(tokenTypes, ixs));

      }

    }
    return combinations;
  }

  private TokenType[] getSubset(List<TokenType> input, int[] s) {
    TokenType[] result = new TokenType[s.length];
    for (int i = 0; i < s.length; i++) {
      result[i] = input.get(s[i]);
    }
    return result;
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

    // now search through the gameboard and create a mapping of viable moves the player can make given their state
    // TODO: find the players inventory from the game, scan over their tokens and cards to ascertain what moves are possible
    GameBoard gameBoard = splendorGame.getBoard();
    // we know that the player is in the game if we make it to this point
    UserInventory userInventory = gameBoard.getInventoryByPlayerName(playerWrapper.getName())
                                           .orElseThrow();
    Map<String, Move> moveMap = new LinkedHashMap<>();
    getBuyDevMoves(moveMap, userInventory, gameBoard, playerWrapper);
    getReserveDevMoves(moveMap, userInventory, gameBoard, playerWrapper);
    getSelectTokenMoves(moveMap, userInventory, gameBoard, playerWrapper);

    return moveMap;
  }

  private void getBuyDevMoves(Map<String, Move> moveMap, UserInventory inventory,
                              GameBoard gameBoard, PlayerWrapper player
  ) {
    // player can buy dev card from face-up on the table or reserved in their hand

    // cards face-up on table
    for (Card faceUp : gameBoard.getCards()) {
      // cannot offer a move involving a card already purchased
      if (inventory.canAffordCard(faceUp) && !faceUp.isPurchased()) {
        accumlateBuyDevMovesConsideringNobles(moveMap, inventory, gameBoard, player, faceUp);
      }
    }

    // cards in players inventory if any
    if (inventory.hasCardsReserved()) {
      // now check if they can afford them
      for (Card card : inventory) {
        if (card.isReserved() && inventory.canAffordCard(card)) {
          accumlateBuyDevMovesConsideringNobles(moveMap, inventory, gameBoard, player, card);
        }
      }
    }

  }

  private void accumlateBuyDevMovesConsideringNobles(Map<String, Move> moveMap,
                                                     UserInventory inventory, GameBoard gameBoard,
                                                     PlayerWrapper player, Card faceUp
  ) {
    if (wouldBeVisitedByPurchasing(faceUp, inventory, gameBoard)) {
      // this card purchase would result in visitation from noble so provide the combination
      // of nobles that the player can pick (if > 1) otherwise the one that will automatically
      // visit them after their turn
      List<Noble> possibleNobleVisitors = getPossibleNobleVisitors(
          inventory, gameBoard, faceUp);
      for (Noble noble : possibleNobleVisitors) {
        Move move = new Move(Action.PURCHASE_DEV, faceUp, player, null, null,
                             noble, null
        );
        String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                                    .toUpperCase();
        moveMap.put(moveMd5, move);
      }
    } else {
      // purchasing this card wouldn't result in being visited by a noble so proceed as normal
      Move move = new Move(Action.PURCHASE_DEV, faceUp, player, null, null,
                           null, null
      );
      String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                                  .toUpperCase();
      moveMap.put(moveMd5, move);
    }
  }

  private boolean wouldBeVisitedByPurchasing(Card card, UserInventory inventory,
                                             GameBoard gameBoard
  ) {
    for (Noble noble : gameBoard.getNobles()) {
      if (inventory.canBeVisitedByNobleWithCardPurchase(noble, card)) {
        return true;
      }
    }
    return false;
  }

  private List<Noble> getPossibleNobleVisitors(UserInventory inventory, GameBoard gameBoard,
                                               Card faceUp
  ) {
    List<Noble> possibleNobleVisitors = new ArrayList<>();
    for (Noble noble : gameBoard.getNobles()) {
      if (inventory.canBeVisitedByNobleWithCardPurchase(noble, faceUp)) {
        possibleNobleVisitors.add(noble);
      }
    }
    return possibleNobleVisitors;
  }


  private void getReserveDevMoves(Map<String, Move> moveMap, UserInventory inventory,
                                  GameBoard gameBoard, PlayerWrapper player
  ) {
    // players may not have more than three reserved cards in hand
    final int maxNumReservedCards = 3;
    if (inventory.reservedCardCount() > maxNumReservedCards) {
      throw new IllegalGameStateException(
          "Illegal for " + player + " to have more than 3 reserved cards in hand");
    }

    // to reserve, player can take any face-up dev card or draw 1 from one of the three decks

    // the player will receive a gold token (joker) if available
    Action action;
    if (gameBoard.noGoldTokens()) {
      action = Action.RESERVE_DEV;
    } else { // if there's at least 1 gold token (joker)
      action = Action.RESERVE_DEV_TAKE_JOKER;
    }

    // here looking at face up cards
    for (Card card : gameBoard.getCards()) {
      Move takeFaceUp = new Move(action, card, player, null, null, null, null);
      String takeFaceUpMd5 = DigestUtils.md2Hex(new Gson().toJson(takeFaceUp))
                                        .toUpperCase();
      moveMap.put(takeFaceUpMd5, takeFaceUp);
    }
    // or can take from one of the decks, but they won't be able to see the card, so it'll be null
    // ,but they will see the different deck levels (1, 2, 3)
    for (Deck deck : gameBoard.getDecks()) {
      // can only legally take from the given deck if it isn't empty
      if (!deck.isEmpty()) {
        Move takeFromDeck = new Move(action, null, player, deck.getType(),
                                     null, null, null
        );
        String takeFromDeckMd5 = DigestUtils.md2Hex(new Gson().toJson(takeFromDeck))
                                            .toUpperCase();
        moveMap.put(takeFromDeckMd5, takeFromDeck);
      }
    }
  }

  /**
   * Gets the legal moves possible in terms of selecting tokens.
   *
   * @param moveMap   map of valid moves.
   * @param inventory the users inventory whose turn it is.
   * @param gameBoard the game board on which the session is taking place.
   * @param player    the player whose turn it currently is.
   */
  private void getSelectTokenMoves(Map<String, Move> moveMap, UserInventory inventory,
                                   GameBoard gameBoard, PlayerWrapper player
  ) {
    // checking select token moves:

    // list of token types which can be drawn from while selecting 3 tokens from different piles
    List<TokenType> validTake3TokenTypes = new ArrayList<>();
    for (TokenPile tokenPile : gameBoard.getTokenPilesNoGold()) {
      // can take 3 tokens of different colors as long as pile isn't empty
      if (tokenPile.getSize() > 0) {
        validTake3TokenTypes.add(tokenPile.getType());
      }
      // can only pick 2 of same color if there are at least 4 of that color available
      if (tokenPile.getSize() >= 4) {
        Move move;
        if (inventory.tokenCount() <= 8) {
          move = new Move(
              Action.TAKE_2_GEM_TOKENS_SAME_COL, null, player, null, null, null,
              tokenPile.getType()
          );
          String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                                      .toUpperCase();
          moveMap.put(moveMd5, move);
        } else if (inventory.tokenCount() == 9) {
          // must return 1 token after the 2 tokens are taken -> any token in inventory plus
          // selected token if they don't have it already
          String          moveMd5;
          List<TokenType> possibleReturnTokenTypes = inventory.getTokenTypes();
          if (!inventory.hasTokenType(tokenPile.getType())) {
            possibleReturnTokenTypes.add(tokenPile.getType());
          }
          // loop over all tokens in their inventory and make a move for all different returns
          for (TokenType tokenType : possibleReturnTokenTypes) {
            move    = new Move(Action.TAKE_2_GEM_TOKENS_SAME_COL_RET_1, null, player, null,
                               new TokenType[] {tokenType}, null, tokenPile.getType()
            );
            moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                                 .toUpperCase();
            moveMap.put(moveMd5, move);
          }

        } else if (inventory.tokenCount() == 10) {
          // have to loop over all possible token return combinations
          List<TokenType> availableReturnTokens = inventory.getTokenTypes();
          // add the token which is being taken in this move as possible return too
          if (!availableReturnTokens.contains(tokenPile.getType())) {
            availableReturnTokens.add(tokenPile.getType());
          }
          // get all combinations of token returns
          List<TokenType[]> returnCombinations = generateTokenCombinations(
              availableReturnTokens, 2);
          // loop through all return combinations now
          String moveMD5;
          for (TokenType[] tokenTypes : returnCombinations) {
            move    = new Move(Action.TAKE_2_GEM_TOKENS_SAME_COL_RET_2, null, player, null,
                               tokenTypes,
                               null, tokenPile.getType()
            );
            moveMD5 = DigestUtils.md2Hex(new Gson().toJson(move))
                                 .toUpperCase();
            moveMap.put(moveMD5, move);
          }

        } else {
          throw new IllegalGameStateException(
              "Inventory of " + player.getName() + " can never exceed 10 tokens");
        }

      }
    }

    // creating the take 3 diff gem tokens moves for all tokens that can be drawn from
    if (!validTake3TokenTypes.isEmpty()) {
      // determining the correct action
      Action action;
      if (inventory.tokenCount() <= 7) {
        action = Action.TAKE_3_GEM_TOKENS_DIFF_COL;
      } else if (inventory.tokenCount() == 8) {
        action = Action.TAKE_3_GEM_TOKENS_DIFF_COL_RET_1;
      } else if (inventory.tokenCount() == 9) {
        action = Action.TAKE_3_GEM_TOKENS_DIFF_COL_RET_2;
      } else if (inventory.tokenCount() == 10) {
        action = Action.TAKE_3_GEM_TOKENS_DIFF_COL_RET_3;
      } else {
        throw new IllegalGameStateException(
            "Inventory of " + player.getName() + " can never exceed 10 tokens");
      }

      // looping through all the possible return token combinations
      int numTokensTake = 3;
      List<TokenType[]> possibleTokens = generateTokenCombinations(
          validTake3TokenTypes, numTokensTake);
      List<TokenType> possibleReturnTokens;
      for (TokenType[] tokenTypes : possibleTokens) {
        // get all tokens in inventory and if the tokens taken for this move aren't there add them
        // we know there are 3 token types in the array
        possibleReturnTokens = inventory.getTokenTypes();
        if (!possibleReturnTokens.contains(tokenTypes[0])) {
          possibleReturnTokens.add(tokenTypes[0]);
        } else if (!possibleReturnTokens.contains(tokenTypes[1])) {
          possibleReturnTokens.add(tokenTypes[1]);
        } else if (!possibleReturnTokens.contains(tokenTypes[2])) {
          possibleReturnTokens.add(tokenTypes[2]);
        }

        List<TokenType[]> possibleReturnCombinations;
        // matching by action how many tokens need to be returned
        switch (action) {
          case TAKE_3_GEM_TOKENS_DIFF_COL -> {
            Move move = new Move(action, null, player, null, null, null, tokenTypes);
            String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                                        .toUpperCase();
            moveMap.put(moveMd5, move);
            possibleReturnCombinations = null; // this will never be reached bc of the check below
          }
          case TAKE_3_GEM_TOKENS_DIFF_COL_RET_1 -> {
            possibleReturnCombinations = generateTokenCombinations(possibleReturnTokens, 1);
          }
          case TAKE_3_GEM_TOKENS_DIFF_COL_RET_2 -> {
            possibleReturnCombinations = generateTokenCombinations(possibleReturnTokens, 2);
          }
          case TAKE_3_GEM_TOKENS_DIFF_COL_RET_3 -> {
            possibleReturnCombinations = generateTokenCombinations(possibleReturnTokens, 3);
          }
          default -> throw new IllegalStateException(
              "Unexpected Action found, expected one of the \"take 3 token\" moves");
        }

        // if we have a move requiring returning, loop through the combinations
        if (action != Action.TAKE_3_GEM_TOKENS_DIFF_COL) {
          for (TokenType[] returns : possibleReturnCombinations) {
            Move move = new Move(action, null, player, null, returns, null, tokenTypes);
            String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move))
                                        .toUpperCase();
            moveMap.put(moveMd5, move);
          }
        }

      }


    }
  }

}
