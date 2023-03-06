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

  /**
   * Creates a moveInfo object.
   *
   * @param playerName the player who performed this move
   * @param action the action that was performed
   * @param cardId the cardId of the card that was purchased/reserved
   * @param tokenType the tokenType of the token that was taken
   * @param nobleId the nobleId of the noble that was reserved/visited
   */
  public MoveInfo(String playerName, String action, String cardId,
                    String tokenType, String nobleId) {
    this.playerName = playerName;
    this.action = action;
    this.cardId = cardId;
    this.tokenType = tokenType;
    this.nobleId = nobleId;
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

}
