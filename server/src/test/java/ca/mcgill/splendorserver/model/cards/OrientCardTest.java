package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.action.Action;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.splendorserver.model.action.Action.*;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE2;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE3;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.TWO;
import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static ca.mcgill.splendorserver.model.tokens.TokenType.EMERALD;
import static org.junit.jupiter.api.Assertions.*;

class OrientCardTest {
  CardCost acost = new CardCost(3,4,5,2,1);
  Card acard = new Card(2,3,EMERALD,BASE3,ONE,acost);


  List<Action> actList = new ArrayList<>();

  OrientCard Ocard = new OrientCard(5,3,DIAMOND,BASE2,TWO,acost,true,actList);

  @Test
  void pairWithCard() {
    actList.add(PURCHASE_DEV);
    actList.add(RECEIVE_NOBLE);
    actList.add(RESERVE_DEV);
    Ocard.pairWithCard(acard);
    assertEquals(1,acard.getTokenBonusAmount());
    assertEquals(EMERALD, acard.getTokenBonusType());
  }

  @Test
  void isSpiceBag() {
    assertTrue(Ocard.isSpiceBag());
  }

  @Test
  void getBonusActions() {
    assertEquals(actList, Ocard.getBonusActions());
  }
}