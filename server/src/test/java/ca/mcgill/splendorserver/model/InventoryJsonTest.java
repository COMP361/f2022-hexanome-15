package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.gameio.PlayerWrapper;
import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.cities.City;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.Token;
import ca.mcgill.splendorserver.model.tradingposts.TradingPostSlot;
import ca.mcgill.splendorserver.model.userinventory.UserInventory;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static ca.mcgill.splendorserver.model.cards.DeckType.BASE1;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ONE;
import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsType.RED;
import static ca.mcgill.splendorserver.model.tradingposts.Power.PURCHASE_CARD_TAKE_TOKEN;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InventoryJsonTest {
  private String userName = "Sofia";
  private PlayerWrapper player = PlayerWrapper.newPlayerWrapper(userName);
  private UserInventory inventory = new UserInventory(player, Optional.ofNullable(RED));

  @Test
  void InventoryJson() {
    CardCost cost = new CardCost(1,0,0,0,0);
    Card card1 = new Card(1,1, DIAMOND, BASE1, ONE, cost);
    Card card2 = new Card(2, 1, DIAMOND, BASE1, ONE, cost);
    Noble noble = new Noble(0, new CardCost(1,0,0,0,0));
    TradingPostSlot tradingSlot = new TradingPostSlot(0, false, PURCHASE_CARD_TAKE_TOKEN, cost);
    City city = new City(0, 0, cost, 0);
    Token token = new Token(DIAMOND);
    inventory.addToken(token);
    inventory.purchaseCard(card1);
    inventory.addReservedCard(card2);
    inventory.receiveVisitFrom(noble);
    inventory.addPower(tradingSlot.getPower());
    inventory.addCity(city);
    InventoryJson json = new InventoryJson(inventory.getCards(),
      inventory.getTokenPiles(), userName, inventory.getPrestigeWon(), inventory.getNobles(),
      inventory.getPowers(), inventory.getCoatOfArmsPile(), inventory.getCities());
    assertNotEquals(null, json);

  }

}