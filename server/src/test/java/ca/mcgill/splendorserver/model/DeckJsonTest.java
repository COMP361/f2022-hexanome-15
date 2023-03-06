package ca.mcgill.splendorserver.model;

import org.junit.jupiter.api.Test;

import static ca.mcgill.splendorserver.model.cards.DeckType.BASE2;
import static org.junit.jupiter.api.Assertions.*;

class DeckJsonTest {

  @Test
  void DeckJsonTest() {
    DeckJson dJson= new DeckJson(2,BASE2);
    assertTrue(dJson != null);

  }
}