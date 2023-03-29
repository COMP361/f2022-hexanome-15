package ca.mcgill.splendorclient.model;

/**
 * Stores the info for a move.
 */
public class MoveInfo {
  
  private final String playerName;
  private final String action;
  private final String cardId;
  private final String tokenType;
  private final String nobleId;
  private final String cityId;
  private final String deckLevel;

  /**
   * Creates a moveInfo object.
   *
   * @param playerName the player who performed this move
   * @param action the action that was performed
   * @param cardId the cardId of the card that was purchased/reserved
   * @param tokenType the tokenType of the token that was taken
   * @param nobleId the nobleId of the noble that was reserved/visited
   * @param cityId the cityId of the city that was received
   * @param deckLevel the level of the deck that was reserved from
   */
  public MoveInfo(String playerName, String action,
                  String cardId, String tokenType,
                  String nobleId, String cityId, String deckLevel) {
    this.playerName = playerName;
    this.action = action;
    this.cardId = cardId;
    this.tokenType = tokenType;
    this.nobleId = nobleId;
    this.cityId = cityId;
    this.deckLevel = deckLevel;
  }

  /**
   * Returns the action associated with this move.
   *
   * @return the action associated with this move
   */
  public String getAction() {
    return action;
  }

  /**
   * Returns the token type associated with this move.
   *
   * @return the token type associated with this move
   */
  public String getTokenType() {
    return tokenType;
  }
  
  /**
   * Returns card id associated with this move.
   *
   * @return the card id
   */
  public String getCardId() {
    return cardId;
  }

  /**
   * Returns noble id associated with this move.
   *
   * @return the noble id
   */
  public String getNobleId() {
    return nobleId;
  }

  /**
   * Returns city id associated with this move.
   *
   * @return the city id
   */
  public String getCityId() {
    return cityId;
  }

  /**
   * Returns the deck level associated with this move.
   *
   * @return the deck level
   */
  public String getDeckLevel() {
    return deckLevel;
  }
}
