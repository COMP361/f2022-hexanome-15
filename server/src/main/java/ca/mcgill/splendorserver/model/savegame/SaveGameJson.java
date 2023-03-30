package ca.mcgill.splendorserver.model.savegame;

import java.util.List;

/**
 * Utility to automate translation to/from JSON for the body of the
 * savegame request.
 *
 * @author lawrenceberardelli
 *
 */
public class SaveGameJson {
  
  public String gameName;
  
  public List<String> players;
  
  public String savegameid;
  
  /**
   * Creates a savegame json for the body of the register savegame request.
   *
   * @param name the name of the user which registered the gameservice
   * @param players in the game
   * @param id of the savegame
   */
  public SaveGameJson(String name, List<String> players, String id) {
    gameName = name;
    this.players = players;
    savegameid = id;
  }

}
