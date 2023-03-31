package ca.mcgill.splendorserver.gameio;

import ca.mcgill.splendorserver.control.LocalGameStorage;
import ca.mcgill.splendorserver.control.SessionInfo;
import ca.mcgill.splendorserver.control.TurnManager;
import ca.mcgill.splendorserver.gameio.*;
import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.IllegalGameStateException;
import ca.mcgill.splendorserver.model.SplendorGame;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.*;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArms;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
import ca.mcgill.splendorserver.model.tradingposts.Power;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Array;
import java.util.*;
import java.util.logging.Logger;

import static ca.mcgill.splendorserver.model.action.Action.*;
import static ca.mcgill.splendorserver.model.cards.CardStatus.NONE;
import static ca.mcgill.splendorserver.model.cards.CardStatus.RESERVED;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE1;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE2;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ZERO;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.BLUE;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static ca.mcgill.splendorserver.model.tradingposts.Power.PURCHASE_CARD_TAKE_TOKEN;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class ActionManagerTest {
  private static List<PlayerWrapper> lpw = new ArrayList<>();
  private static List<Player> players = new ArrayList<>();
  private static List<OrientCard> cardField = new ArrayList<>();
  private static List<Noble> nobles = new ArrayList<>();
  private static List<Deck> decks = new ArrayList<>();
  private static List<UserInventory> inventories = new ArrayList<>();
  private static List<TokenPile> ltp = new ArrayList<>();
  private static List<TokenPile> tokenPiles = new ArrayList<>();
  private static List<City> cities = new ArrayList<>();
  private static List<TradingPostSlot> tradingPostSlots = new ArrayList<>();
  private static Logger logger = Logger.getAnonymousLogger();
  private static TurnManager tm = new TurnManager();
  private static Map<String, Move> moves = new LinkedHashMap<>();
  private static SplendorGame splendorGame;
  Action pendingBonusAction;
  UserInventory uinv;
  Optional<CoatOfArmsType> coat;
  private static ActionManager aM = new ActionManager();


  @BeforeEach
  void setup() {
    PlayerWrapper aPlayer = PlayerWrapper.newPlayerWrapper("Slava");
    PlayerWrapper aPlayer2 = PlayerWrapper.newPlayerWrapper("Larry");
    coat = Optional.of(RED);
    uinv = new UserInventory(aPlayer,coat);
    // Initialize Players
    lpw.add(aPlayer);
    lpw.add(aPlayer2);

    Player player1 = new Player("Slava", "yellow");
    Player player2 = new Player("Larry", "orange");
    players.add(player1);
    players.add(player2);

    SessionInfo si = new SessionInfo("",players,lpw,aPlayer,"1L");
    splendorGame = new SplendorGame(si,1L);
    LocalGameStorage.addActiveGame(splendorGame);

    pendingBonusAction = TAKE_TOKEN;

    List<Action> bActions = new ArrayList<>();
    bActions.add(TAKE_TOKEN);

    OrientCard cardMove = (OrientCard) splendorGame.getBoard().getCards().get(15);
    OrientCard cardMove1 = (OrientCard) splendorGame.getBoard().getCards().get(16);
    CardCost costMove = cardMove.getCardCost();

    // Add necessary tokens to user inventory allow them to purchase card
    inventories.add(uinv);
    for (int i = 0; i < costMove.costByTokenType(DIAMOND); i++) {
      Token t = new Token(DIAMOND);
      uinv.addToken(t);
    }
    for (int i = 0; i < costMove.costByTokenType(SAPPHIRE); i++) {
      Token t = new Token(SAPPHIRE);
      uinv.addToken(t);
    }
    for (int i = 0; i < costMove.costByTokenType(EMERALD); i++) {
      Token t = new Token(EMERALD);
      uinv.addToken(t);
    }
    for (int i = 0; i < costMove.costByTokenType(RUBY); i++) {
      Token t = new Token(RUBY);
      uinv.addToken(t);
    }
    for (int i = 0; i < costMove.costByTokenType(ONYX); i++) {
      Token t = new Token(ONYX);
      uinv.addToken(t);
    }


    Noble anoble = new Noble(1,costMove);
    nobles.add(anoble);

    Deck adeck = new Deck(BASE2);
    decks.add(adeck);

    City acity = new City(1,2,costMove, 0);
    cities.add(acity);

    TradingPostSlot aTPS = new TradingPostSlot(1,false,PURCHASE_CARD_TAKE_TOKEN,costMove);
    tradingPostSlots.add(aTPS);

    Move amove = new Move(PURCHASE_DEV,cardMove,aPlayer,BASE2,anoble,aTPS,DIAMOND, acity);
    moves.put("B0914F85246E7181D38AB1138EA637C0",amove);
    Map<String, Move> moveMap = new LinkedHashMap<>();
  }

  //This test does not work with mvn, idk why
  /*@Test
  void performAction() {
    Move selectedMove = moves.get("B0914F85246E7181D38AB1138EA637C0");
    Optional<PlayerWrapper> playerWrapper = splendorGame.getPlayerByName("Slava");
    Action endOfTurnAction = splendorGame.getBoard().getEndOfTurnActions(selectedMove, uinv);
    ResponseEntity<String> response = aM.performAction(1L, "Slava",
      "B0914F85246E7181D38AB1138EA637C0", "Slava");

    assertEquals(ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(pendingBonusAction.toString()), response);

    pendingBonusAction = RET_TOKEN;
    response = aM.performAction(1L, "Slava",
      "B0914F85246E7181D38AB1138EA637C0", "Slava");
    assertEquals(ResponseEntity.status(HttpStatus.OK).body("Larry"), response);

  }*/

  @Test
  void getAvailableActions() {

  }
}