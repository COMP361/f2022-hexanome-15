package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.InventoryJson;
import ca.mcgill.splendorserver.model.SplendorGame;
import ca.mcgill.splendorserver.model.TradingPostJson;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.savegame.DeckJson;
import ca.mcgill.splendorserver.model.savegame.GameBoardJson;
import ca.mcgill.splendorserver.model.savegame.SaveGame;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArms;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SaveGameStorageTest {

  private String id;
  private String json;
  private SaveGame savegame;

  @Test
  void getSaveGame() {
    PlayerWrapper sofia = PlayerWrapper.newPlayerWrapper("Sofia");
    PlayerWrapper jeff = PlayerWrapper.newPlayerWrapper("Jeff");
    Player player1 = new Player("Sofia", "purple");
    Player player2 = new Player("Jeff", "blue");
    List<Player> playerList = new ArrayList<>();
    playerList.add(player1);
    playerList.add(player2);
    List<PlayerWrapper> players = new ArrayList<>();
    players.add(sofia);
    players.add(jeff);
    SessionInfo sessionInfo = new SessionInfo("SplendorOrientTradingPosts", playerList, players, sofia,"");
    SplendorGame game = new SplendorGame(sessionInfo, 1L);

    List<InventoryJson> inventoriesJson = new ArrayList<>();
    GameBoard gameboard = game.getBoard();
    for (UserInventory inventory : gameboard.getInventories()) {
      InventoryJson inventoryJson = new InventoryJson(inventory.getCards(),
        inventory.getTokenPiles(), inventory.getPlayer().getName(),
        inventory.getPrestigeWon(), inventory.getNobles(),
        inventory.getPowers(), inventory.getCoatOfArmsPile(),
        inventory.getCities(), null);
      inventoriesJson.add(inventoryJson);
    }
    List<DeckJson> decksJson = new ArrayList<>();
    for (Deck deck : gameboard.getDecks()) {
      decksJson.add(new DeckJson(deck));
    }
    List<Integer> nobles = new ArrayList<>();
    for (Noble noble : gameboard.getNobles()) {
      nobles.add(noble.getId());
    }
    List<Integer> cardField = new ArrayList<>();
    for (Card card : gameboard.getCards()) {
      cardField.add(card.getId());
    }
    List<TradingPostJson> tradingPosts = new ArrayList<>();
    for (TradingPostSlot tradingPostSlot : gameboard.getTradingPostSlots()) {
      List<CoatOfArmsType> coatOfArmsTypes = new ArrayList<>();
      for (CoatOfArms coatOfArms : tradingPostSlot.getAcquiredCoatOfArmsList()) {
        coatOfArmsTypes.add(coatOfArms.getType());
      }
      tradingPosts.add(new TradingPostJson(tradingPostSlot.getId(), coatOfArmsTypes));
    }
    List<Integer> cities = new ArrayList<>();
    for (City city : gameboard.getCities()) {
      cities.add(city.getId());
    }
    List<String> winningPlayers = new ArrayList<>();
    for (PlayerWrapper player : game.getWinningPlayers()) {
      winningPlayers.add(player.getName());
    }
    GameBoardJson gameBoardJson = new GameBoardJson(game.whoseTurn().getName(), inventoriesJson, decksJson,
      nobles, cardField, gameboard.getTokenPiles(), tradingPosts, cities, winningPlayers);
    id = String.valueOf(new Random().nextInt() & Integer.MAX_VALUE);
    json = new Gson().toJson(gameBoardJson);
    savegame = new SaveGame(id, new Gson().toJson(gameBoardJson), "");

    SaveGameStorage.getInstance().addAndFlushSaveGame(savegame);
    assertEquals(savegame, SaveGameStorage.getInstance().getSaveGame(id));
    SaveGameStorage.getInstance().removeSaveGameFromDb(savegame);
  }
}