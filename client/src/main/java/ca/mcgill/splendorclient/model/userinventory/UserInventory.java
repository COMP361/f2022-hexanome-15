package ca.mcgill.splendorclient.model.userinventory;

import java.util.ArrayList;

import ca.mcgill.splendorclient.model.cards.Card;
import ca.mcgill.splendorclient.model.tokens.TokenPile;

/**
 * Represents the inventory of a Splendor player.
 * Contains cards and token piles.
 */
public class UserInventory {

  ArrayList<Card> cards;

  ArrayList<TokenPile> tokenPiles;

}
