package backend.lobbyservice;

import java.util.Optional;

/**
 * Represents a game session.
 *
 * @author zacharyhayden
 */
public class Session {
  private final String creator; // username of creator user
  private final Optional<String> saveGame;
  private final Optional<String> title;
  private final long sessionid = -1; // TODO: implement this from the LS

  /**
   * Creates a Session.
   *
   * @param creator creator of the session: user who presses create session
   * @param saveGame optional fork of previously saved game; can be null
   */
  public Session(String creator, String saveGame, String title) {
    assert creator != null && creator.length() != 0;
    this.creator = creator;
    this.saveGame = Optional.ofNullable(saveGame);
    this.title = Optional.ofNullable(title);
  }

  @Override
  public String toString() {
    if (title.isPresent()) {
      return "Session " + title.get() + " : created by " + creator;
    }
    return "Session created by " + creator;
  }

}
