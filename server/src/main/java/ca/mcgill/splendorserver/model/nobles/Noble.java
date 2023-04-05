package ca.mcgill.splendorserver.model.nobles;

import ca.mcgill.splendorserver.model.cards.CardCost;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Splendor Noble with reserved status and prestige.
 */
public class Noble {
  private final int id;
  private final CardCost visitRequirements;
  private NobleStatus status;
  private final int         prestige;
  private static final List<Noble> nobles  = new ArrayList<>();
  private static List<Noble> noblesInGame = new ArrayList<>();

  /**
   * Creates a noble.
   *
   * @param id the noble id
   * @param visitRequirements the token type bonus requirements for visiting this noble
   */
  public Noble(int id, CardCost visitRequirements) {
    assert id >= 0 && visitRequirements != null;
    this.visitRequirements = visitRequirements;
    this.status            = NobleStatus.ON_BOARD;
    this.prestige          = 3;
    this.id = id;
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
   * Returns the id of this noble.
   *
   * @return the id of this noble
   */
  public int getId() {
    return id;
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
    assert numPlayers >= 2 && numPlayers <= 4;
    if (nobles.size() == 0) {
      generateNobles();
    }
    List<Noble> nobleList = new ArrayList<>(nobles);
    Collections.shuffle(nobleList);
    noblesInGame = new ArrayList<>();

    for (int i = 0; i < numPlayers + 1; i++) {
      Noble noble = nobleList.get(i);
      noblesInGame.add(noble);
    }
    return noblesInGame;
  }
  
  /**
   * Get noble by id.
   *
   * @param id of sought noble
   * @return the sought noble.
   */
  public static Noble getNoble(int id) {
    if (nobles.size() == 0) {
      generateNobles();
    }
    return nobles.get(id);
  }

  /**
   * Generates all nobles.
   * Then 3-5 random nobles will be added to the game.
   */
  private static void generateNobles() {
    nobles.add(new Noble(0, new CardCost(4, 4, 0, 0, 0)));
    nobles.add(new Noble(1, new CardCost(0, 4, 4, 0, 0)));
    nobles.add(new Noble(2, new CardCost(0, 0, 4, 4, 0)));
    nobles.add(new Noble(3, new CardCost(0, 0, 0, 4, 4)));
    nobles.add(new Noble(4, new CardCost(4, 0, 0, 0, 4)));
    nobles.add(new Noble(5, new CardCost(3, 3, 0, 0, 3)));
    nobles.add(new Noble(6, new CardCost(3, 3, 3, 0, 0)));
    nobles.add(new Noble(7, new CardCost(0, 3, 3, 3, 0)));
    nobles.add(new Noble(8, new CardCost(0, 0, 3, 3, 3)));
    nobles.add(new Noble(9, new CardCost(3, 0, 0, 3, 3)));
    nobles.add(new Noble(10, new CardCost(0, 4, 0, 4, 0)));
    nobles.add(new Noble(11, new CardCost(0, 3, 3, 0, 3)));
    nobles.add(new Noble(12, new CardCost(4, 0, 4, 0, 0)));
    nobles.add(new Noble(13, new CardCost(0, 0, 4, 0, 4)));
    nobles.add(new Noble(14, new CardCost(3, 3, 0, 3, 0)));
    nobles.add(new Noble(15, new CardCost(0, 3, 0, 3, 3)));
    nobles.add(new Noble(16, new CardCost(4, 0, 0, 4, 0)));
    nobles.add(new Noble(17, new CardCost(0, 0, 4, 0, 4)));
    nobles.add(new Noble(18, new CardCost(4, 4, 0, 0, 0)));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Noble noble)) {
      return false;
    }
    return id == noble.id
             && prestige == noble.prestige
             && visitRequirements.equals(noble.visitRequirements)
             && status == noble.status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, visitRequirements, status, prestige);
  }
}
