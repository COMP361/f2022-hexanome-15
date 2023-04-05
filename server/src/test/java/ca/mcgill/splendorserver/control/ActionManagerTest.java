package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.control.*;
import ca.mcgill.splendorserver.gameio.Player;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.SplendorGame;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.*;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;

import static ca.mcgill.splendorserver.model.action.Action.*;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE1;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ZERO;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.BLUE;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class ActionManagerTest {
  private static List<PlayerWrapper> lpw = new ArrayList<>();
  private static List<Player> players = new ArrayList<>();
  //private static List<OrientCard> cardField = new ArrayList<>();
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
  UserInventory uinv2;
  Optional<CoatOfArmsType> coat;
  Optional<CoatOfArmsType> coat2;
  private static ActionManager aM = new ActionManager();


  @BeforeEach
  void setup() {
  }


  private void affordCard(UserInventory inv, String playerName) {
    OrientCard cardMove = (OrientCard) splendorGame.getBoard().getCards().get(15);
    CardCost costMove = cardMove.getCardCost();

    // Add necessary tokens to user inventory allow them to purchase card
    for (int i = 0; i < costMove.costByTokenType(DIAMOND); i++) {
      Token t = new Token(DIAMOND);
      inv.addToken(t);
    }
    for (int i = 0; i < costMove.costByTokenType(SAPPHIRE); i++) {
      Token t = new Token(SAPPHIRE);
      inv.addToken(t);
    }
    for (int i = 0; i < costMove.costByTokenType(EMERALD); i++) {
      Token t = new Token(EMERALD);
      inv.addToken(t);
    }
    for (int i = 0; i < costMove.costByTokenType(RUBY); i++) {
      Token t = new Token(RUBY);
      inv.addToken(t);
    }
    for (int i = 0; i < costMove.costByTokenType(ONYX); i++) {
      Token t = new Token(ONYX);
      inv.addToken(t);
    }
  }

  private static void gameBoardReflector(String fieldName, Object obj, Object val){
    try {
      // Create Field Object
      Field privateField = splendorGame.getBoard().getClass().getDeclaredField(fieldName);

      // Enable Accessibility
      privateField.setAccessible(true);

      // Modify Field
      // card.getTokenBonusType() == tokenType && card.isPurchased()
      privateField.set(obj, val);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void cardDiscard (int id, PlayerWrapper player, TokenType color, Action discard) {

    CardCost cost = new CardCost(0,0,1,1,1);
    Card card = new Card(id,1,color,BASE1,ONE,cost);

    List<Card> newCard = new ArrayList<>();
    newCard.add(card);
    List<Card> newList = new ArrayList<>();

    newList.addAll(rmElement(splendorGame.getBoard().getCards()));
    newList.addAll(newCard);
    gameBoardReflector("cardField",splendorGame.getBoard(), newList);

    Noble bNoble = new Noble(id,cost);
    TradingPostSlot bTPS = new TradingPostSlot(id,true, Power.PURCHASE_CARD_TAKE_TOKEN,cost);
    City bCity = new City(id,5,cost,2);
    Move bMove = new Move(PURCHASE_DEV,card,player,BASE1,bNoble,bTPS,color,bCity);
    splendorGame.getBoard().applyMove(bMove,player);

    gameBoardReflector("actionPending",splendorGame.getBoard(),discard);
    //System.out.println(splendorGame.getBoard().getPendingAction());
  }

  private static void buyCard (PlayerWrapper player, Card card, Action action, Move move) {

    //cost = new CardCost(0,0,1,1,1);
    //Card card = new Card(id,1,color,BASE1,ONE,cost);

    List<Card> newCard = new ArrayList<>();
    newCard.add(card);
    List<Card> newList = new ArrayList<>();

    newList.addAll(rmElement(splendorGame.getBoard().getCards()));
    newList.addAll(newCard);
    gameBoardReflector("cardField",splendorGame.getBoard(), newList);

    //Noble bNoble = new Noble(id,cost);
    //TradingPostSlot bTPS = new TradingPostSlot(id,true, Power.PURCHASE_CARD_TAKE_TOKEN,cost);
    //City bCity = new City(id,5,cost,2);
    //Move bMove = new Move(PURCHASE_DEV,card,player,BASE1,noble,bTPS,color,bCity);
    splendorGame.getBoard().applyMove(move,player);

    gameBoardReflector("actionPending",splendorGame.getBoard(),action);
    //System.out.println(splendorGame.getBoard().getPendingAction());
  }

  private static <T> List<T> rmElement(List<T> mylist) {
    mylist.remove(0);
    return mylist;
  }

  @Test
  void getAvailableActions() {

  }

}
//getPendingAction with reflection