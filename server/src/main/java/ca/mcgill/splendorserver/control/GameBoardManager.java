package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.games.PlayerWrapper;
import ca.mcgill.splendorserver.games.TurnManager;
import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardType;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.DeckLevel;
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

  /**
   * GameBoardManager constructor.
   *
   * @param info   session info, cannot be null
   * @param gameId gameID
   */
  public GameBoardManager(SessionInfo info, long gameId) {
    assert info != null;
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

  private void setUpPlayingField(List<Card> playingField, List<Deck> decks) {
    for (int i = 0; i < DeckLevel.values().length; ++i) {
      Deck deck = new Deck(CardType.values()[i], DeckLevel.values()[i]);
      List<Card> cards = deck.deal();
      playingField.addAll(cards);
      decks.add(deck);
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
    List<TokenPile> piles = new ArrayList<>();
    List<Deck> decks = new ArrayList<>();
    List<Card> playingField = new ArrayList<>();
    List<UserInventory> inventories = new ArrayList<>();
    setUpPlayingField(playingField, decks);
    setUpTokenPiles(piles, true);
    setUpUserInventories(inventories);

    List<Noble> nobles = new ArrayList<>();
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
