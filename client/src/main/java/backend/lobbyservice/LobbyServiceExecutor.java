/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zacharyhayden
 * @see singleton design pattern
 */
public class LobbyServiceExecutor {
	public static final LobbyServiceExecutor LOBBY_SERVICE_EXECUTOR = new LobbyServiceExecutor();

	private LobbyServiceExecutor() {
	}

	/**
	 * Default execution is synchronous
	 */
	public void execute(RunScript command) {
		assert command != null;

		try {
			Method run = command.getClass().getDeclaredMethod("run");
			run.setAccessible(true);
			run.invoke(command, new Object[] {}); // no parameters to run method

			System.out.println("Executed: " + command);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}

}
