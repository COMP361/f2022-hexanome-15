package ca.mcgill.splendorserver.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;

/**
 * ParseJSON.
 *
 * @author zacharyhayden
 */
public class Parsejson {
  /**
   * Parsejson instance.
   */
  public static final Parsejson PARSE_JSON = new Parsejson();

  private Parsejson() {

  }

  /**
   * Parses a the output from a curl request.
   *
   * @param scriptOutput output from execution
   * @return the parsed output
   */
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
      System.exit(1);
    }
    return new JSONObject(output.toString());
  }

}
