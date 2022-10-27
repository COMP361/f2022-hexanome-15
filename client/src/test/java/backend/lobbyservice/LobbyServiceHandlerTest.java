/**
 * Oct 27, 2022
 * TODO
 */
package backend.lobbyservice;

import static org.junit.jupiter.api.Assertions.*;
import static backend.lobbyservice.LobbyServiceHandler.createLSEventHandler;

import org.junit.jupiter.api.Test;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author zacharyhayden
 *
 */
class LobbyServiceHandlerTest {

	private final LobbyServiceExecutor ls = new LobbyServiceExecutor("http://127.0.0.1:4242",
			"/home/zacharyhayden/Documents/school/mcgill/comp361/software/Splendor/f2022-hexanome-15/client/src/main/bash/");

	@Test
	void test() {
		EventHandler<ActionEvent> eventHandler = createLSEventHandler(new ScriptExecutor<String>() {

			@Override
			public String execute() {
				return ls.debug();
			}

			@Override
			public void respond(Object scriptOutput) {
				System.out.println(scriptOutput);
				assertEquals("Lobby Service is happily serving 5 users.\n\n", scriptOutput);

			}
		}, null);

		eventHandler.handle(new ActionEvent());
	}

}
