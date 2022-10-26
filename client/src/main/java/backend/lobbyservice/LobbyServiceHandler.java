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

	/**
	 * @assert pScript != null
	 * @param <T>          type of the Event
	 * @param pScript      script to run
	 * @param pError       alert to be triggered if the handler encounters an empty
	 *                     argument in the run script
	 * @param pSuccess     alert to be triggered if the handler doesn't encounter
	 *                     empty arguments for the pScript and it was executed
	 *                     properly. If none then leave null
	 * @param pHandlerTask handler for the output of executing the run script,
	 *                     should be an anonymous class implementation. Can be null
	 * @return event handler for LS function call
	 */
	public static <T extends Event> EventHandler<T> createLSHandler(RunScript pScript, Alert pError, Alert pSuccess,
			HandlerTask pHandlerTask) {
		assert pScript != null && pError != null;

		return new EventHandler<T>() {

			@Override
			public void handle(T pEvent) {
				// making sure that if any of the parameters are empty to throw the injected
				// error Alert
				for (String arg : pScript) {
					if (arg.length() == 0) {
						pError.show();
						return;
					}
				}

				LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR.execute(pScript);
				pEvent.consume();

				if (pHandlerTask != null) {
					pHandlerTask.handle();
				}

				if (pSuccess != null) {
					// success outcome
					pSuccess.show();
				}
			}
		};
	}

}