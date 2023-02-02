package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.games.PlayerWrapper;
import ca.mcgill.splendorserver.games.TurnManager;
import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardType;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Encapsulates a game board and contains functionality to alter and/or view the state of the game.
 */
public class GameBoardManager {

  private final SessionInfo sessionInfo;
  private final long gameId;
  private final TurnManager turnManager;
  private GameBoard board;

  public GameBoardManager(SessionInfo info, long gameId) {
    sessionInfo = info;
    this.gameId = gameId;
    turnManager = new TurnManager(info.getPlayers());
    instantiateNewGameboard();
  }

  public TurnManager getTurnManager() {
    return turnManager;
  }

  public SessionInfo getSessionInfo() {
    return sessionInfo;
  }

  public long getGameId() {
    return gameId;
  }

  public GameBoard getBoard() {
    return board;
  }

  // TODO: do we need the decks parameter???
  private void setUpPlayingField(List<Card> playingField, List<Deck> decks) {
    for (int i = 0; i < 3; ++i) {
      Deck deck = new Deck(CardType.values()[i]);
      List<Card> cards = deck.deal();
      playingField.addAll(cards);
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
    for (PlayerWrapper playerName : sessionInfo) {
      List<TokenPile> piles = new ArrayList<>();
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GameBoardManager that)) {
      return false;
    }
    return getGameId() == that.getGameId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionInfo, getGameId());
  }
}
