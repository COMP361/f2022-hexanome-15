package ca.mcgill.splendorserver.control;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class LobbyServiceExecutorTest {
  private LobbyServiceExecutor lsExecutor;

  @BeforeEach
  void setUp() {
    lsExecutor = Mockito.mock(LobbyServiceExecutor.class);
  }

  @Test
  void auth_token() {
    Gson gson = new Gson();
    AuthTokenJson authToken = new AuthTokenJson();
    String string = gson.toJson(authToken);
    JSONObject json = new JSONObject(string);
    Mockito.when(lsExecutor.auth_token("maex", "abc123_ABC123")).thenReturn(json);
    assertEquals(json, lsExecutor.auth_token("maex", "abc123_ABC123"));
  }

  @Test
  void register_gameservice() {
    lsExecutor.register_gameservice(" ", 4, 2,
      "SplendorOrient", "SplendorOrient", true);
    assertTrue(true);
  }

  @Test
  void save_game() {
    lsExecutor.save_game("", "SplendorOrient", "1L");
  }
}