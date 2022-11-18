package backend.lobbyservice;

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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return output.toString();
  }

}
