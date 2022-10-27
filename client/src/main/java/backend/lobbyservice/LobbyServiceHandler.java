/**
 * Oct 25, 2022
 * TODO
 */
package backend.lobbyservice;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;

/**
 * @author zacharyhayden
 *
 */
public class LobbyServiceHandler {

	public static <T extends Event> EventHandler<T> createLSEventHandler(ScriptExecutor<?> pScriptExecutor, Alert pSuccess) {
		return new EventHandler<T>() {

			@Override
			public void handle(T pEvent) {
				pScriptExecutor.respond(pScriptExecutor.execute());

				if (pSuccess != null) {
					// success outcome
					pSuccess.show();
				}

				pEvent.consume();
			}
		};
	}

}