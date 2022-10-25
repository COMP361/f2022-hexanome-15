/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import java.util.concurrent.Executor;

/**
 * @author zacharyhayden
 */
public class LobbyServiceExecutor implements Executor {
	public static LobbyServiceExecutor LOBBY_SERVICE_EXECUTOR = new LobbyServiceExecutor();

	private LobbyServiceExecutor() {
	}

	public static LobbyServiceExecutor getLobbyServiceExecutor() {
		return LOBBY_SERVICE_EXECUTOR;
	}

	@Override
	public void execute(Runnable command) {
		command.run();
	}

	public void executeAsynchronously(Runnable pRunnable) {
		new Thread(pRunnable).start();
	}

}
