package ca.mcgill.splendorclient.lobbyserviceio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Parses text.
 *
 * @author zacharyhayden
 */
public class ParseText implements OutputParser {

  /**
   * ParseText instance.
   */
  public static final ParseText PARSE_TEXT = new ParseText();

  private ParseText() {

  }

  @Override
  public Object parse(InputStream scriptOutput) {
    assert scriptOutput != null;

    StringBuilder output = new StringBuilder();
    BufferedReader reader = new BufferedReader(new InputStreamReader(scriptOutput));

    String line;
    try {
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return output.toString();
  }

}
