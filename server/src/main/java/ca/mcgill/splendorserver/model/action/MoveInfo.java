package ca.mcgill.splendorserver.model.action;

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

}
