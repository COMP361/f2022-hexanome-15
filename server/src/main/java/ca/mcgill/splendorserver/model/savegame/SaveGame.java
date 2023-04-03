package ca.mcgill.splendorserver.model.savegame;

/**
 * SaveGame model storing the json representation of the gamestate and id of game. 
 *
 * @author lawrenceberardelli
 *
 */
public class SaveGame {
  
  private String id;
  private String json;
  //For forwarding on re-registration.
  private String body;
  
  /**
   * Creates a savegame model object.
   *
   * @param id generated; key for a savegame
   * @param json the representation of a savegame
   */
  public SaveGame(String id, String json, String body) {
    this.id = id;
    this.json = json;
    this.body = body;
  }
  
  /**
   * Gets savegame id.
   *
   * @return id of this savegame
   */
  public String getId() {
    return id;
  }
  
  /**
   * Gets the json of this savegame.
   *
   * @return json of this savegame
   */
  public String getJson() {
    return json;
  }
  
  /**
   * Returns the body for the registration request to Lobby Service.
   *
   * @return the body
   */
  public String getBody() {
    return body;
  }
  
}
