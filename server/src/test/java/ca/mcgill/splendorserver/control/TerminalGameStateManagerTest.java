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

  private SplendorGame game1;
  private SplendorGame game2;
  PlayerWrapper larry;
  PlayerWrapper zach;

  @BeforeEach
  void setUp() {
    List<PlayerWrapper> lp = new ArrayList<>();
    larry = PlayerWrapper.newPlayerWrapper("Larry");
    zach = PlayerWrapper.newPlayerWrapper("Zach");
    lp.add(larry);
    lp.add(zach);
    List<Player> players = new ArrayList<>();
    Player player1 = new Player("Larry", "orange");
    Player player2 = new Player("Zach", "green");
    players.add(player1);
    players.add(player2);
    SessionInfo sessionInfo1 = new SessionInfo("SplendorOrient",players, lp, larry,"");
    game1 = new SplendorGame(sessionInfo1,1L);
    SessionInfo sessionInfo2 = new SessionInfo("SplendorOrientCities",players, lp, larry,"");
    game2 = new SplendorGame(sessionInfo2,1L);
  }

  @Test
  void isNotTerminalGameState() {
    assertFalse(TerminalGameStateManager.isTerminalGameState(game1));
  }

  @Test
  void isTerminalGameState() {
    UserInventory inventory1 = game1.getBoard().getInventoryByPlayerName("Larry").get();
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card = new Card(0,15, DIAMOND, BASE1, ONE, cost);
    Token token = new Token(DIAMOND);
    inventory1.addToken(token);
    inventory1.purchaseCard(card);
    game1.endTurn(larry);
    assertTrue(TerminalGameStateManager.isTerminalGameState(game1));
  }

  @Test
  void isTerminalGameStateCities() {
    UserInventory inventory1 = game2.getBoard().getInventoryByPlayerName("Larry").get();
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card = new Card(0,15, DIAMOND, BASE1, ONE, cost);
    Token token = new Token(DIAMOND);
    inventory1.addToken(token);
    inventory1.purchaseCard(card);
    City city = new City(0, 0, cost, 0);
    inventory1.addCity(city);
    game2.endTurn(larry);
    assertTrue(TerminalGameStateManager.isTerminalGameState(game2));
  }
}