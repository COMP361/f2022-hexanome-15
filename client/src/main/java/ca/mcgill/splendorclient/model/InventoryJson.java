package ca.mcgill.splendorclient.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Facilitates the creation of a stripped down user inventory json. 
 *
 * @author lawrenceberardelli
 *
 */
public class InventoryJson {
  
  private List<Integer> cards = new ArrayList<Integer>();
  private Map<TokenType, Integer> tokens = new HashMap<TokenType, Integer>();
  private String userName;
  private int prestige;
  private List<Integer> visitingNobles;
  private List<Power> powers;

  /**
   * Creates an InventoryJson object.
   */
  public InventoryJson() {
  }
}
