package ca.mcgill.splendorserver.model;

import ca.mcgill.splendorserver.model.cards.Card;
import ca.mcgill.splendorserver.model.cards.CardCost;
import ca.mcgill.splendorserver.model.nobles.Noble;
import ca.mcgill.splendorserver.model.tokens.TokenPile;
import ca.mcgill.splendorserver.model.tokens.TokenType;
import ca.mcgill.splendorserver.model.tradingposts.CoatOfArmsPile;
import ca.mcgill.splendorserver.model.tradingposts.Power;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static ca.mcgill.splendorserver.model.cards.DeckType.BASE1;
import static ca.mcgill.splendorserver.model.cards.TokenBonusAmount.ZERO;
import static ca.mcgill.splendorserver.model.tokens.TokenType.DIAMOND;
import static org.junit.jupiter.api.Assertions.*;

class InventoryJsonTest {
  List<Card> cards = new ArrayList<>();;
  EnumMap<TokenType, TokenPile> tokens;
  String userName;
  int prestige;
  List<Noble> visitingNobles;
  List<Power> powers;
  CoatOfArmsPile pile;
  Card card1;
  CardCost cost;

  @Test
  void InventoryJson() {
    cards = new ArrayList<>();
    visitingNobles = new ArrayList<>();
    powers = new ArrayList<>();
    tokens = new EnumMap<>(TokenType.class);
    prestige = 1;
    cost = new CardCost(1,0,0,0,0);
    card1 = new Card(1,2,DIAMOND,BASE1,ZERO,cost);
    cards.add(card1);
    InventoryJson json = new InventoryJson(cards,tokens,userName,prestige,visitingNobles,powers,pile);
    assertEquals(card1,cards.get(0),"");

  }

}