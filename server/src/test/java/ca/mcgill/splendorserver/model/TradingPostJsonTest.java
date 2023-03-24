package ca.mcgill.splendorserver.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class TradingPostJsonTest {

  @Test
  void createTradingPostJson() {
    TradingPostJson tradingPostJson = new TradingPostJson(1, new ArrayList<>());
    assertNotEquals(null, tradingPostJson);
  }

}