package backend.lobbyservice;

import java.io.InputStream;

/**
 * Parses output.
 *
 * @author zacharyhayden
 * @implSpec Singleton design pattern is well advised given that access to
 *           output parser will be requested from multiple, disparate places
 *           within the system and the implementation is same and so there can
 *           be benefits to only having one instance in existence.
 */
public interface OutputParser {
  Object parse(InputStream scriptOutput);

  default boolean isNull() {
    return false;
  }
}
