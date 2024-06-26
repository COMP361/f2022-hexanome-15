package ca.mcgill.splendorserver.model.action;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import org.junit.jupiter.api.Test;
import static ca.mcgill.splendorserver.model.action.Action.PURCHASE_DEV;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE2;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static ca.mcgill.splendorserver.model.tradingposts.Power.PURCHASE_CARD_TAKE_TOKEN;
import static org.junit.jupiter.api.Assertions.*;

class MoveTest
{
  private CardCost cardc = new CardCost(1, 0, 0, 3, 0);
  private Noble anoble = new Noble(0, cardc);
  private TradingPostSlot aTPS = new TradingPostSlot(0,false, PURCHASE_CARD_TAKE_TOKEN, cardc);
  private TokenType tkps = DIAMOND;
  private PlayerWrapper aPlayer = PlayerWrapper.newPlayerWrapper("Slava");
  private Card acard = new Card(1,2,DIAMOND,BASE2,ONE,cardc);
  private Action mAction = PURCHASE_DEV;
  private City city = new City(0, 3, cardc, 0);
  private Move amove = new Move(PURCHASE_DEV, acard, aPlayer, BASE2, anoble, aTPS, tkps, city);

  @Test
  void getCard() {
    assertEquals(acard, amove.getCard());
  }

  @Test
  void getName() {
    assertEquals(aPlayer.getName(),amove.getPlayerName());
  }

  @Test
  void getDeckType() {
    assertEquals(BASE2, amove.getDeckType());
  }

  @Test
  void getSelectedTokenTypes() {
    assertEquals(tkps, amove.getSelectedTokenTypes());
  }

  @Test
  void getNoble() {
    assertEquals(anoble, amove.getNoble());
  }

  @Test
  void getTradingPostSlot() {
    assertEquals(aTPS, amove.getTradingPostSlot());
  }

  @Test
  void getAction() {
    assertEquals(mAction, amove.getAction());
  }

  @Test
  void getCity() {
    assertEquals(city, amove.getCity());
  }

  @Test
  void testToString() {
    assertEquals(
      "Move{"
        + "player=" + aPlayer
        + ", action=" + mAction
        + ", card=" + acard
        + ", tokenTypes=" + tkps
        + ", deckLevel=" + BASE2
        + ", tradingPostSlot=" + aTPS
        + '}',
      amove.toString());
  }
}