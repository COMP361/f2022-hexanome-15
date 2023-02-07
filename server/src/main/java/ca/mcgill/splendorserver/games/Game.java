package ca.mcgill.splendorserver.games;

import ca.mcgill.splendorserver.model.GameBoard;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a game session.
 *
 * @author zacharyhayden
 */
@Entity
public class Game implements Iterable<PlayerWrapper>
{
    private final String savegame;
    @Embedded
    private final TurnManager turnManager; // contains player user-names
    @Id
    private Long gameid; // TODO: implement this from the LS
    private String initGameBoard; // the game-board which initialized the game
    private String lastAction = "";
    private       GameBoard gameBoard;
    private final boolean   finished = false;


    /**
     * Creates a Game.
     *
     * @param saveGame optional fork of previously saved game; can be null
     * @param gameid   unique game ID
     * @param players  list of players in game session as indicated from the rest
     *                 call
     * @param gb       The gameboard
     */
    public Game(String saveGame, long gameid, List<PlayerWrapper> players, GameBoard gb)
    {
        this.savegame = saveGame;
        this.gameid = gameid;
        this.turnManager = new TurnManager(players);
        gameBoard = gb;
    }

    public boolean isFinished() {return finished;}

    /**
     * Returns the game id.
     *
     * @return the game id
     */
    public long gameid()
    {
        return gameid;
    }

    /**
     * Returns the savegame.
     *
     * @return the savegame
     */
    public String savegame()
    {
        return savegame;
    }

    /**
     * Removes a player.
     *
     * @param username The removed player's username
     */
    public void removePlayer(PlayerWrapper username)
    {
        turnManager.removePlayer(username);
    }

    /**
     * Ends the current players turn and updates the current player.
     *
     * @return the player wrapper whose turn it now is
     */
    public PlayerWrapper endTurn()
    {
        return turnManager.endTurn();
    }

    /**
     * Returns the player whose turn it is.
     *
     * @return the player whose turn it is
     */
    public PlayerWrapper whoseTurn()
    {
        return turnManager.whoseTurn();
    }

    @Override
    public String toString()
    {
        return "Game [savegame=" + savegame + ", gameID=" + gameid + ", aTakeTurn=" + turnManager + "]";
    }

    @Override
    public Iterator<PlayerWrapper> iterator()
    {
        return turnManager.iterator();
    }

    /**
     * Updates the gameboard in the server.
     *
     * @param newGameBoard the updated gameboard
     */
    public void updateGameBoard(String newGameBoard)
    {
        assert newGameBoard != null;
        this.initGameBoard = newGameBoard;
    }

    /**
     * Returns the gameboard.
     *
     * @return the gameboard
     */
    public GameBoard getGameBoard()
    {
        return gameBoard;
    }

    /**
     * Returns the last action that a player made.
     *
     * @return the lastAction
     */
    public String getLastAction()
    {
        return lastAction;
    }

    /**
     * Sets the last action that a player made.
     *
     * @param lastAction the lastAction to set
     */
    public void setLastAction(String lastAction)
    {
        this.lastAction = lastAction;
    }

}
