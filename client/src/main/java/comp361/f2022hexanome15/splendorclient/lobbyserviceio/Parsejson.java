package comp361.f2022hexanome15.splendorclient.lobbyserviceio;

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
public class Parsejson implements OutputParser {
  public static final Parsejson PARSE_JSON = new Parsejson();

  private Parsejson() {

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
      System.exit(1);
    }
    return new JSONObject(output.toString());
  }

  /**
   * Get JSON object from key.
   *
   * @param json parsed json object, will require a cast when used with output
   *              from executing
   * @param key  string key to search for and get associated value of
   * @return the returned value corresponding to the key
   * @assert pJson != null && pKey != null
   */
  public Object getFromKey(JSONObject json, String key) {
    assert json != null && key != null;
    return json.get(key);
  }

}
