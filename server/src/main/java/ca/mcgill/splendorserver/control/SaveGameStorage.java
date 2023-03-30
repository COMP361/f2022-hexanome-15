package ca.mcgill.splendorserver.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ca.mcgill.splendorserver.model.savegame.SaveGame;

/**
 * Manages and stores savegames in memory
 *
 * @author lawrenceberardelli
 *
 */
@Component
public class SaveGameStorage {

  private static List<SaveGame> savegames = new ArrayList<>();
  
  public static void addSaveGame(SaveGame savegame) {
    savegames.add(savegame);
  }
  
  public static SaveGame getSaveGame(String id) {
    for (SaveGame savegame : savegames) {
      if (savegame.getId().equals(id)) {
        return savegame;
      }
    }
    return null;
  }
}
