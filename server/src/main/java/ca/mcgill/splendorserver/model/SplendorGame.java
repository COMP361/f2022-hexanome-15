package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.control.SaveGameStorage;
import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.control.TurnManager;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cards.CardStatus;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cards.DeckType;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.savegame.SaveGame;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
import ca.mcgill.splendorserver.model.tradingposts.Power;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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
    if (info.getSaveGameId().isEmpty()) {
      instantiateNewGameboard();
    } else {
      instantiateGameboardFromSavegame(info.getSaveGameId());
    }
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
   * Returns the winning player at the end of the game.
   *
   * @return the winning player at the end of the game
   */
  public List<PlayerWrapper> getWinningPlayers() {
    List<PlayerWrapper> winningPlayers = new ArrayList<>();
    int highestPrestige = 14;
    int highestPrestigeCities = 0;
    int fewestCards = Integer.MAX_VALUE;
    if (finished) {
      for (PlayerWrapper player : sessionInfo.getPlayers()) {
        UserInventory inventory = getBoard().getInventoryByPlayerName(player.getName()).get();
        if (sessionInfo.getGameServer().equals("SplendorOrientCities")) {
          if (inventory.getCities().size() > 0) {
            if (inventory.getPrestigeWon() > highestPrestigeCities) {
              highestPrestigeCities = inventory.getPrestigeWon();
              fewestCards = inventory.purchasedCardCount();
              int numWinningPlayers = winningPlayers.size();
              for (int i = 0; i < numWinningPlayers; i++) {
                winningPlayers.remove(0);
              }
              winningPlayers.add(player);
            } else if (inventory.getPrestigeWon() == highestPrestigeCities) {
              if (inventory.purchasedCardCount() < fewestCards) {
                fewestCards = inventory.purchasedCardCount();
                int numWinningPlayers = winningPlayers.size();
                for (int i = 0; i < numWinningPlayers; i++) {
                  winningPlayers.remove(0);
                }
                winningPlayers.add(player);
              } else if (inventory.purchasedCardCount() == fewestCards) {
                winningPlayers.add(player);
              }
            }
          }
        } else {
          if (inventory.getPrestigeWon() > highestPrestige) {
            highestPrestige = inventory.getPrestigeWon();
            fewestCards = inventory.purchasedCardCount();
            winningPlayers.add(player);
          } else if (inventory.getPrestigeWon() == highestPrestige) {
            if (inventory.purchasedCardCount() < fewestCards) {
              fewestCards = inventory.purchasedCardCount();
              int numWinningPlayers = winningPlayers.size();
              for (int i = 0; i < numWinningPlayers; i++) {
                winningPlayers.remove(0);
              }
              winningPlayers.add(player);
            } else if (inventory.purchasedCardCount() == fewestCards) {
              winningPlayers.add(player);
            }
          }
        }
      }
    }
    return winningPlayers;
  }

  /**
   * Returns the last player in the game.
   *
   * @return the last player in the game
   */
  public PlayerWrapper getLastPlayer() {
    return sessionInfo.getPlayers().get(sessionInfo.getNumPlayers() - 1);
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
   * Returns the session info of this Splendor game.
   *
   * @return the session info of this Splendor game
   */
  public SessionInfo getSessionInfo() {
    return sessionInfo;
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
      cities = City.getCities();
    } else {
      setUpUserInventories(inventories);
    }

    List<Noble> nobles = Noble.getNobles(sessionInfo.getNumPlayers());
    board = new GameBoard(inventories, decks, playingField,
      piles, nobles, tradingPostSlots, cities);
  }
  
  private void instantiateGameboardFromSavegame(String id) {
    SaveGame savegame = SaveGameStorage.getInstance().getSaveGame(id);
    ca.mcgill.splendorserver.model.savegame.GameBoardJson 
        gameboardJson = 
            new Gson().fromJson(savegame.getJson(), 
                ca.mcgill.splendorserver.model.savegame.GameBoardJson.class);
    List<TokenPile>     piles        = new ArrayList<>();
    List<Deck>          decks        = new ArrayList<>();
    List<Card>          playingField = new ArrayList<>();
    List<TradingPostSlot> tradingPostSlots = new ArrayList<>();
    
    //setting up gameboard
    for (Entry<TokenType, Integer> entry : gameboardJson.tokenField.entrySet()) {
      TokenPile pile = new TokenPile(entry.getKey());
      for (int i = 0; i < entry.getValue(); ++i) {
        pile.addToken(new Token(entry.getKey()));
      }
      piles.add(pile);
    }

    for (ca.mcgill.splendorserver.model.savegame.DeckJson deckJson : gameboardJson.decks) {
      Deck deck = new Deck(deckJson);
      decks.add(deck);
    }
    
    for (Integer cardId : gameboardJson.cardField) {
      if (cardId == -1) {
        //fill with a placeholder
        Card card = new Card(-1, 0, null, null, null,
            new CardCost(100, 100, 100, 100, 100));
        card.setCardStatus(CardStatus.PURCHASED);
        playingField.add(card);
      } else {
        playingField.add(Card.getCard(cardId));
      }
    }
    
    List<City> cities = new ArrayList<>();
    if (sessionInfo.getGameServer().equals("SplendorOrientTradingPosts")) {
      tradingPostSlots = TradingPostSlot.getTradingPostSlots();
    } else if (sessionInfo.getGameServer().equals("SplendorOrientCities")) {
      List<City> citiesInInventories = new ArrayList<>();
      for (InventoryJson inventory : gameboardJson.inventories) {
        for (Integer cityId : inventory.cities) {
          citiesInInventories.add(City.getCity(cityId));
        }
      }
      for (Integer cityId : gameboardJson.cities) {
        cities.add(City.getCity(cityId));
      }
      cities.removeAll(citiesInInventories);
    }
    // Removing the nobles that are in the inventories
    // to instantiate the gameboard only with nobles on the board
    List<Noble> noblesInInventories = new ArrayList<>();
    for (InventoryJson inventory : gameboardJson.inventories) {
      for (Integer nobleId : inventory.visitingNobles) {
        noblesInInventories.add(Noble.getNoble(nobleId));
      }
    }
    List<Noble> nobles = new ArrayList<>();
    for (Integer nobleId : gameboardJson.nobles) {
      nobles.add(Noble.getNoble(nobleId));
    }
    nobles.removeAll(noblesInInventories);
    
    //setting up user inventories
    final List<UserInventory> inventories  = new ArrayList<>();
    int i = 0;
    for (InventoryJson inventoryJson : gameboardJson.inventories) {
      UserInventory inventory;
      if (sessionInfo.getGameServer().equals("SplendorOrientTradingPosts")) {
        inventory = 
            new UserInventory(
                sessionInfo.getPlayers().get(i), 
                Optional.ofNullable(CoatOfArmsType.values()[i]));
      } else {
        inventory = 
            new UserInventory(
                sessionInfo.getPlayers().get(i), 
                Optional.empty());
      }
      for (Integer purchasedCard : inventoryJson.purchasedCards) {
        inventory.addPurchasedCard(Card.getCard(purchasedCard));
      }
      for (Integer reservedCard : inventoryJson.reservedCards) {
        inventory.addReservedCard(Card.getCard(reservedCard));
      }
      for (Entry<TokenType, Integer> tokenEntry : inventoryJson.tokens.entrySet()) {
        for (int j = 0; j < tokenEntry.getValue(); ++j) {
          inventory.addToken(new Token(tokenEntry.getKey()));
        }
      }
      for (Integer nobleId : inventoryJson.reservedNobles) {
        inventory.addReservedNoble(Noble.getNoble(nobleId));
      }
      for (Integer nobleId : inventoryJson.visitingNobles) {
        inventory.receiveVisitFrom(Noble.getNoble(nobleId));
      }
      for (Power power : inventoryJson.powers) {
        inventory.addPower(power);
        for (TradingPostSlot slot : tradingPostSlots) {
          if (slot.getPower() == power) {
            slot.addCoatOfArms(inventory.getCoatOfArmsPile().removeCoatOfArms());
          }
        }
      }
      for (Integer cityId : inventoryJson.cities) {
        inventory.addCity(City.getCity(cityId));
      }
      inventories.add(inventory);
      ++i;
    }
    
    //make the board
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
