package ca.mcgill.splendorserver.control;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardType;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;

public class GameBoardManager {
  
  private GameBoard board;
  private SessionInfo sessionInfo;
  private long gameId;
  
  public GameBoardManager(SessionInfo info, long gameId) {
    sessionInfo = info;
    this.gameId = gameId;
    instantiateNewGameboard();
  }
  
  public long getGameId() {
    return gameId;
  }
  
  public GameBoard getBoard() {
    return board;
  }
  
  private void setUpPlayingField(List<Card> playingField, List<Deck> decks) {
    for (int i = 0; i < 3; ++i) {
      Deck deck = new Deck(CardType.values()[i]);
      List<Card> cards = deck.deal();
      for (Card card : cards) {
        playingField.add(card);
      }
    }
  }
  
  private void setUpTokenPiles(List<TokenPile> piles, boolean bSetUp) {
    for (int i = 0; i < TokenType.values().length; ++i) {
      TokenPile pile = new TokenPile(TokenType.values()[i]);
      if (bSetUp) {
        pile.setUp(sessionInfo.getNumPlayers());
      }
      piles.add(pile);
    }
  }
  
  private void setUpUserInventories(List<UserInventory> inventories) {
    for (String playerName : sessionInfo) {
      List<TokenPile> piles = new ArrayList<TokenPile>();
      setUpTokenPiles(piles, false);
      inventories.add(new UserInventory(piles, playerName));
    }
  }
  
  private void instantiateNewGameboard() {
    List<TokenPile> piles = new ArrayList<TokenPile>();
    List<Deck> decks = new ArrayList<Deck>();
    List<Card> playingField = new ArrayList<Card>();
    List<UserInventory> inventories = new ArrayList<UserInventory>();
    List<Noble> nobles = new ArrayList<Noble>();
    setUpPlayingField(playingField, decks);
    setUpTokenPiles(piles, true);
    setUpUserInventories(inventories);
    board = new GameBoard(inventories, decks, playingField, piles, nobles);
  }

}
