package ca.mcgill.splendorserver.model.nobles;

import ca.mcgill.splendorserver.model.cards.CardCost;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static org.junit.jupiter.api.Assertions.*;

class NobleTest {
  CardCost cardc = new CardCost(1,2,3,4,5);
  Noble anoble = new Noble(cardc);
  List<Noble> nobles = new ArrayList<>();

  @Test
  void getPrestige() {
    assertEquals(3,anoble.getPrestige(),"");
  }

  @Test
  void getVisitRequirements() {
    assertEquals(cardc,anoble.getVisitRequirements(),"");
  }

  @Test
  void getStatus() {
    assertEquals(NobleStatus.ON_BOARD,anoble.getStatus(),"");
  }

  @Test
  void setStatus() {
    anoble.setStatus(NobleStatus.VISITING);
    assertEquals(NobleStatus.VISITING,anoble.getStatus(),"");
  }

  @Test
  void getNobles() {
    nobles.add(new Noble(new CardCost(3, 3, 3, 0, 0)));
    nobles.add(new Noble(new CardCost(0, 3, 3, 3, 0)));
    nobles.add(new Noble(new CardCost(3, 0, 0, 3, 3)));
    nobles.add(new Noble(new CardCost(3, 3, 0, 0, 3)));
    nobles.add(new Noble(new CardCost(0, 0, 3, 3, 3)));
    nobles.add(new Noble(new CardCost(0, 0, 0, 4, 4)));
    nobles.add(new Noble(new CardCost(4, 0, 0, 0, 4)));
    nobles.add(new Noble(new CardCost(0, 0, 4, 4, 0)));
    nobles.add(new Noble(new CardCost(0, 4, 4, 0, 0)));
    nobles.add(new Noble(new CardCost(4, 4, 0, 0, 0)));
    assertEquals(3,nobles.get(0).getVisitRequirements().costByTokenType(DIAMOND),"");
  }
}