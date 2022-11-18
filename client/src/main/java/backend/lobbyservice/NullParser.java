package backend.lobbyservice;

import java.io.InputStream;

/**
 * Parses scripts.
 * Implements Object design pattern and Singelton design pattern.
 *
 * @author zacharyhayden
 */
public class NullParser implements OutputParser {
  public static final NullParser NULLPARSER = new NullParser();

  private NullParser() {

  }

  @Override
  public Object parse(InputStream scriptOutput) {
    return NULLPARSER.toString();
  }

  @Override
  public String toString() {
    return "NULLPARSER";
  }

  @Override
  public boolean isNull() {
    return true;
  }

}
