package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.control.TurnManager;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.DeckType;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * SplendorGame object which keeps track of all game info including the session info,
 * game id, the turn manage, and the game board.
 */
@Entity
public class SplendorGame {

  @Embedded
  private final SessionInfo sessionInfo;
  @Id
  private final long        gameId;
  @Embedded
  private final TurnManager turnManager;
  @Embedded
  private       GameBoard   board;
  private       boolean     finished = false;
  private boolean requiresUpdate;

  /**
   * SplendorGame constructor.
   *
   * @param info   session info, cannot be null
   * @param gameId gameID
   */
  public SplendorGame(SessionInfo info, long gameId) {
    assert info != null;
    sessionInfo = info;
    this.gameId = gameId;
    turnManager = new TurnManager(info.getPlayers());
    requiresUpdate = true;
    instantiateNewGameboard();
  }

  /**
   * Checks if the game requires an update.
   *
   * @return a boolean determining if the game requires an update
   */
  public boolean getRequiresUpdate() {
    return requiresUpdate;
  }

  /**
   * Sets the requiresUpdate boolean.
   *
   * @param requiresUpdate the boolean to be set
   */
  public void setRequiresUpdate(boolean requiresUpdate) {
    this.requiresUpdate = requiresUpdate;
  }

  /**
   * Checks if the game is finished.
   *
   * @return a boolean determining if the game is finished
   */
  public boolean isFinished() {
    return finished;
  }

  /**
   * Sets the finished boolean to true.
   */
  public void setFinished() {
    finished = true;
  }

  /**
   * Returns the player whose turn it is.
   *
   * @return the player whose turn it is
   */
  public PlayerWrapper whoseTurn() {
    return turnManager.whoseTurn();
  }

  /**
   * Checks if the given player is the player who starts the game.
   *
   * @param player the given player
   * @return a boolean determining if the given player is the player who starts the game
   */
  public boolean isStartingPlayer(PlayerWrapper player) {
    return sessionInfo.getGameCreator().equals(player);
  }

  /**
   * Checks that it is the passed players turn and then ends their turn, advancing
   * whose turn.
   *
   * @param currentPlayer the player which is calling this, has to be the current player
   * @return the player whose turn it is after the turn has been ended
   */
  public PlayerWrapper endTurn(PlayerWrapper currentPlayer) {
    assert currentPlayer != null;
    // checking that given player is the current player and thus valid to end their turn
    if (currentPlayer != whoseTurn()) {
      throw new IllegalGameStateException(
          currentPlayer + " cannot end their turn while " + whoseTurn() + " is the current player");
    }

    return this.turnManager.endTurn();
  }

  /**
   * Retrieves the player with the given username and returns it.
   *
   * @param name the player's username
   * @return the player
   */
  public Optional<PlayerWrapper> getPlayerByName(String name) {

    return sessionInfo.getPlayerByName(name);
  }

  /**
   * Returns the game id.
   *
   * @return the game id
   */
  @Id
  public long getGameId() {

    return gameId;
  }

  /**
   * Returns the game board of the game.
   *
   * @return the game board
   */
  public GameBoard getBoard() {
    return board;
  }

  /**
   * Sets up the playing field upon starting the game.
   *
   * @param playingField the cards on the game board
   * @param decks the decks on the game board
   */
  private void setUpPlayingField(List<Card> playingField, List<Deck> decks) {
    for (int i = 0; i < DeckType.values().length; ++i) {
      Deck       deck  = new Deck(DeckType.values()[i]);
      List<Card> cards = deck.deal();
      playingField.addAll(cards);
      decks.add(deck);
    }
  }

  /**
   * Sets up the token piles upon starting the game.
   *
   * @param piles the token piles
   * @param setUp a boolean determining if tokens need to be added to the token piles
   */
  private void setUpTokenPiles(List<TokenPile> piles, boolean setUp) {
    for (int i = 0; i < TokenType.values().length; ++i) {
      TokenPile pile = new TokenPile(TokenType.values()[i]);
      if (setUp) {
        pile.setUp(sessionInfo.getNumPlayers());
      }
      piles.add(pile);
    }
  }

  /**
   * Sets up the user inventories upon starting the game.
   *
   * @param inventories the user inventories in the game
   */
  private void setUpUserInventories(List<UserInventory> inventories) {
    for (PlayerWrapper playerName : sessionInfo) {
      inventories.add(new UserInventory(playerName, Optional.empty()));
    }
  }

  /**
   * Sets up the user inventories upon starting the game.
   * This is used strictly for trading posts expansion.
   *
   * @param inventories the user inventories in the game
   */
  private void setUpUserInventoriesTradingPosts(List<UserInventory> inventories) {
    int i = 0;
    for (PlayerWrapper playerName : sessionInfo) {
      inventories.add(new UserInventory(playerName,
          Optional.ofNullable(CoatOfArmsType.values()[i])));
      i++;
    }
  }

  /**
   * Instantiates the game board upon starting the game.
   */
  private void instantiateNewGameboard() {
    List<TokenPile>     piles        = new ArrayList<>();
    List<Deck>          decks        = new ArrayList<>();
    List<Card>          playingField = new ArrayList<>();
    List<UserInventory> inventories  = new ArrayList<>();
    List<TradingPostSlot> tradingPostSlots = new ArrayList<>();
    List<City> cities = new ArrayList<>();
    setUpPlayingField(playingField, decks);
    setUpTokenPiles(piles, true);

    if (sessionInfo.getGameServer().equals("SplendorOrientTradingPosts")) {
      setUpUserInventoriesTradingPosts(inventories);
      tradingPostSlots = TradingPostSlot.getTradingPostSlots();
    } else if (sessionInfo.getGameServer().equals("SplendorOrientCities")) {
      setUpUserInventories(inventories);
      cities = City.getCities(sessionInfo.getNumPlayers());
    } else {
      setUpUserInventories(inventories);
    }

    List<Noble> nobles = Noble.getNobles(sessionInfo.getNumPlayers());
    board = new GameBoard(inventories, decks, playingField,
      piles, nobles, tradingPostSlots, cities);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SplendorGame that)) {
      return false;
    }
    return getGameId() == that.getGameId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getGameId());
  }
}
