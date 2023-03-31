package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.model.savegame.SaveGame;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Manages and stores savegames in memory.
 *
 * @author lawrenceberardelli
 *
 */
@Component
public class SaveGameStorage {

  private static List<SaveGame> savegames = new ArrayList<>();
  
  /**
   * Adds a savegame to the bucket of savegames.
   *
   * @param savegame to put into the bucket
   */
  public static void addSaveGame(SaveGame savegame) {
    savegames.add(savegame);
  }
  
  /**
   * Grabs a savegame from the bucket.
   *
   * @param id of game to fetch.
   * @return the game if found, null otherwise.
   */
  public static SaveGame getSaveGame(String id) {
    for (SaveGame savegame : savegames) {
      if (savegame.getId().equals(id)) {
        return savegame;
      }
    }
    return null;
  }
}
