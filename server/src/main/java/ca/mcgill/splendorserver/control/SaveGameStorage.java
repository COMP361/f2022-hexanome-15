package ca.mcgill.splendorserver.control;

import ca.mcgill.splendorserver.model.savegame.SaveGame;
import ca.mcgill.splendorserver.model.savegame.SaveGameJson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

  private List<SaveGame> savegames = new ArrayList<>();
  private Path savegamepath = Paths.get("savegames");
  private Path savegamebodypath = Paths.get("savegamebody");
  private static SaveGameStorage instance = new SaveGameStorage();
  
  /**
   * Generates the savegame directory if it doesn't exist.
   */
  private SaveGameStorage() {
    System.out.println(savegamepath.toAbsolutePath());
    System.out.println(savegamebodypath.toAbsolutePath());
    if (Files.notExists(savegamepath)) {
      try {
        Files.createDirectory(savegamepath);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    if (Files.notExists(savegamebodypath)) {
      try {
        Files.createDirectories(savegamebodypath);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Fulfilling the singleton pattern.
   *
   * @return the single instance of savegamestorage.
   */
  public static SaveGameStorage getInstance() {
    return instance;
  }
  
  /**
   * Gets the save games.
   *
   * @return the save games
   */
  public List<SaveGame> getSaveGames() {
    return this.savegames;
  }
  
  /**
   * Adds a savegame to the bucket of savegames.
   *
   * @param savegame to put into the bucket
   */
  public void addSaveGame(SaveGame savegame) {
    savegames.add(savegame);
    File permanentsavegame = new File(savegamepath + "/" + savegame.getId());
    File permanentsavegamebody = new File(savegamebodypath + "/" + savegame.getId());
    try {
      permanentsavegame.createNewFile();
      permanentsavegamebody.createNewFile();
      FileWriter gameboardwriter = new FileWriter(permanentsavegame.getAbsolutePath());
      gameboardwriter.write(savegame.getJson());
      FileWriter savegamebodywriter = new FileWriter(permanentsavegamebody.getAbsoluteFile());
      savegamebodywriter.write(savegame.getBody());
      gameboardwriter.close();
      savegamebodywriter.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /**
   * Loops over the savegame directory and adds savegames to the in memory bucket.
   */
  public void loadSaveGames() {
    File dir = new File(savegamepath.toString());
    for (File savegamefile : dir.listFiles()) {
      try {
        String json = Files.readString(savegamefile.toPath());
        String id = savegamefile.toPath().getFileName().toString();
        String body = Files.readString(Paths.get(savegamebodypath + "/" + id));
        SaveGame savegame = new SaveGame(id, json, body);
        savegames.add(savegame);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Grabs a savegame from the bucket.
   *
   * @param id of game to fetch.
   * @return the game if found, null otherwise.
   */
  public SaveGame getSaveGame(String id) {
    for (SaveGame savegame : savegames) {
      if (savegame.getId().equals(id)) {
        return savegame;
      }
    }
    return null;
  }
}
