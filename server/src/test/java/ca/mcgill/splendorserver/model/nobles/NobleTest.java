package ca.mcgill.splendorserver.model.nobles;

import ca.mcgill.splendorserver.model.cards.CardCost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static org.junit.jupiter.api.Assertions.*;

class NobleTest {

  private CardCost cardc;
  private Noble anoble;

  @BeforeEach
  void setUp() {
    cardc = new CardCost(4, 4, 0, 0, 0);
    anoble = new Noble(0, cardc);
  }

  @Test
  void getPrestige() {

    assertEquals(3,anoble.getPrestige());
  }

  @Test
  void getVisitRequirements() {

    assertEquals(cardc, anoble.getVisitRequirements());
  }

  @Test
  void getStatus() {

    assertEquals(NobleStatus.ON_BOARD,anoble.getStatus());
  }

  @Test
  void getId() {
    assertEquals(0, anoble.getId());
  }

  @Test
  void setStatus() {
    anoble.setStatus(NobleStatus.VISITING);
    assertEquals(NobleStatus.VISITING, anoble.getStatus());
  }

  @Test
  void getNoblesThreePlayers() {
    assertEquals(4, Noble.getNobles(3).size());
  }

}