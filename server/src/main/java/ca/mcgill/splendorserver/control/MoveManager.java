package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.games.PlayerWrapper;
import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zachary Hayden
 * Date: 2/1/23
 */
@RestController
public class MoveManager {
  private final Logger logger = Logger.getAnonymousLogger();

  private List<TokenType[]> generateTokenCombinations(List<TokenType> tokenTypes, int r) {
    List<TokenType[]> combinations = new ArrayList<>();
    int[] ixs = new int[r]; // keep indexes

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
  @GetMapping(value = "/api/games/{gameid}/players/{player}/actions", produces = "application/json; charset=utf-8")
  public ResponseEntity getMoves(@PathVariable(name = "gameid") long gameid,
                                 @PathVariable(name = "player") String playerName
  ) {
    // if the given gameid doesn't exist or isn't active then throw an error
    if (!BroadcastManager.exists(gameid)) {
      throw new IllegalArgumentException(
          "gameid: " + gameid + " is invalid or represents a game which is not active");
    }

    // get the game manager instance
    GameBoardManager gameBoardManager = BroadcastManager.getActiveGame(gameid).orElseThrow();
    Optional<PlayerWrapper> playerWrapper =
        gameBoardManager.getSessionInfo().getPlayerByName(playerName);
    //String serializedMoves =
    return null;
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
    }
    // if its not the players turn then return no moves
    else if (!gameBoardManager.getTurnManager().whoseTurn().getName()
        .equals(playerWrapper.getName())) {
      logger.log(Level.INFO, "not " + playerWrapper.getName() + " turn -> no moves can be made");
      return new LinkedHashMap<>();
    }

    // now search through the gameboard and create a mapping of viable moves the player can make given their state
    // TODO: find the players inventory from the game, scan over their tokens and cards to ascertain what moves are possible
    GameBoard gameBoard = gameBoardManager.getBoard();
    // we know that the player is in the game if we make it to this point
    UserInventory userInventory =
        gameBoard.getInventoryByPlayerName(playerWrapper.getName()).orElseThrow();

    return null;
  }

  private Map<String, Move> getSelectTokenMoves(Map<String, Move> moveMap, UserInventory inventory,
                                                GameBoard gameBoard, PlayerWrapper player
  ) {
    // checking select token moves:

    // player cannot be left with more than 10 tokens after move is made
    // ie their token count must be <= 7 if select 3 diff, or <= 8 if selecting 2 same
    if (inventory.tokenCount() <= 7) {
      // can either do take 2 (if legal) or take 3 gem tokens

      // list of token types which can be drawn from while selecting 3 tokens from different piles
      List<TokenType> validTokenTypes = new ArrayList<>();
      // can they pick 2 tokens of same color?
      for (TokenPile tokenPile : gameBoard.getTokenPiles()) {
        // can take 3 tokens of different colors as long as pile isn't empty
        if (tokenPile.getSize() > 0) {
          validTokenTypes.add(tokenPile.getType());
        }
        // can only pick 2 of same color if there are at least 4 of that color available
        if (tokenPile.getSize() >= 4) {
          Move move =
              new Move(Action.TAKE_2_GEM_TOKENS_SAME_COL, null, player, tokenPile.getType());
          String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move)).toUpperCase();
          moveMap.put(moveMd5, move);
        }
      }

      // creating the take 3 diff gem tokens moves for all tokens that can be drawn from
      if (!validTokenTypes.isEmpty()) {
        int numTokensTake = 3;
        List<TokenType[]> possibleTokens =
            generateTokenCombinations(validTokenTypes, numTokensTake);
        for (TokenType[] tokenTypes : possibleTokens) {
          Move move = new Move(Action.TAKE_3_GEM_TOKENS_DIFF_COL, null, player, tokenTypes);
          String moveMd5 = DigestUtils.md2Hex(new Gson().toJson(move)).toUpperCase();
          moveMap.put(moveMd5, move);
        }


      }
    }
    return moveMap;
  }

}
