package ca.mcgill.splendorclient.lobbyserviceio;

/**
 * Executes bash scripts.
 *
 * @param <T> Type
 * @author zacharyhayden
 */
public interface ScriptExecutor<T> {
  /**
   * Executes a client specified script from the LobbyServiceExecutor
   * within this interface implementation.
   *
   * @return output of the script if any
   */
  T execute();

  /**
   * Responds to the output produced from execute method; can do nothing and just return.
   *
   * @param object output of calling the execute method
   */
  void respond(Object object);
}
