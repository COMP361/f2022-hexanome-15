package comp361.f2022hexanome15.splendorclient.model.cards;

import java.util.List;

import comp361.f2022hexanome15.splendorclient.model.tokens.TokenPile;
import comp361.f2022hexanome15.splendorclient.model.userinventory.UserInventory;

/**
 * Model of the gameboard. Necessary due to permanence requirement. 
 * Only ever used for initial setup, the rest can be taken care of by the actions. 
 * 
 * @author lawrenceberardelli
 *
 */
public class GameBoard {
  
  private List<UserInventory> inventories;
  private List<Deck> decks;
  private List<Card> cardField;
  private List<TokenPile> tokenPiles;
  
  public GameBoard(List<UserInventory> inventories, List<Deck> decks, List<Card> cardField, List<TokenPile> tokenPiles) {
    this.inventories = inventories;
    this.decks = decks;
    this.cardField = cardField;
    this.tokenPiles = tokenPiles;
  }
}
