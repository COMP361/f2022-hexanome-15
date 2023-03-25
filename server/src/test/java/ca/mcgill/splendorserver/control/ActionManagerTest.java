package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.gameio.*;
import ca.mcgill.splendorserver.model.GameBoard;
import ca.mcgill.splendorserver.model.IllegalGameStateException;
import ca.mcgill.splendorserver.model.SplendorGame;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cards.CardStatus;
import ca.mcgill.splendorserver.model.cards.Deck;
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

import static ca.mcgill.splendorserver.model.action.Action.PURCHASE_DEV;
import static ca.mcgill.splendorserver.model.cards.CardStatus.NONE;
import static ca.mcgill.splendorserver.model.cards.CardStatus.RESERVED;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE2;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.BLUE;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static ca.mcgill.splendorserver.model.tradingposts.Power.PURCHASE_CARD_TAKE_TOKEN;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class ActionManagerTest {
  /*@Test
  void performAction() {
    Logger logger = Logger.getAnonymousLogger();
    PlayerWrapper aPlayer = PlayerWrapper.newPlayerWrapper("Slava");
    PlayerWrapper aPlayer2 = PlayerWrapper.newPlayerWrapper("Larry");
    List<PlayerWrapper> lpw = new ArrayList<>();
    lpw.add(aPlayer);
    lpw.add(aPlayer2);
    Player player1 = new Player("Slava", "yellow");
    Player player2 = new Player("Larry", "orange");
    List<Player> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);
    SessionInfo si = new SessionInfo("",players,lpw,aPlayer,"1L");
    String playerName = "Slava";

    GameRestController gRC = new GameRestController();
    gRC.launchRequest(1L,si.toString());

    CardCost cardc = new CardCost(1,0,0,0,0);
    Card acard = new Card(1,2,DIAMOND,BASE2,ONE,cardc);
    acard.setCardStatus(CardStatus.RESERVED);

    Noble anoble = new Noble(1,cardc);

    List<TokenPile> ltp = new ArrayList<>();
    TokenPile tp1 = new TokenPile(DIAMOND);
    Token t1 = new Token(DIAMOND);
    tp1.addToken(t1); tp1.addToken(t1); tp1.addToken(t1); tp1.addToken(t1);
    ltp.add(tp1);
    Optional<CoatOfArmsType> coat = Optional.of(RED);
    UserInventory uinv = new UserInventory(aPlayer,coat);

    TurnManager tm = new TurnManager();
    List<UserInventory> inventories = new ArrayList<>();
    inventories.add(uinv);

    List<Deck> decks = new ArrayList<>();
    Deck adeck = new Deck(BASE2);
    decks.add(adeck);

    List<Card> cardField = new ArrayList<>();
    cardField.add(acard);

    List<TokenPile> tokenPiles = new ArrayList<>();
    tokenPiles.add(tp1);

    List<Noble> nobles = new ArrayList<>();
    nobles.add(anoble);

    TradingPostSlot aTPS = new TradingPostSlot(1,false,PURCHASE_CARD_TAKE_TOKEN,cardc);
    List<TradingPostSlot> tradingPostSlots = new ArrayList<>();
    tradingPostSlots.add(aTPS);

    City acity = new City(1,2,cardc, 0);
    List<City> cities = new ArrayList<>();
    cities.add(acity);
    cardField.add(acard);

    GameBoard gb = new GameBoard(inventories,decks,cardField,tokenPiles,nobles,tradingPostSlots,cities);
    SplendorGame splendorGame = new SplendorGame(si,1L);
    splendorGame.getBoard().getCards().add(acard);

    Optional<PlayerWrapper> playerWrapper = splendorGame.getPlayerByName(playerName);

    TokenType tkps = DIAMOND;

    Move amove = new Move(PURCHASE_DEV,acard,aPlayer,BASE2,anoble,aTPS,tkps, acity);
    Map<String, Move> moves = new HashMap<>();
    moves.put("Slava",amove);

    acard.setCardStatus(NONE);
    Move selectedMove = moves.get("Slava");
    Action pendingAction = splendorGame.getBoard()
      .applyMove(selectedMove, playerWrapper.orElseThrow(
        () -> new IllegalGameStateException(
          "If a valid move has been selected, "
            + "there must be a corresponding player "
            + "who selected it "
            + "but player was found empty")));

    SplendorGame sg = new SplendorGame(si,1L);
    ActionManager aM = new ActionManager();

    assertEquals
      (
      ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(pendingAction.toString()),
      aM.performAction(1L,"Slava","Purchase Cards","Slava"),
      ""
      );
  }*/

  @Test
  void getAvailableActions() {

  }
}