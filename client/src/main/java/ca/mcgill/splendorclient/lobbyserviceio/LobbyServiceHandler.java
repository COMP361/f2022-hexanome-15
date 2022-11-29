package ca.mcgill.splendorclient.lobbyserviceio;

import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * Handles the Lobby Service commands.
 *
 * @author zacharyhayden
 */
public class LobbyServiceHandler {

  /**
   * Creates a LobbyServiceHandler.
   */
  public LobbyServiceHandler() {

  }

  /**
   * Creates an EventHandler.
   *
   * @param <T>  Type
   * @param scriptExecutor ScriptExecutor
   * @return the new Event Handler
   */
  public static <T extends Event> EventHandler<T> createlsEventHandler(
      ScriptExecutor<?> scriptExecutor) {
    return new EventHandler<>() {

      @Override
      public void handle(T event) {
        scriptExecutor.respond(scriptExecutor.execute());

        event.consume();
      }
    };
  }

}