package comp361.f2022hexanome15.splendorclient.model;

import javafx.scene.paint.Color;
import comp361.f2022hexanome15.splendorclient.model.cards.CardType;
import comp361.f2022hexanome15.splendorclient.model.tokens.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorManagerTest {

  @Test
  void getColorDiamond() {
    assertEquals(Color.LIGHTBLUE, ColorManager.getColor(TokenType.DIAMOND));
  }

  @Test
  void getColorSapphire() {
    assertEquals(Color.BLUE, ColorManager.getColor(TokenType.SAPPHIRE));
  }

  @Test
  void getColorEmerald() {
    assertEquals(Color.LIMEGREEN, ColorManager.getColor(TokenType.EMERALD));
  }

  @Test
  void getColorRuby() {
    assertEquals(Color.RED, ColorManager.getColor(TokenType.RUBY));
  }

  @Test
  void getColorOnyx() {
    assertEquals(Color.DIMGREY, ColorManager.getColor(TokenType.ONYX));
  }

  @Test
  void getColorGold() {
    assertEquals(Color.YELLOW, ColorManager.getColor(TokenType.GOLD));
  }

  @Test
  void getColorBase1() {
    assertEquals(Color.GREENYELLOW, ColorManager.getColor(CardType.BASE1));
  }

  @Test
  void getColorBase2() {
    assertEquals(Color.GOLD, ColorManager.getColor(CardType.BASE2));
  }

  @Test
  void getColorBase3() {
    assertEquals(Color.DEEPSKYBLUE, ColorManager.getColor(CardType.BASE3));
  }

  @Test
  void getColorOrient1() {
    assertEquals(Color.DARKRED, ColorManager.getColor(CardType.ORIENT1));
  }

  @Test
  void getColorOrient2() {
    assertEquals(Color.FIREBRICK, ColorManager.getColor(CardType.ORIENT2));
  }

  @Test
  void getColorOrient3() {
    assertEquals(Color.CRIMSON, ColorManager.getColor(CardType.ORIENT3));
  }

}