package ca.mcgill.splendorserver.model.nobles;

import ca.mcgill.splendorserver.model.cards.CardCost;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO: Make Nobles flyweights

/**
 * Represents a Splendor Noble with reserved status and prestige.
 */
public class Noble {
  private final CardCost visitRequirements;
  private NobleStatus status;
  private final int         prestige;
  private static final List<Noble> nobles  = new ArrayList<Noble>();
  private static final List<Noble> noblesInGame = new ArrayList<>();

  /**
   * Creates a noble.
   *
   * @param visitRequirements the token type bonus requirements for visiting this noble
   */
  public Noble(CardCost visitRequirements) {
    assert visitRequirements != null;
    this.visitRequirements = visitRequirements;
    this.status            = NobleStatus.ON_BOARD;
    this.prestige          = 3;
  }

  /**
   * Returns this noble's prestige.
   *
   * @return this noble's prestige
   */
  public int getPrestige() {
    return prestige;
  }

  /**
   * Returns this noble's card requirements.
   *
   * @return this noble's card requirements
   */
  public CardCost getVisitRequirements() {
    return visitRequirements;
  }

  /**
   * Returns the reserved status of this noble.
   *
   * @return the reserved status of this noble
   */
  public NobleStatus getStatus() {
    return status;
  }

  /**
   * Sets the noble status to the given status.
   *
   * @param nobleStatus the given status
   */
  public void setStatus(NobleStatus nobleStatus) {
    assert nobleStatus != null;
    this.status = nobleStatus;
  }

  /**
   * Returns the nobles that are currently in the game.
   *
   * @param numPlayers the number of players in the game
   * @return a list of nobles that are currently in the game
   */
  public static List<Noble> getNobles(int numPlayers) {
    if (nobles.size() == 0) {
      generateNobles();
    }
    Random random = new Random();
    if (noblesInGame.size() == 0) {
      for (int i = 0; i < numPlayers + 1; i++) {
        int randomIndex = random.nextInt(nobles.size());
        Noble randomNoble = nobles.get(randomIndex);
        noblesInGame.add(randomNoble);
      }
    }
    return noblesInGame;
  }

  /**
   * Generates all nobles.
   * Then 3-5 random nobles will be added to the game.
   */
  private static void generateNobles() {
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
  }
}
