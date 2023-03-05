package ca.mcgill.splendorclient.model;

public class MoveInfo {
  
  private final String playerName;
  private final String action;
  private final String cardId;
  private final String tokenType;
  private final String nobleId;
  
  public MoveInfo(String playerName, String action, String cardId, String tokenType, String nobleId) {
    this.playerName = playerName;
    this.action = action;
    this.cardId = cardId;
    this.tokenType = tokenType;
    this.nobleId = nobleId;
  }

}
