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

	public static <T extends Event> EventHandler<T> createLSEventHandler(ScriptExecutor<?> pScriptExecutor) {
		return new EventHandler<T>() {

			@Override
			public void handle(T pEvent) {
				pScriptExecutor.respond(pScriptExecutor.execute());

				pEvent.consume();
			}
		};
	}

}