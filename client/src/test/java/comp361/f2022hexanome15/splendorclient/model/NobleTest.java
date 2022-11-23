package comp361.f2022hexanome15.splendorclient.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NobleTest {

  @Test
  void getCardRequirements() {
    int[] requirements = new int[] {3,3,3,0,0};
    Noble noble = new Noble(requirements);
    assertEquals(requirements, noble.getCardRequirements());
  }

  @Test
  void isReserved() {
    int[] requirements = new int[] {3,3,3,0,0};
    Noble noble = new Noble(requirements);
    assertFalse(noble.isReserved());
  }
}