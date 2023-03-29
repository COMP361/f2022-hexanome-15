package ca.mcgill.splendorclient.view.gameboard;

import ca.mcgill.splendorclient.control.ActionManager;
import ca.mcgill.splendorclient.control.ColorManager;
import ca.mcgill.splendorclient.model.TokenType;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

/**
 * Represents the view of a token pile.
 * Observes token piles.
 */
public class TokenPileView extends StackPane {

  private final Circle icon;
  private final Counter tokenCounter;
  private TokenType type;

  /**
   * Creates a TokenPileView.
   *
   * @param radius the radius of the circle used to represent the token pile
   * @param type   the type of tokens in the token pile
   */
  public TokenPileView(float radius, TokenType type) {
    super();
    //this.setPrefSize(radius * 2, radius * 2);
    icon = new Circle(radius);
    icon.setFill(ColorManager.getColor(type));
    this.type = type;
    tokenCounter = new Counter(0);
    this.getChildren().addAll(icon, tokenCounter);
    icon.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        HttpResponse<String> result = ActionManager.findAndSendAssociatedTakeTokenMove(type);
        if (result != null) {
          if (result.getStatus() == 206) {
            ActionManager.handleCompoundMoves(result.getBody().toString());
          }
          if (result.getStatus() == 200) {
            //inform end of turn
          } else {
            //error
          }
        } else {
          result = ActionManager.findAndSendAssociatedReturnTokenMove(type);
          if (result != null) {
            if (result.getStatus() == 206) {
              ActionManager.handleCompoundMoves(result.getBody().toString());
            }
          } else {
            result = ActionManager.findAndSendAssociatedTakeExtraTokenMove(type);
            if (result != null) {
              if (result.getStatus() == 206) {
                ActionManager.handleCompoundMoves(result.getBody().toString());
              }
            }
          }
        }
      }

    });
  }

  /**
   * Returns the token type of the token pile view.
   *
   * @return the token type of the token pile view
   */
  public TokenType getType() {
    return type;
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
