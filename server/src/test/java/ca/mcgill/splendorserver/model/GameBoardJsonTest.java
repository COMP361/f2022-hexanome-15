package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static ca.mcgill.splendorserver.model.cards.DeckType.BASE1;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ZERO;
import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardJsonTest {
  private String whoseTurn = "Sofia";
  private List<InventoryJson> inventories = new ArrayList<>();
  private List<Deck> decks = new ArrayList<>();
  private List<Card> cards = new ArrayList<>();
  private EnumMap<TokenType, TokenPile> tokenPiles = new EnumMap<TokenType, TokenPile>(TokenType.class);
  private GameBoardJson gjson;



  @BeforeEach
  void setUp() {
    TokenPile white = new TokenPile(TokenType.DIAMOND);
    tokenPiles.put(TokenType.DIAMOND, white);
    TokenPile blue = new TokenPile(TokenType.SAPPHIRE);
    tokenPiles.put(TokenType.SAPPHIRE, blue);
    TokenPile green = new TokenPile(TokenType.EMERALD);
    tokenPiles.put(TokenType.EMERALD, green);
    TokenPile red = new TokenPile(TokenType.RUBY);
    tokenPiles.put(TokenType.RUBY, red);
    TokenPile black = new TokenPile(TokenType.ONYX);
    tokenPiles.put(TokenType.ONYX, black);
    TokenPile gold = new TokenPile(TokenType.GOLD);
    tokenPiles.put(TokenType.GOLD, gold);
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card1 = new Card(1,2, DIAMOND, BASE1, ZERO, cost);
    cards.add(card1);
    Deck deck1 = new Deck(BASE1);
    decks.add(deck1);
    List<Noble> nobles = Noble.getNobles(2);
    List<TradingPostSlot> tradingPostSlots = TradingPostSlot.getTradingPostSlots();
    gjson = new GameBoardJson(whoseTurn, inventories, decks,
      nobles, cards, tokenPiles, tradingPostSlots);
  }

  @Test
  void getWhoseTurn() {
    assertEquals("Sofia", gjson.getWhoseTurn());
  }

}