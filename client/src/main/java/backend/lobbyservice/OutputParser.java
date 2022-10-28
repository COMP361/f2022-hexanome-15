/**
 * Oct 25, 2022
 * TODO
 */
package backend.lobbyservice;

import java.io.InputStream;

/**
 * @author zacharyhayden
 * @implSpec Singleton design pattern is well advised given that access to
 *           output parser will be requested from multiple, disparate places
 *           within the system and the implementation is same and so there can
 *           be benefits to only having one instance in existence.
 */
public interface OutputParser {
	Object parse(InputStream pScriptOutput);

	default boolean isNull() {
		return false;
	}
}
