package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.games.PlayerWrapper;
import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * Manages the move discovery, selection, and propogation of moves made by specific players in
 * specific game sessions. Singleton by default.
 *
 * @author Zachary Hayden
 * Date: 2/1/23
 */
@RestController
public class MoveManager {
  private final Logger logger = Logger.getAnonymousLogger();

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

  // TODO: do we need the users access token as request param to validate here???

  /**
   * Method to discover and retrieve all the possible moves for the specific player in specific game session.
   *
   * @param gameid     game ID of the game being played
   * @param playerName player whose turn it currently is
   * @return the possible moves that can be made in a map w/ key = hash of move object, value = raw move object.
   */
  @GetMapping(value = "/api/games/{gameid}/players/{player}/actions",
              produces = "application/json; charset=utf-8")
  public ResponseEntity getMoves(@PathVariable(name = "gameid") long gameid,
                                 @PathVariable(name = "player") String playerName
  ) {
    try {
      // if the given gameid doesn't exist or isn't active then throw an error
      if (!BroadcastManager.exists(gameid)) {
        throw new IllegalArgumentException(
            "gameid: " + gameid + " is invalid or represents a game which is not active");
      }

      // get the game manager instance
      GameBoardManager gameBoardManager = BroadcastManager.getActiveGame(gameid).orElseThrow();
      Optional<PlayerWrapper> playerWrapper = gameBoardManager.getSessionInfo()
          .getPlayerByName(playerName);
      String serializedMoves = new Gson().toJson(findMoves(gameBoardManager, playerWrapper));
      return ResponseEntity.status(HttpStatus.OK).body(serializedMoves);
    } catch (IllegalArgumentException | IllegalStateException e) {
      /*
      - if we get here either the game doesnt exist or we've reached a state error somewhere
      - a state error indicates that a game rule / state has been broken ie player has too many
      devs etc
      - send a bad response back indicating the error
       */
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

    }
  }

  /**
   * Finds the moves able to be made for the given player in the given gameboard.
   *
   * @param gameBoardManager game board.
   * @param player           player.
   * @return the possible moves for the player based on the game state.
   * @throws AssertionError if gameBoardManager or player are null.
   */
  private Map<String, Move> findMoves(GameBoardManager gameBoardManager,
                                      Optional<PlayerWrapper> player
  ) {
    assert gameBoardManager != null;

    // handling an empty player which means player is not in the that game
    if (player.isEmpty()) {
      logger.log(Level.INFO, "empty player -> no moves to be made");
      return new LinkedHashMap<>();
    }

    // extracting the player wrapper since we know if we get here it exists
    PlayerWrapper playerWrapper = player.get();

    // if game finished return empty set := no moves
    if (gameBoardManager.getBoard().isFinished()) {
      logger.log(Level.INFO, "game is finished, no moves can be made");
      return new LinkedHashMap<>();
      // if its not the players turn then return no moves
    } else if (!gameBoardManager.getTurnManager().whoseTurn().getName()
        .equals(playerWrapper.getName())) {
      logger.log(Level.INFO, "not " + playerWrapper.getName() + " turn -> no moves can be made");
      return new LinkedHashMap<>();
    }

    // now search through the gameboard and create a mapping of viable moves the player can make given their state
    // TODO: find the players inventory from the game, scan over their tokens and cards to ascertain what moves are possible
    GameBoard gameBoard = gameBoardManager.getBoard();
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
      if (inventory.canAffordCard(faceUp)) {
        Move   move    = new Move(Action.PURCHASE_DEV, faceUp, player, null, null);
        String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move)).toUpperCase();
        moveMap.put(moveMd5, move);
      }
    }

    // cards in players inventory if any
    if (inventory.hasCards()) {
      // now check if they can afford them
      for (Card reservedCard : inventory) {
        if (inventory.canAffordCard(reservedCard)) {
          Move   move    = new Move(Action.PURCHASE_DEV, reservedCard, player, null, null);
          String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move)).toUpperCase();
          moveMap.put(moveMd5, move);
        }
      }
    }
  }


  private void getReserveDevMoves(Map<String, Move> moveMap, UserInventory inventory,
                                  GameBoard gameBoard, PlayerWrapper player
  ) {
    // players may not have more than three reserved cards in hand
    final int maxNumReservedCards = 3;
    if (inventory.cardCount() > maxNumReservedCards) {
      throw new IllegalStateException(
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
      Move   takeFaceUp    = new Move(action, card, player, null, null);
      String takeFaceUpMd5 = DigestUtils.md2Hex(new Gson().toJson(takeFaceUp)).toUpperCase();
      moveMap.put(takeFaceUpMd5, takeFaceUp);
    }
    // or can take from one of the decks, but they won't be able to see the card, so it'll be null
    // ,but they will see the different deck levels (1, 2, 3)
    for (Deck deck : gameBoard.getDecks()) {
      Move   takeFromDeck    = new Move(action, null, player, deck.getLevel(), null);
      String takeFromDeckMd5 = DigestUtils.md2Hex(new Gson().toJson(takeFromDeck)).toUpperCase();
      moveMap.put(takeFromDeckMd5, takeFromDeck);
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
    List<TokenType> validTokenTypes = new ArrayList<>();
    for (TokenPile tokenPile : gameBoard.getTokenPiles()) {
      // can take 3 tokens of different colors as long as pile isn't empty
      if (tokenPile.getSize() > 0) {
        validTokenTypes.add(tokenPile.getType());
      }
      // can only pick 2 of same color if there are at least 4 of that color available
      if (tokenPile.getSize() >= 4) {
        Move move;
        if (inventory.tokenCount() <= 8) {
          move = new Move(
              Action.TAKE_2_GEM_TOKENS_SAME_COL, null, player, null, tokenPile.getType());
        } else if (inventory.tokenCount() == 9) {
          // must return token after the 2 tokens are taken
          move = new Move(Action.TAKE_2_GEM_TOKENS_SAME_COL_RET_1, null, player, null,
                          tokenPile.getType()
          );
        } else if (inventory.tokenCount() == 10) {
          move = new Move(Action.TAKE_2_GEM_TOKENS_SAME_COL_RET_2, null, player, null,
                          tokenPile.getType()
          );
        } else {
          throw new IllegalStateException(
              "Inventory of " + player.getName() + " cannot exceed 10 tokens");
        }

        String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move)).toUpperCase();
        moveMap.put(moveMd5, move);
      }
    }

    // creating the take 3 diff gem tokens moves for all tokens that can be drawn from
    if (!validTokenTypes.isEmpty()) {
      int               numTokensTake  = 3;
      List<TokenType[]> possibleTokens = generateTokenCombinations(validTokenTypes, numTokensTake);

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
        throw new IllegalStateException(
            "Inventory of " + player.getName() + " cannot exceed 10 tokens");
      }

      for (TokenType[] tokenTypes : possibleTokens) {
        Move   move    = new Move(action, null, player, null, tokenTypes);
        String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move)).toUpperCase();
        moveMap.put(moveMd5, move);
      }


    }
  }

}
