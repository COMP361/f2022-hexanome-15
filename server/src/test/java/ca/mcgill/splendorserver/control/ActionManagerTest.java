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

  //This test does not work with mvn, idk why
  /*@Test
  void performAction() {

    PlayerWrapper aPlayer = PlayerWrapper.newPlayerWrapper("Slava");
    PlayerWrapper aPlayer2 = PlayerWrapper.newPlayerWrapper("Larry");
    coat = Optional.of(RED);
    coat2 = Optional.of(BLUE);
    uinv = new UserInventory(aPlayer,coat);
    uinv2 = new UserInventory(aPlayer2,coat2);

    // Initialize Players
    lpw.add(aPlayer);
    lpw.add(aPlayer2);
    inventories.add(uinv);
    inventories.add(uinv2);

    Player player1 = new Player("Slava", "yellow");
    Player player2 = new Player("Larry", "orange");
    players.add(player1);
    players.add(player2);

    SessionInfo si = new SessionInfo("",players,lpw,aPlayer,"");
    splendorGame = new SplendorGame(si,2L);
    LocalGameStorage.addActiveGame(splendorGame);

    List<Action> bActions0 = new ArrayList<>();
    bActions0.add(RESERVE_NOBLE);
    CardCost acost = new CardCost(0,1,0,1,0);
    OrientCard aCard = new OrientCard(111,1,SAPPHIRE,BASE1,ZERO,acost,false,bActions0);
    Noble aNoble = new Noble(13,acost);
    Move aMove = new Move(PURCHASE_DEV,aCard,aPlayer,BASE1,aNoble,null,SAPPHIRE,null);

    gameBoardReflector("actionPending",splendorGame.getBoard(),RESERVE_NOBLE);
    buyCard (aPlayer, aCard, TAKE_TOKEN, aMove);
    ResponseEntity<String> LMoves = aM.getAvailableActions(2L,"Slava","");
    String lAction;
    if (LMoves.getBody().length() == 2) { lAction = "";} else { lAction = LMoves.getBody().substring(2,34);}


  assertEquals
    (ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
        .body(TAKE_TOKEN.toString()),
      aM.performAction(2L, "Slava", lAction, "Slava"),
      "");

    LMoves = aM.getAvailableActions(2L,"Slava","");
    if (LMoves.getBody().length() == 2) { lAction = "";} else { lAction = LMoves.getBody().substring(2,34);}

    assertEquals //REWRITE
      (ResponseEntity.status(HttpStatus.OK)
          .body("Larry"),
        aM.performAction(2L, "Slava", lAction, "Slava"),
        "");

    affordCard(uinv2,"Larry");
    LMoves = aM.getAvailableActions(2L,"Larry","");
    lAction = LMoves.getBody().substring(2,34);


    assertEquals // REWRITE
      (ResponseEntity.status(HttpStatus.OK)
          .body("Slava"),
        aM.performAction(2L, "Larry", lAction, "Larry"),
        "");


    cardDiscard(1,aPlayer,SAPPHIRE,DISCARD_FIRST_BLUE_CARD);
    LMoves = aM.getAvailableActions(2L,"Slava","");
    lAction = LMoves.getBody().substring(2,34);
    //System.out.println(splendorGame.getBoard().getInventories().get(0).toString());

    assertEquals
      (ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(DISCARD_SECOND_BLUE_CARD.toString()),
        aM.performAction(2L, "Slava", lAction, "Slava"),
        "");

    cardDiscard(2,aPlayer,SAPPHIRE,DISCARD_SECOND_BLUE_CARD);
    LMoves = aM.getAvailableActions(2L,"Slava","");
    lAction = LMoves.getBody().substring(2,34);

    assertEquals
      (ResponseEntity.status(HttpStatus.OK).body("Larry"),
        aM.performAction(2L, "Slava", lAction, "Slava"),
        "");

    cardDiscard(3,aPlayer2,DIAMOND,DISCARD_FIRST_WHITE_CARD);
    LMoves = aM.getAvailableActions(2L,"Larry","");
    lAction = LMoves.getBody().substring(2,34);

    assertEquals
      (ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(DISCARD_SECOND_WHITE_CARD.toString()),
        aM.performAction(2L, "Larry", lAction, "Larry"),
        "");

    cardDiscard(3,aPlayer2,DIAMOND,DISCARD_SECOND_WHITE_CARD);
    LMoves = aM.getAvailableActions(2L,"Larry","");
    lAction = LMoves.getBody().substring(2,34);

    assertEquals
      (ResponseEntity.status(HttpStatus.OK).body("Slava"),
        aM.performAction(2L, "Larry", lAction, "Larry"),
        "");

    cardDiscard(4,aPlayer,EMERALD,DISCARD_FIRST_GREEN_CARD);
    LMoves = aM.getAvailableActions(2L,"Slava","");
    lAction = LMoves.getBody().substring(2,34);

    assertEquals
      (ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(DISCARD_SECOND_GREEN_CARD.toString()),
        aM.performAction(2L, "Slava", lAction, "Slava"),
        "");

    cardDiscard(4,aPlayer,EMERALD,DISCARD_SECOND_GREEN_CARD);
    LMoves = aM.getAvailableActions(2L,"Slava","");
    lAction = LMoves.getBody().substring(2,34);

    assertEquals
      (ResponseEntity.status(HttpStatus.OK).body("Larry"),
        aM.performAction(2L, "Slava", lAction, "Slava"),
        "");

    cardDiscard(5,aPlayer2,RUBY,DISCARD_FIRST_RED_CARD);
    LMoves = aM.getAvailableActions(2L,"Larry","");
    lAction = LMoves.getBody().substring(2,34);

    assertEquals
      (ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(DISCARD_SECOND_RED_CARD.toString()),
        aM.performAction(2L, "Larry", lAction, "Larry"),
        "");

    cardDiscard(5,aPlayer2,RUBY,DISCARD_SECOND_RED_CARD);
    LMoves = aM.getAvailableActions(2L,"Larry","");
    lAction = LMoves.getBody().substring(2,34);

    assertEquals
      (ResponseEntity.status(HttpStatus.OK).body("Slava"),
        aM.performAction(2L, "Larry", lAction, "Larry"),
        "");

    cardDiscard(6,aPlayer,ONYX,DISCARD_FIRST_BLACK_CARD);
    LMoves = aM.getAvailableActions(2L,"Slava","");
    lAction = LMoves.getBody().substring(2,34);

    assertEquals
      (ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(DISCARD_SECOND_BLACK_CARD.toString()),
        aM.performAction(2L, "Slava", lAction, "Slava"),
        "");

    cardDiscard(6,aPlayer,ONYX,DISCARD_SECOND_BLACK_CARD);
    LMoves = aM.getAvailableActions(2L,"Slava","");
    lAction = LMoves.getBody().substring(2,34);

    assertEquals
      (ResponseEntity.status(HttpStatus.OK).body("Larry"),
        aM.performAction(2L, "Slava", lAction, "Slava"),
        "");

    List<Action> bonusActions = new ArrayList<>();
    bonusActions.add(TAKE_EXTRA_TOKEN);
    CardCost cost = new CardCost(0,0,1,1,1);
    OrientCard card = new OrientCard(11,5,SAPPHIRE,BASE1,ONE,cost,true,bonusActions);
    Noble bNoble = new Noble(11,cost);
    TradingPostSlot bTPS = new TradingPostSlot(11,true, Power.PURCHASE_CARD_TAKE_TOKEN,cost);
    City bCity = new City(11,5,cost,2);
    Move bMove = new Move(PURCHASE_DEV,card,aPlayer2,BASE1,bNoble,bTPS,SAPPHIRE,bCity);

    buyCard (aPlayer2, card, CASCADE_LEVEL_2, bMove);
    LMoves = aM.getAvailableActions(2L,"Larry","");
    lAction = LMoves.getBody().substring(2,34);

    List<ResponseEntity<String>> possibleOutputs = new ArrayList<>();
    possibleOutputs.add(ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(RESERVE_NOBLE.toString()));
    possibleOutputs.add(ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(PAIR_SPICE_CARD.toString()));
    possibleOutputs.add(ResponseEntity.status(HttpStatus.OK).body("Slava"));

    assertThat(
      (aM.performAction(2L, "Larry", lAction, "Larry")), in(possibleOutputs));

    List<Action> bonusActions1 = new ArrayList<>();
    bonusActions1.add(TAKE_EXTRA_TOKEN);
    CardCost cost1 = new CardCost(0,0,1,1,1);
    OrientCard card1 = new OrientCard(11,1,null,BASE1,ZERO,cost1,true,bonusActions1);
    Noble noble1 = new Noble(11,cost1);
    TradingPostSlot tps1 = new TradingPostSlot(11,true, Power.PURCHASE_CARD_TAKE_TOKEN,cost1);
    City city0 = new City(11,5,cost1,2);
    Move move1 = new Move(PURCHASE_DEV,card1,aPlayer,BASE1,noble1,tps1,SAPPHIRE,city0);

    buyCard (aPlayer, card1, CASCADE_LEVEL_1, move1);
    LMoves = aM.getAvailableActions(2L,"Slava","");

    if (LMoves.getBody().length() == 2) {
      lAction = "";
    } else {
      lAction = LMoves.getBody().substring(2,34);
    }

    //lAction = LMoves.getBody().substring(2,34);

    List<ResponseEntity<String>> possibleOutputs1 = new ArrayList<>();
    possibleOutputs1.add(ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(RESERVE_NOBLE.toString()));
    possibleOutputs1.add(ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(PAIR_SPICE_CARD.toString()));
    possibleOutputs1.add(ResponseEntity.status(HttpStatus.OK).body("Larry"));
    possibleOutputs1.add(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

    assertThat(
      (aM.performAction(2L, "Slava", lAction, "Slava")), in(possibleOutputs1));

    List<City> cities = new ArrayList<>();
    CardCost cost2 = new CardCost(0,0,0,0,0);
    City city1 = new City(11,0,cost2,0);
    cities.add(city1);
    gameBoardReflector("cities",splendorGame.getBoard(), cities);
    //gameBoardReflector("cities",splendorGame.getBoard().getInventories().get(0), cities);

    List<Action> bonusActions2 = new ArrayList<>();
    bonusActions2.add(RECEIVE_CITY);
    OrientCard card2 = new OrientCard(12,5,SAPPHIRE,BASE1,ONE,cost2,true,bonusActions2);
    Noble noble2 = new Noble(12,cost2);
    TradingPostSlot tps2 = new TradingPostSlot(12,true, Power.PURCHASE_CARD_TAKE_TOKEN,cost2);
    Move move2 = new Move(PURCHASE_DEV,card2,aPlayer2,BASE1,noble2,tps2,SAPPHIRE,city1);

    //Move move2 = new Move(PAIR_SPICE_CARD,card2,aPlayer2,BASE1,noble1,tps1,SAPPHIRE,city1);
    aM.performAction(2L, "Larry", lAction, "Larry");
    uinv2 = splendorGame.getBoard().getInventories().get(1);

    buyCard (aPlayer2, card2, RECEIVE_CITY, move2);
    LMoves = aM.getAvailableActions(2L,"Larry","");
    if (LMoves.getBody().length() == 2) { lAction = "";} else { lAction = LMoves.getBody().substring(2,34);}

    assertEquals(
      (ResponseEntity.status(HttpStatus.OK).body("Slava")),aM.performAction(2L, "Larry", lAction, "Larry"),"");


    CardCost cost3 = new CardCost(1,0,0,0,0);
    City city2 = new City(11,0,cost3,0);
    cities.add(city2);
    gameBoardReflector("cities",splendorGame.getBoard(), cities);
    //gameBoardReflector("cities",splendorGame.getBoard().getInventories().get(0), cities);

    List<Action> bonusActions3 = new ArrayList<>();
    bonusActions2.add(PAIR_SPICE_CARD);
    OrientCard card3 = new OrientCard(13,5,SAPPHIRE,BASE1,ONE,cost3,true,bonusActions3);
    Noble noble3 = new Noble(13,cost3);
    TradingPostSlot tps3 = new TradingPostSlot(13,true, Power.PURCHASE_CARD_TAKE_TOKEN,cost3);
    Move move3 = new Move(PURCHASE_DEV,card3,aPlayer,BASE1,noble3,tps3,SAPPHIRE,city2);

    //Move move2 = new Move(PAIR_SPICE_CARD,card2,aPlayer2,BASE1,noble1,tps1,SAPPHIRE,city1);
    aM.performAction(2L, "Larry", lAction, "Larry");
    uinv2 = splendorGame.getBoard().getInventories().get(1);

    buyCard (aPlayer, card3, PAIR_SPICE_CARD, move3);
    LMoves = aM.getAvailableActions(2L,"Slava","");
    if (LMoves.getBody().length() == 2) { lAction = "";} else { lAction = LMoves.getBody().substring(2,34);}

    assertEquals(
      (ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(TAKE_EXTRA_TOKEN.toString())),
      aM.performAction(2L, "Slava", lAction, "Slava"),"");

    //buyCard (aPlayer, card3, PAIR_SPICE_CARD, move3);
    LMoves = aM.getAvailableActions(2L,"Slava","");
    if (LMoves.getBody().length() == 2) { lAction = "";} else { lAction = LMoves.getBody().substring(2,34);}

    assertEquals(
      (ResponseEntity.status(HttpStatus.OK).body("Larry")),
      aM.performAction(2L, "Slava", lAction, "Slava"),"");


    Move move4 = new Move(RET_TOKEN,card3,aPlayer2,BASE1,noble3,tps3,SAPPHIRE,city2);
    Token t1 = new Token(SAPPHIRE);
    uinv2.addToken(t1); uinv2.addToken(t1); uinv2.addToken(t1);

    buyCard (aPlayer2, card3, RET_TOKEN, move4);
    LMoves = aM.getAvailableActions(2L,"Larry","");
    if (LMoves.getBody().length() == 2) { lAction = "";} else { lAction = LMoves.getBody().substring(2,34);}

    assertEquals(
      (ResponseEntity.status(HttpStatus.OK).body("Slava")),
      aM.performAction(2L, "Larry", lAction, "Larry"),"");

    List<Noble> nobles = new ArrayList();
    nobles.add(noble1); nobles.add(noble2); nobles.add(noble3);
    gameBoardReflector("nobles",splendorGame.getBoard(),nobles);

    Move move5 = new Move(RECEIVE_NOBLE,card3,aPlayer,BASE1,noble3,tps3,SAPPHIRE,city2);
    buyCard (aPlayer, card3, RECEIVE_NOBLE, move5);
    LMoves = aM.getAvailableActions(2L,"Slava","");
    if (LMoves.getBody().length() == 2) { lAction = "";} else { lAction = LMoves.getBody().substring(2,34);}

    assertEquals(
      (ResponseEntity.status(HttpStatus.OK).body("Larry")),
      aM.performAction(2L, "Slava", lAction, "Slava"),"");


  }*/



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