/**
 * Oct 27, 2022
 * TODO
 */
package backend.lobbyservice;

import static org.junit.jupiter.api.Assertions.*;
import static backend.lobbyservice.LobbyServiceHandler.createlsEventHandler;

import org.junit.jupiter.api.Test;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author zacharyhayden
 *
 */
class LobbyServiceHandlerTest {

	private final LobbyServiceExecutor ls = LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR;

//	@Test
//	void test() {
//		EventHandler<ActionEvent> eventHandler = createLSEventHandler(new ScriptExecutor<String>() {
//
//			@Override
//			public String execute() {
//				return ls.debug();
//			}
//
//			@Override
//			public void respond(Object scriptOutput) {
//				System.out.println(scriptOutput);
//				assertEquals("Lobby Service is happily serving 5 users.\n\n", scriptOutput);
//
//			}
//		});
//
//		eventHandler.handle(new ActionEvent());
//	}

}
