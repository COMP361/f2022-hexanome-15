/**
 * Oct 25, 2022
 * TODO
 */
package backend.lobbyservice;

import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * @author zacharyhayden
 *
 */
public class LobbyServiceHandler {

	/**
	 * @assert pScript != null
	 * @param <T>     type of the Event
	 * @param pScript script to run
	 * @return event handler for LS function call
	 */
	public static <T extends Event> EventHandler<T> createLSHandler(RunScript pScript) {
		assert pScript != null;

		return new EventHandler<T>() {

			@Override
			public void handle(T pEvent) {
				LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR.execute(pScript);
				pEvent.consume();

			}
		};
	}

}
