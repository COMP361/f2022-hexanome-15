package ca.mcgill.splendorserver.model.action;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static ca.mcgill.splendorserver.model.action.Action.PURCHASE_DEV;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE2;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static ca.mcgill.splendorserver.model.tokens.TokenType.EMERALD;
import static ca.mcgill.splendorserver.model.tradingposts.Power.PURCHASE_CARD_TAKE_TOKEN;
import static org.junit.jupiter.api.Assertions.*;

class MoveTest
{
  CardCost cardc = new CardCost(1,2,3,4,5);
  Noble anoble = new Noble(cardc);
  TradingPostSlot aTPS = new TradingPostSlot(false,PURCHASE_CARD_TAKE_TOKEN,cardc);
  TokenType tkps = DIAMOND;
  TokenType rtkps = EMERALD;
  PlayerWrapper aPlayer = PlayerWrapper.newPlayerWrapper("Slava");
  Card acard = new Card(1,2,DIAMOND,BASE2,ONE,cardc);
  Action mAction = PURCHASE_DEV;
  Move amove = new Move(PURCHASE_DEV,acard,aPlayer,BASE2,rtkps,anoble,aTPS,tkps);

  @Test
  void getCard() {
    assertEquals(acard,amove.getCard());
  }

  @Test
  void getName() {
    assertEquals(aPlayer.getName(),amove.getPlayerName());
  }

  @Test
  void getDeckType() {
    assertEquals(BASE2,amove.getDeckType());
  }

  @Test
  void getSelectedTokenTypes() {
    assertEquals(tkps,amove.getSelectedTokenTypes());
  }

  @Test
  void getReturnedTokenTypes() {
    assertEquals(rtkps,amove.getReturnedTokenTypes());
  }

  @Test
  void getNoble() {
    assertEquals(anoble,amove.getNoble());
  }

  @Test
  void getTradingPostSlot() {
    assertEquals(aTPS,amove.getTradingPostSlot());
  }

  @Test
  void getAction() {
    assertEquals(mAction,amove.getAction());
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