package ca.mcgill.splendorserver.model.cards;

import ca.mcgill.splendorserver.model.action.Action;
import org.junit.jupiter.api.BeforeEach;
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

  private List<Action> actList = new ArrayList<>();
  private OrientCard oCard;

  @BeforeEach
    void setUp() {
    actList.add(PAIR_SPICE_CARD);
    CardCost cost = new CardCost(2,3,0,2,1);
    oCard = new OrientCard(5,3,DIAMOND,BASE2,TWO, cost,true,actList);

  }

  @Test
  void pairWithCard() {
    CardCost cost = new CardCost(3,4,5,2,1);
    Card newCard = new Card(2,3, EMERALD, BASE3, ONE, cost);
    oCard.pairWithCard(newCard);
    assertEquals(1, oCard.getTokenBonusAmount());
    assertEquals(EMERALD, oCard.getTokenBonusType());
  }

  @Test
  void isSpiceBag() {
    assertTrue(oCard.isSpiceBag());
  }

  @Test
  void getBonusActions() {

    assertEquals(actList, oCard.getBonusActions());
  }
}