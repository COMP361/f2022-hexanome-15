package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.SplendorGame;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE1;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static org.junit.jupiter.api.Assertions.*;

class TerminalGameStateManagerTest {

  private SplendorGame game;

  @BeforeEach
  void setUp() {
    List<PlayerWrapper> lp = new ArrayList<>();
    PlayerWrapper larry = PlayerWrapper.newPlayerWrapper("Larry");
    PlayerWrapper zach = PlayerWrapper.newPlayerWrapper("Zach");
    lp.add(larry);
    lp.add(zach);
    List<Player> players = new ArrayList<>();
    Player player1 = new Player("Larry", "orange");
    Player player2 = new Player("Zach", "green");
    players.add(player1);
    players.add(player2);
    SessionInfo si = new SessionInfo("GameServer",players, lp, larry,"1L");
    game = new SplendorGame(si,1L);
  }

  @Test
  void isNotTerminalGameState() {
    assertFalse(TerminalGameStateManager.isTerminalGameState(game));
  }

  @Test
  void isTerminalGameState() {
    UserInventory inventory1 = game.getBoard().getInventoryByPlayerName("Larry").get();
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card = new Card(0,15, DIAMOND, BASE1, ONE, cost);
    Token token = new Token(DIAMOND);
    inventory1.addToken(token);
    inventory1.purchaseCard(card);
    assertTrue(TerminalGameStateManager.isTerminalGameState(game));
  }

  @Test
  void isTerminalGameStateCities() {
    UserInventory inventory1 = game.getBoard().getInventoryByPlayerName("Larry").get();
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card = new Card(0,0, DIAMOND, BASE1, ONE, cost);
    Token token = new Token(DIAMOND);
    inventory1.addToken(token);
    inventory1.purchaseCard(card);
    City city = new City(0, 0, cost, 0);
    inventory1.addCity(city);
    assertTrue(TerminalGameStateManager.isTerminalGameState(game));
  }
}