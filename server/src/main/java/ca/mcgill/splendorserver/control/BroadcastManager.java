package ca.mcgill.splendorserver.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages the game instances. TODO: maybe long polling but seems more nice to have than necessary.
 *
 * @author lawrenceberardelli
 */
public class BroadcastManager
{

    private static final List<GameBoardManager> activeGames = new ArrayList<>();

    private BroadcastManager()
    {
    }

    public static void addActiveGame(GameBoardManager manager)
    {
        for (GameBoardManager gameManager : activeGames) {
            if (gameManager.getGameId() == manager.getGameId())
                return;
        }
        activeGames.add(manager);
    }

    public static void removeActiveGame(GameBoardManager manager)
    {
        activeGames.remove(manager);
    }

    public static Optional<GameBoardManager> getActiveGame(long gameid)
    {
        for (GameBoardManager manager : activeGames) {
            if (manager.getGameId() == gameid) {
                return Optional.of(manager);
            }
        }
        return Optional.empty();
    }

    /**
     * Check if gameid exists and is active game.
     * @param gameid game ID
     * @return if exists and is active.
     */
    public static boolean exists(long gameid)
    {
        for (GameBoardManager manager : activeGames) {
            if (manager.getGameId() == gameid) return true;
        }
        return false;
    }

}
