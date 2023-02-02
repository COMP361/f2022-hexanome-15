package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Model of the gameboard. Necessary due to permanence requirement.
 * Only ever used for initial setup, the rest can be taken care of by the actions.
 *
 * @author lawrenceberardelli
 */
public class GameBoard {

  private final List<UserInventory> inventories;
  private final List<Deck> decks;
  private final List<Card> cardField;
  private final List<TokenPile> tokenPiles;
  private final List<Noble> nobles;
  private boolean finished = false;

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
    this.decks = decks;
    this.cardField = cardField;
    this.tokenPiles = tokenPiles;
    this.nobles = nobles;
  }

  public Optional<UserInventory> getInventoryByPlayerName(String playerName) {
    for (UserInventory userInventory : inventories) {
      if (userInventory.getPlayer().getName().equals(playerName)) {
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
    return tokenPiles;
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
