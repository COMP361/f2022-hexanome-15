package ca.mcgill.splendorserver.model;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.*;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.nobles.NobleStatus;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ca.mcgill.splendorserver.model.action.Action.RECEIVE_NOBLE;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE1;
import static ca.mcgill.splendorserver.model.cards.DeckType.ORIENT1;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ZERO;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.BLUE;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static ca.mcgill.splendorserver.model.tradingposts.Power.GAIN_5_PRESTIGE;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class GameBoardTest {
  UserInventory uinv;
  UserInventory uinv2;
  PlayerWrapper player;
  PlayerWrapper player2;
  CardCost cost1;
  CardCost cost2;
  Noble noble1;
  Card card1;
  OrientCard oCard1;
  OrientCard oCard2;
  OrientCard oCard3;
  OrientCard oCard4;
  TradingPostSlot tpS1;
  Move amove;
  Move move2;
  Move move3;
  TokenPile t1;
  TokenPile t2;
  TokenPile t3;
  TokenPile t4;
  TokenPile t6;

  Token tokD1;
  Token tokD2;
  Token tokD3;
  Token tokD4;

  Token tokO1;
  Token tokO2;
  Token tokO3;
  Token tokO4;

  Token tokS1;
  Token tokS2;
  Token tokS3;
  Token tokS4;

  Token tokE1;
  Token tokE2;
  Token tokE3;
  Token tokE4;

  List<UserInventory> Luinv;
  List<Deck> decks;
  List<Card> cards;
  List <TokenPile> tokenPiles;
  List<Noble> nobles;
  List<TradingPostSlot> LtPS;
  List<City> cities;

  @BeforeEach
  void setUp() {
    cost1 = new CardCost(1,0,0,0,0);
    cost2 = new CardCost(0,0,0,0,1);
    List<Action> actions = new ArrayList<>();
    actions.add(Action.TAKE_TOKEN);
    oCard1 = new OrientCard(1,3, ONYX, ORIENT1, ZERO, cost1,true, new ArrayList<>());
    oCard2 = new OrientCard(1,3, null, ORIENT1, ZERO, cost2,true, new ArrayList<>());
    oCard3 = new OrientCard(1,3, ONYX, ORIENT1, ZERO, cost2,false, actions);
    oCard4 = new OrientCard(1,3, null, ORIENT1, ZERO, cost2,true, actions);
    card1 = new Card(1,1, DIAMOND, BASE1, ZERO, cost1);
    card1.setCardStatus(CardStatus.NONE);
    noble1 = new Noble(0,new CardCost(1,0,0,0,0));
    player = PlayerWrapper.newPlayerWrapper("Slava");
    player2 = PlayerWrapper.newPlayerWrapper("Ojas");
    uinv = new UserInventory(player, Optional.ofNullable(RED));
    uinv2 = new UserInventory(player2, Optional.ofNullable(BLUE));
    tpS1 = new TradingPostSlot(1,true,GAIN_5_PRESTIGE,cost1);
    amove = new Move(Action.PURCHASE_DEV,card1,player,BASE1,noble1,tpS1,DIAMOND);
    move2 = new Move(Action.PAIR_SPICE_CARD,oCard1,player,BASE1,noble1,tpS1,DIAMOND);
    move3 = new Move(Action.PAIR_SPICE_CARD,oCard3,player,BASE1,noble1,tpS1,ONYX);
    tokD1 = new Token(DIAMOND);
    tokD2 = new Token(DIAMOND);
    tokD3 = new Token(DIAMOND);
    tokD4 = new Token(DIAMOND);

    tokO1 = new Token(ONYX);
    tokO2 = new Token(ONYX);
    tokO3 = new Token(ONYX);
    tokO4 = new Token(ONYX);

    tokS1 = new Token(SAPPHIRE);
    tokS2 = new Token(SAPPHIRE);
    tokS3 = new Token(SAPPHIRE);
    tokS4 = new Token(SAPPHIRE);

    tokE1 = new Token(EMERALD);


    t1 = new TokenPile(DIAMOND);
    t1.addToken(tokD1);
    t1.addToken(tokD2);
    t1.addToken(tokD3);
    t1.addToken(tokD4);
    t2 = new TokenPile(ONYX);
    t2.addToken(tokO1);
    t2.addToken(tokO2);
    t2.addToken(tokO3);
    t2.addToken(tokO4);

    t3 = new TokenPile(SAPPHIRE);
    t3.addToken(tokO1);
    t3.addToken(tokO2);
    t3.addToken(tokO3);
    t3.addToken(tokO4);

    t4 = new TokenPile(EMERALD);
    t4.addToken(tokE1);

    t6 = new TokenPile(GOLD);

    Luinv = new ArrayList<>();
    decks = new ArrayList<>();
    Deck deck1 = new Deck(BASE1);
    decks.add(deck1);

    cards = new ArrayList<>();
    cards.add(card1);

    tokenPiles = new ArrayList<>();
    tokenPiles.add(t1);
    tokenPiles.add(t2);
    tokenPiles.add(t3);
    tokenPiles.add(t4);
    tokenPiles.add(t6);

    uinv.addToken(tokD1);
    uinv.addToken(tokD2);
    uinv.addToken(tokD3);
    uinv.addToken(tokD4);

    uinv.addToken(tokO1);
    uinv.addToken(tokO2);
    uinv.addToken(tokO3);
    uinv.addToken(tokO3);

    uinv.addToken(tokS1);
    uinv.addToken(tokS2);
    uinv.addToken(tokS3);
    uinv.addToken(tokS4);

    Luinv.add(uinv);
    Luinv.add(uinv2);

    nobles = new ArrayList<>();
    LtPS = new ArrayList<>();
    cities = new ArrayList<>();
  }

  @Test
  void applyMove() {
    card1.setCardStatus(CardStatus.NONE);
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    uinv.addReservedNoble(noble1);
    Action myAction = RECEIVE_NOBLE;
    assertEquals(myAction, gb.applyMove(amove,player),"");

    //uinv.purchaseCard(card1);
    uinv.purchaseCard(oCard1);
    uinv.purchaseCard(oCard2);

    assertEquals(null, gb.applyMove(move2, player),"");

    uinv.purchaseCard(oCard3);
    uinv.purchaseCard(oCard4);
    Action action2 = (Action.TAKE_TOKEN);
    assertEquals(action2, gb.applyMove(move3, player),"");


  }

  @Test
  void getPendingAction() {
    card1.setCardStatus(CardStatus.NONE);
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    uinv.addReservedNoble(noble1);
    Action myAction = RECEIVE_NOBLE;
    assertEquals(null, gb.getPendingAction(),"");

  }

  @Test
  void getInventoryByPlayerName() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(Optional.empty(), gb.getInventoryByPlayerName("John"),"");

  }

  @Test
  void getInventories() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(uinv, gb.getInventories().get(0),"");

  }

  @Test
  void getDecks() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(decks, gb.getDecks(),"");
  }

  @Test
  void getCards() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(cards, gb.getCards(),"");
  }

  @Test
  void getTokenPilesNoGold() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(
      tokenPiles.get(0), gb.getTokenPilesNoGold().get(0),"");
  }

  @Test
  void getTokenPiles() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(tokenPiles.get(0), gb.getTokenPiles().get(DIAMOND),"");
  }

  @Test
  void noGoldTokens() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(true, gb.noGoldTokens(),"");
  }

  @Test
  void getNobles() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(nobles, gb.getNobles(),"");

  }

  @Test
  void testEquals() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    GameBoard gb2 = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    tokenPiles.remove(t1);
    GameBoard gb3 = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertTrue(gb.equals(gb2),"");
    assertTrue(gb.equals(gb),"");
    assertFalse(gb.equals(gb3),"");
    assertFalse(gb.equals(1),"");
  }
  @Test
  void testGetInventoryByPlayerName() {

  }

  @Test
  void getTradingPostSlots() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(LtPS, gb.getTradingPostSlots(),"");
  }

  @Test
  void getCities() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(cities, gb.getCities(),"");
  }

  @Test
  void testGetTokenPilesNoGold() {
  }

  @Test
  void getMoveCache() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    List<Move> moves = new ArrayList<>();
    assertEquals(moves, gb.getMoveCache(),"");
  }

  @Test
  void getEndOfTurnActions() {
    card1.setCardStatus(CardStatus.NONE);
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    uinv.addReservedNoble(noble1);
    Action endAction = gb.applyMove(amove,player);
    assertEquals(Action.RET_TOKEN,gb.getEndOfTurnActions(amove,uinv),"");

    /*
    noble1.setStatus(NobleStatus.ON_BOARD);
    Luinv.remove(uinv);
    nobles.add(noble1);
    uinv.addReservedNoble(noble1);
    Luinv.add(uinv);
    GameBoard gb1 = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(RECEIVE_NOBLE,gb1.getEndOfTurnActions(amove,uinv),"")
    ;*/
  }

  @Test
  void endTurn() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    gb.endTurn();
    assertEquals(null,gb.getPendingAction(),"");
  }
}