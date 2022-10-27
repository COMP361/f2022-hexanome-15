/**
 * Oct 26, 2022
 * TODO
 */
package backend.lobbyservice;

/**
 * @author zacharyhayden
 * @apiNote Used for extending client provided functionality within the
 *          LobbyServiceHandler class
 */
public interface HandlerTask {
	/**
	 * Performs client defined operations after the RunScript has been executed
	 * inside the EventHandler created by the LobbyServiceHandler factory method.
	 */
	void handle();
}
