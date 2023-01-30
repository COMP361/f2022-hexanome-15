package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/**
 * Model of the gameboard. Necessary due to permanence requirement. 
 * Only ever used for initial setup, the rest can be taken care of by the actions. 
 *
 * @author lawrenceberardelli
 */
public class GameBoard {
  
  private List<UserInventory> inventories;
  private List<Deck> decks;
  private List<Card> cardField;
  private List<TokenPile> tokenPiles;
  private List<Noble> nobles;

  /**
   * Creates a gameboard.
   *
   * @param inventories The player inventories in the game
   * @param decks The decks in the game
   * @param cardField The cards dealt onto the gameboard
   * @param tokenPiles The token piles that are on the gameboard
   * @param nobles The nobles in the game
   */
  public GameBoard(List<UserInventory> inventories, List<Deck> decks,
                   List<Card> cardField, List<TokenPile> tokenPiles, List<Noble> nobles) {
    this.inventories = inventories;
    this.decks = decks;
    this.cardField = cardField;
    this.tokenPiles = tokenPiles;
    this.nobles = nobles;
  }


}
