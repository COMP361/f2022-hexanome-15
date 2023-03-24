package ca.mcgill.splendorserver.model;
import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.action.Action;
import ca.mcgill.splendorserver.model.action.Move;
import ca.mcgill.splendorserver.model.cards.*;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static ca.mcgill.splendorserver.model.action.Action.RECEIVE_NOBLE;
import static ca.mcgill.splendorserver.model.action.Action.RET_TOKEN;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE1;
import static ca.mcgill.splendorserver.model.cards.DeckType.ORIENT1;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ZERO;
import static ca.mcgill.splendorserver.model.tokens.TokenType.*;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.BLUE;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static ca.mcgill.splendorserver.model.tradingposts.Power.GAIN_5_PRESTIGE;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockRestServiceServer
class GameBoardTest {
  private UserInventory uinv;
  private UserInventory uinv2;
  private PlayerWrapper player;
  private PlayerWrapper player2;
  private CardCost cost1;
  private CardCost cost2;
  private Noble noble1;
  private Card card1;
  private OrientCard oCard1;
  private OrientCard oCard2;
  private OrientCard oCard3;
  private OrientCard oCard4;
  private TradingPostSlot tpS1;
  private Move amove;
  private Move move2;
  private Move move3;
  private TokenPile t1;
  private TokenPile t2;
  private TokenPile t3;
  private TokenPile t4;
  private TokenPile t6;

  private Token tokD1;
  private Token tokD2;
  private Token tokD3;
  private Token tokD4;

  private Token tokO1;
  private Token tokO2;
  private Token tokO3;
  private Token tokO4;

  private Token tokS1;
  private Token tokS2;
  private Token tokS3;
  private Token tokS4;

  private Token tokE1;

  private List<UserInventory> Luinv;
  private List<Deck> decks;
  private List<Card> cards;
  private List <TokenPile> tokenPiles;
  private List<Noble> nobles;
  private List<TradingPostSlot> LtPS;
  private List<City> cities;

  @BeforeEach
  void setUp() {
    nobles = new ArrayList<>();
    nobles.add(new Noble(5, new CardCost(2,2,2,2,2)));
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
    amove = new Move(Action.PURCHASE_DEV,card1,player,BASE1,noble1,tpS1,DIAMOND, null);
    move2 = new Move(Action.PAIR_SPICE_CARD,oCard1,player,BASE1,noble1,tpS1,DIAMOND, null);
    move3 = new Move(Action.PAIR_SPICE_CARD,oCard3,player,BASE1,noble1,tpS1,ONYX, null);
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

    LtPS = new ArrayList<>();
    cities = new ArrayList<>();
  }

  @Test
  void applyMove() {
    card1.setCardStatus(CardStatus.NONE);
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    uinv.addReservedNoble(noble1);
    assertEquals(null, gb.applyMove(amove,player));

    uinv.purchaseCard(oCard1);
    uinv.purchaseCard(oCard2);

    assertEquals(null, gb.applyMove(move2, player));

    uinv.purchaseCard(oCard3);
    uinv.purchaseCard(oCard4);
    Action action2 = (Action.TAKE_TOKEN);
    assertEquals(action2, gb.applyMove(move3, player));


  }

  @Test
  void getPendingAction() {
    card1.setCardStatus(CardStatus.NONE);
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    uinv.addReservedNoble(noble1);
    Action myAction = RECEIVE_NOBLE;
    assertEquals(null, gb.getPendingAction());

  }

  @Test
  void getInventoryByPlayerName() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(Optional.empty(), gb.getInventoryByPlayerName("John"));

  }

  @Test
  void getInventories() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(uinv, gb.getInventories().get(0));

  }

  @Test
  void getDecks() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(decks, gb.getDecks());
  }

  @Test
  void getCards() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(cards, gb.getCards());
  }

  @Test
  void getTokenPilesNoGold() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(
      tokenPiles.get(0), gb.getTokenPilesNoGold().get(0));
  }

  @Test
  void getTokenPiles() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(tokenPiles.get(0), gb.getTokenPiles().get(DIAMOND));
  }

  @Test
  void noGoldTokens() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(true, gb.noGoldTokens());
  }

  @Test
  void getNobles() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(nobles, gb.getNobles());

  }

  @Test
  void testEquals() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    GameBoard gb2 = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    tokenPiles.remove(t1);
    GameBoard gb3 = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertTrue(gb.equals(gb2));
    assertFalse(gb.equals(gb3));
    assertFalse(gb.equals(1));
  }
  @Test
  void testGetInventoryByPlayerName() {

  }

  @Test
  void getTradingPostSlots() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(LtPS, gb.getTradingPostSlots());
  }

  @Test
  void getCities() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    assertEquals(cities, gb.getCities());
  }

  @Test
  void testGetTokenPilesNoGold() {
  }

  @Test
  void getMoveCache() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    List<Move> moves = new ArrayList<>();
    assertEquals(moves, gb.getMoveCache());
  }

  @Test
  void getEndOfTurnActions() {
    card1.setCardStatus(CardStatus.NONE);
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    uinv.addReservedNoble(noble1);
    Action endAction = gb.applyMove(amove,player);
    assertEquals(RET_TOKEN,gb.getEndOfTurnActions(amove,uinv));
  }

  @Test
  void endTurn() {
    GameBoard gb = new GameBoard(Luinv,decks,cards,tokenPiles,nobles,LtPS,cities);
    gb.endTurn();
    assertEquals(null,gb.getPendingAction());
  }
}