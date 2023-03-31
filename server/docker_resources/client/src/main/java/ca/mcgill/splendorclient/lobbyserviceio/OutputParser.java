package ca.mcgill.splendorclient.lobbyserviceio;

import java.io.InputStream;

/**
 * Parses output.
 * Singleton design pattern is well advised given that access to
 * output parser will be requested from multiple, disparate places
 * within the system and the implementation is same and so there can
 * be benefits to only having one instance in existence.
 *
 * @author zacharyhayden
 */
public interface OutputParser {
  /**
   * Parses the input stream and returns an object.
   *
   * @param scriptOutput the output of the script
   * @return the object
   */
  Object parse(InputStream scriptOutput);

  /**
   * Returns whether the output is null.
   *
   * @return boolean determining whether the output is null
   */
  default boolean isNull() {
    return false;
  }
}
