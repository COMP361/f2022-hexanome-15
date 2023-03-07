package ca.mcgill.splendorclient.view.gameboard;


import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.mcgill.splendorclient.control.ActionManager;
import ca.mcgill.splendorclient.control.ColorManager;
import ca.mcgill.splendorclient.model.MoveInfo;
import ca.mcgill.splendorclient.model.TokenType;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

/**
 * Represents the view of a token pile.
 * Observes token piles.
 */
public class TokenPileView extends Circle {

  private final Counter tokenCounter;
  private TokenType type;

  /**
   * Creates a TokenPileView.
   *
   * @param radius the radius of the circle used to represent the token pile
   * @param type   the type of tokens in the token pile
   */
  public TokenPileView(float radius, TokenType type) {
    super(radius);
    this.setFill(ColorManager.getColor(type));
    this.type = type;
    tokenCounter = new Counter(0);
    this.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        HttpResponse<String> result = ActionManager.findAndSendAssociatedTokenMove(type);
        if (result != null) {
          if (result.getStatus() == 206) {
            System.out.println(result.getBody().toString());
            ActionManager.handleCompoundMoves(result.getBody().toString());
          }
          if (result.getStatus() == 200) {
            //inform end of turn
          }
          else {
            //error
          }
        }
      }
      
    });
  }
  
  public TokenType getType() {
    return type;
  }
  
  /**
   * Creates a clickable TokenPileView for the gameboard view.
   * On click, it continues generating a token request code.
   *
   * @param radius the radius of the token pile view
   * @param type the type of tokens in the token pile
   * @param locationCode the location code of the token pile
   */
  public TokenPileView(float radius, TokenType type, String locationCode) {
    super(radius);
    this.setFill(ColorManager.getColor(type));
    tokenCounter = new Counter(0);
  }

  /**
   * Returns the token counter.
   *
   * @return the token counter
   */
  public Counter getCounter() {
    return tokenCounter;
  }
  
}
