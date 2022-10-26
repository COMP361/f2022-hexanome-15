/**
 * Oct 25, 2022
 * TODO
 */
package backend.lobbyservice;

import java.io.InputStream;

/**
 * @author zacharyhayden
 * @see Null object design pattern, singelton design pattern.
 */
public class NullParser implements OutputParser {
	public static final NullParser NULLPARSER = new NullParser();

	private NullParser() {

	}

	@Override
	public Object parse(InputStream pScriptOutput) {
		return NULLPARSER;
	}
	
	@Override
	public String toString() {
		return "NULLPARSER";
	}

}
