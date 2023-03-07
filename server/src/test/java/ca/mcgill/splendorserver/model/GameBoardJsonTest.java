package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cards.Deck;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static ca.mcgill.splendorserver.model.cards.DeckType.BASE1;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ZERO;
import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardJsonTest {
  String whoseTurn = new String();
  List<InventoryJson> inventories = new ArrayList<>();
  List<Deck> decks = new ArrayList<>();
  List<Noble> nobles = new ArrayList<>();
  List<Card> cardField = new ArrayList<>();
  EnumMap<TokenType, TokenPile> tokenField = new EnumMap<>(TokenType.class);
  List<TradingPostSlot> tradingPostSlots = new ArrayList<>();
  GameBoardJson gjson;



  @Test
  void GameBoardJson() {
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card1 = new Card(1,2,DIAMOND,BASE1,ZERO,cost);
    cardField.add(card1);
    gjson = new GameBoardJson(whoseTurn,inventories,decks,nobles,cardField,tokenField,tradingPostSlots);
    assertEquals(cardField.get(0),card1,"");

  }

  @Test
  void getWhoseTurn() {


  }

}