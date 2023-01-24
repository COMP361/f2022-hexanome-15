package ca.mcgill.splendorserver.model;


import ca.mcgill.splendorserver.model.cards.CardType;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import javafx.scene.paint.Color;

/**
 * Manages colors for tokens and cards.
 */
public class ColorManager {

  /**
   * Creates a ColorManager.
   */
  public ColorManager() {

  }

  /**
   * Returns the color for a given token type.
   *
   * @param type The given token type
   * @return the color of a token type
   */
  public static Color getColor(TokenType type) {
    switch (type) {
      // yes ik switch statements used liberally are an antipattern,
      // but I have to bind colors somewhere
      // might refactor after
      case DIAMOND:
        return Color.LIGHTBLUE;
      case SAPPHIRE:
        return Color.BLUE;
      case EMERALD:
        return Color.LIMEGREEN;
      case RUBY:
        return Color.RED;
      case ONYX:
        return Color.DIMGREY;
      case GOLD:
        return Color.YELLOW;
      default:
        return Color.WHITE;
    }
  }

  /**
   * Returns the color for a given card type.
   *
   * @param type the given card type
   * @return the color of a card type
   */
  public static Color getColor(CardType type) {
    switch (type) {
      // yes ik switch statements used liberally are an antipattern,
      // but I have to bind colors somewhere
      // might refactor after
      case BASE1:
        return Color.GREENYELLOW;
      case BASE2:
        return Color.GOLD;
      case BASE3:
        return Color.DEEPSKYBLUE;
      case ORIENT1:
        return Color.DARKRED;
      case ORIENT2:
        return Color.FIREBRICK;
      case ORIENT3:
        return Color.CRIMSON;
      default:
        return Color.WHITE;
    }
  }
}