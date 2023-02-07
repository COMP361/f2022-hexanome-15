package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.games.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.DeckLevel;
import ca.mcgill.splendorserver.model.cards.Noble;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
  private       boolean                       finished = false;
  private final Logger                        logger   = Logger.getAnonymousLogger();

  /**
   * Creates a gameboard.
   *
   * @param inventories The player inventories in the game
   * @param decks       The decks in the game
   * @param cardField   The cards dealt onto the gameboard
   * @param tokenPiles  The token piles that are on the gameboard
   * @param nobles      The nobles in the game
   */
  public GameBoard(List<UserInventory> inventories, List<Deck> decks, List<Card> cardField,
                   List<TokenPile> tokenPiles, List<Noble> nobles
  ) {
    this.inventories = inventories;
    this.decks       = decks;
    this.cardField   = cardField;
    this.tokenPiles  = new EnumMap<>(tokenPiles.stream()
                                               .collect(Collectors.toMap(
                                                   TokenPile::getType,
                                                   tokens -> tokens
                                               )));
    this.nobles      = nobles;
  }

  /**
   * Applies the given move to this game board model. Note that we can assume that for any move that
   * requires drawing from a deck, that the deck will not be empty otherwise it wouldn't have been
   * offered as a valid move.
   *
   * @param move   selected move, must be valid move, cannot be null
   * @param player player whose turn it is, cannot be null
   */
  public void applyMove(Move move, PlayerWrapper player) {
    assert move != null && player != null;
    // getting players inventory or throws exception if not there
    UserInventory inventory = getInventoryByPlayerName(player.getName()).orElseThrow(
        () -> new IllegalArgumentException(
            "player (" + player.getName() + ") wasn't found in this current game board"));

    switch (move.getAction()) {
      case PURCHASE_DEV -> {
        // checking to see whether they're buying from reserved card in hand / table or from deck
        Card selectedCard = move.getCard()
                                .orElseThrow(() -> new IllegalGameStateException(
                                    "if selected move is to purchase a dev card, there needs to be a card selected"));
        // check to make sure that the card they wish to purchase from their hand is valid
        if (inventory.hasCardReserved(selectedCard)) {
          // they have the card, and it has been reserved, so they can legally buy it
          // add the card to their inventory as a purchased card
          inventory.addPurchasedCard(selectedCard);
          logger.log(Level.INFO, player + " purchased a reserved dev card: " + selectedCard);
        } else if (cardField.contains(selectedCard)) { // purchase face-up dev card
          // purchase card which is face-up on the board
          inventory.addPurchasedCard(selectedCard);
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

      }
      case RESERVE_DEV -> {
        // no gold token (joker) will be received, just the reserved card
        // throws an error if a card nor deck level to choose from weren't chosen
        inventory.addReservedCard(move.getCard()
                                      .orElse(getCardByDeckLevel(move.getDeckLevel()
                                                                     .orElseThrow(
                                                                         () -> new IllegalGameStateException(
                                                                             "If move is to reserve dev card, a card or deck level to draw a card from must be chosen")))));
      }
      case RESERVE_DEV_TAKE_JOKER -> {
        // gold token and card reserved
        inventory.addReservedCard(move.getCard()
                                      .orElse(getCardByDeckLevel(move.getDeckLevel()
                                                                     .orElseThrow(
                                                                         () -> new IllegalGameStateException(
                                                                             "If move is to reserve dev card, a card or deck level to draw a card from must be chosen")))));
        inventory.addTokens(drawGoldToken());
      }
      case TAKE_2_GEM_TOKENS_SAME_COL -> {
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
      case TAKE_2_GEM_TOKENS_SAME_COL_RET_1 -> {
        // check that tokens selected is not empty
        if (move.getSelectedTokenTypes()
                .isEmpty() || move.getSelectedTokenTypes()
                                  .get().length != 1) {
          throw new IllegalGameStateException(
              "If move is to take 2 gems of same color, then gems needs to be of size 1");
        }

        // check that the token to return is not empty and proper size
        if (move.getReturnedTokenTypes()
                .isEmpty() || move.getReturnedTokenTypes()
                                  .get().length != 1) {
          throw new IllegalGameStateException(
              "If move is to take 2 gems of same color and return 1, then gems to return needs to be of size 1");
        }

        // all good, add the selected token twice, and return the token from inventory to the table
        // we know only 1 element in this array
        TokenType selected = move.getSelectedTokenTypes()
                                 .get()[0];
        inventory.addTokens(drawTokenByTokenType(selected));
        inventory.addTokens(drawTokenByTokenType(selected));
        // return token
        selected = move.getReturnedTokenTypes()
                       .get()[0];
        returnTokensToBoard(inventory, selected);
      }
      case TAKE_2_GEM_TOKENS_SAME_COL_RET_2 -> {
        // check that tokens selected is not empty
        if (move.getSelectedTokenTypes()
                .isEmpty() || move.getSelectedTokenTypes()
                                  .get().length != 1) {
          throw new IllegalGameStateException(
              "If move is to take 2 gems of same color, then gems needs to be of size 1");
        }

        // check that the token to return is not empty and proper size
        if (move.getReturnedTokenTypes()
                .isEmpty() || move.getReturnedTokenTypes()
                                  .get().length != 2) {
          throw new IllegalGameStateException(
              "If move is to take 2 gems of same color and return 2, then gems to return needs to be of size 2");
        }

        // all good, add the selected token twice, and return the token from inventory to the table
        // we know only 1 element in this array
        TokenType selected = move.getSelectedTokenTypes()
                                 .get()[0];
        inventory.addTokens(drawTokenByTokenType(selected));
        inventory.addTokens(drawTokenByTokenType(selected));
        // return the 2 tokens
        TokenType ret1 = move.getReturnedTokenTypes()
                             .get()[0];
        TokenType ret2 = move.getReturnedTokenTypes()
                             .get()[1];
        returnTokensToBoard(inventory, ret1, ret2);
      }
      case TAKE_3_GEM_TOKENS_DIFF_COL -> {
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
      case TAKE_3_GEM_TOKENS_DIFF_COL_RET_1 -> {
        // check that tokens selected is not empty
        if (move.getSelectedTokenTypes()
                .isEmpty() || move.getSelectedTokenTypes()
                                  .get().length != 3) {
          throw new IllegalGameStateException(
              "If move is to take 3 gems of different colors, then gems needs to be of size 3");
        }

        // check that the return token is selected
        if (move.getReturnedTokenTypes()
                .isEmpty() || move.getReturnedTokenTypes()
                                  .get().length != 1) {
          throw new IllegalGameStateException(
              "If move is to take 3 gems of different colors and return 1 token, then selected gems needs to be of size 3 and returned gems of size 1");
        }

        // all good, add the selected tokens
        TokenType[] selected = move.getSelectedTokenTypes()
                                   .get();
        moveTokensToUserInventory(inventory, selected);
        // return the selected
        returnTokensToBoard(inventory, move.getReturnedTokenTypes()
                                           .get());
      }
      case TAKE_3_GEM_TOKENS_DIFF_COL_RET_2 -> {
        // check that tokens selected is not empty
        if (move.getSelectedTokenTypes()
                .isEmpty() || move.getSelectedTokenTypes()
                                  .get().length != 3) {
          throw new IllegalGameStateException(
              "If move is to take 3 gems of different colors, then gems needs to be of size 3");
        }

        // check that the return token is selected
        if (move.getReturnedTokenTypes()
                .isEmpty() || move.getReturnedTokenTypes()
                                  .get().length != 2) {
          throw new IllegalGameStateException(
              "If move is to take 3 gems of different colors and return 2 token, then selected gems needs to be of size 3 and returned gems of size 2");
        }

        // all good, add the selected tokens
        moveTokensToUserInventory(inventory, move.getSelectedTokenTypes()
                                                 .get());
        // return the selected
        returnTokensToBoard(inventory, move.getReturnedTokenTypes()
                                           .get());
      }
      case TAKE_3_GEM_TOKENS_DIFF_COL_RET_3 -> {
        // check that tokens selected is not empty
        if (move.getSelectedTokenTypes()
                .isEmpty() || move.getSelectedTokenTypes()
                                  .get().length != 3) {
          throw new IllegalGameStateException(
              "If move is to take 3 gems of different colors, then gems needs to be of size 3");
        }

        // check that the return token is selected
        if (move.getReturnedTokenTypes()
                .isEmpty() || move.getReturnedTokenTypes()
                                  .get().length != 3) {
          throw new IllegalGameStateException(
              "If move is to take 3 gems of different colors and return 3 token, then selected gems needs to be of size 3 and returned gems of size 3");
        }

        // all good, add the selected tokens
        moveTokensToUserInventory(inventory, move.getSelectedTokenTypes()
                                                 .get());
        // return the selected
        returnTokensToBoard(inventory, move.getReturnedTokenTypes()
                                           .get());
      }
      default -> throw new IllegalGameStateException(
          "Expected an instance of type Action instead found: " + move.getAction());


    }
  }

  private void moveTokensToUserInventory(UserInventory inventory, TokenType[] selected) {
    for (TokenType tokenType : selected) {
      inventory.addTokens(drawTokenByTokenType(tokenType));
    }
  }

  private void returnTokensToBoard(UserInventory inventory, TokenType... selected) {
    for (TokenType tokenType : selected) {
      tokenPiles.get(tokenType)
                .addToken(inventory.removeTokenByTokenType(tokenType));
    }
  }

  private Card getCardByDeckLevel(DeckLevel deckLevel) {
    assert deckLevel != null;
    for (Deck deck : decks) {
      if (deck.getLevel() == deckLevel) {
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

  public boolean isFinished() {
    return finished;
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
