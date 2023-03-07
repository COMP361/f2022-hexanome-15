package ca.mcgill.splendorclient.view.gameboard;

import ca.mcgill.splendorclient.control.ColorManager;
import ca.mcgill.splendorclient.model.CardType;
import ca.mcgill.splendorclient.model.TokenType;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import kong.unirest.json.JSONArray;

/**
 * Represents the view of the Splendor game board.
 */
public class GameBoardView {
  
  private final static ArrayList<CardView> cardViews = new ArrayList<CardView>();
  private List<TokenPileView> tokenPileViews;
  private static GameBoardView instance = new GameBoardView();
  private static TextField whoseTurnField = new TextField();

  /**
   * Creates a GameBoardView.
   */
  private GameBoardView() {
    tokenPileViews = new ArrayList<>();
  }
  
  public static GameBoardView getInstance() {
    return instance;
  }
  
  public List<TokenPileView> getTokenPileViews() {
    return tokenPileViews;
  }
  
  public static void setWhoseTurnField(String whoseTurn) {
    whoseTurnField.setText(whoseTurn);
  }

  /**
   * Creates a stack pane for the given DeckView and returns it.
   *
   * @param deckView the DeckView that is being added to the stack pane
   * @return the stack pane for the given DeckView
   */
  private static StackPane getDeckPane(DeckView deckView) {
    StackPane deckPane = new StackPane();
    Label cardCount = deckView.getNumCardsDisplay();
    deckPane.getChildren().addAll(deckView, cardCount);
    return deckPane;
  }

  /**
   * Creates a DeckView for the given deck and returns it.
   *
   * @param type       the type of deck that is being represented by the DeckView
   * @param screenSize the size of the screen
   */
  private static DeckView createDeckView(CardType type, Dimension screenSize) {
    return new DeckView(screenSize.height / 15f, screenSize.width / 15f,
      0, ColorManager.getColor(type));
  }

  /**
   * Creates a CardView and returns it.
   *
   * @param screenSize the size of the screen
   */
  private static CardView createCardView(Dimension screenSize, String location) {
    CardView newView = new CardView(screenSize.height / 15f, screenSize.width / 15f, location);
    cardViews.add(newView);
    return newView;
  }

  /**
   * Creates a column of cards for the game board view.
   *
   * @param column     the column
   * @param screenSize the size of the screen
   * @param aggregator the list of cardViews
   */
  private static void createCardFieldColumn(VBox column, Dimension screenSize,
                                         List<CardView> aggregator, int columnCount) {
    int numRows = 3;
    for (int i = 0; i < numRows; ++i) {
      //flatten the 2d playing field for cardview location data.
      int location = columnCount * numRows + i;
      CardView cardView = createCardView(screenSize, "C" + location);
      column.getChildren().add(cardView);
      aggregator.add(cardView);
    }
  }

  /**
   * Creates a token display for the player's inventory.
   *
   * @param tokenColumn the column of tokens
   * @param screenSize  the size of the screen
   */
  private static void
        populateUserInventoryDisplay(VBox tokenColumn, Dimension screenSize,
                               final UserInventoryView userInventoryView) {
    int i = 0;
    for (CardColumnView cardColumn : userInventoryView) {
      TokenPileView pileView =
          new TokenPileView((float) screenSize.height / 55f, TokenType.values()[i]);
      Rectangle miniCard = new Rectangle(screenSize.height / 45f, screenSize.width / 50f);
      miniCard.setFill(ColorManager.getColor(TokenType.values()[i]));
      Counter cardCounter = cardColumn.getNumCardsDisplay();
      HBox tokenRow = new HBox();
      tokenRow.getChildren().addAll(pileView, pileView.getCounter(), miniCard, cardCounter);
      tokenColumn.getChildren().add(tokenRow);
      ++i;
    }
    //  Token Display for gold tokens
    final HBox tokenRow = new HBox();
    TokenPileView pileView =
        new TokenPileView((float) screenSize.height / 55f, TokenType.values()[i]);
    Rectangle miniCard = new Rectangle(screenSize.height / 45f, screenSize.width / 50f);
    Counter cardCounter = new Counter(0);
    miniCard.setFill(ColorManager.getColor(TokenType.values()[i]));
    tokenRow.getChildren().addAll(pileView, pileView.getCounter(), miniCard, cardCounter);
    tokenColumn.getChildren().add(tokenRow);
  }

  /**
   * Populates the token piles for the game board view.
   *
   * @param tokenRow   the row of tokens
   * @param screenSize the size of the screen
   */
  private static void populateGameBoardTokenPiles(HBox tokenRow,
                                                             Dimension screenSize, int numPlayers) {
    for (int i = 0; i < TokenType.values().length; ++i) {
      final VBox tokenColumn = new VBox();
      TokenPileView pileView =
          new TokenPileView((float) screenSize.height / 55f, TokenType.values()[i]);
      tokenColumn.getChildren().addAll(pileView, pileView.getCounter());
      tokenRow.getChildren().add(tokenColumn);
      instance.tokenPileViews.add(pileView);
    }
  }

  /**
   * Populates the view of the user's inventory.
   *
   * @param inventoryView   the view the user's inventory
   * @param screenSize the size of the screen
   */
  private static void populateUserInventoryView(UserInventoryView inventoryView,
                                                Dimension screenSize) {
    for (int i = 0; i < TokenType.values().length - 1; ++i) {
      inventoryView.addCardColumn(new CardColumnView(TokenType.values()[i], screenSize));
    }
  }


  private static HBox buildUserInventoryView(int numPlayer,
                                                 Dimension screenSize,
                                                 String playerName) {
    //building the user inventory
    float yoffset = 6 * screenSize.height / 10f;
    float xoffset = screenSize.width * (numPlayer + 1) / 6f;
    if (numPlayer > 0) {
      xoffset = screenSize.width * (numPlayer + 2) / 6f;
    }
    HBox userInventoryView = new HBox();
    userInventoryView.setLayoutY(yoffset);
    userInventoryView.setLayoutX(xoffset);
    VBox tokenColumn = new VBox();
    tokenColumn.setSpacing(3);
    userInventoryView.getChildren().add(tokenColumn);
    TotalTokenCountView tokenCountView = new TotalTokenCountView("Total Token Count: 15");
    TotalCardCountView cardCountView = new TotalCardCountView("Total Card Count: 0");
    TotalPrestigeCountView prestigeCountView =
        new TotalPrestigeCountView("Total Prestige Count: 0");
    tokenColumn.getChildren().addAll(tokenCountView, cardCountView, prestigeCountView);
    UserInventoryView inventoryView = new UserInventoryView(playerName);
    populateUserInventoryView(inventoryView, screenSize);
    populateUserInventoryDisplay(tokenColumn, screenSize, inventoryView);
    for (CardColumnView cardColumn : inventoryView) {
      userInventoryView.getChildren().add(cardColumn);
    }
    userInventoryView.setSpacing(10);
    return userInventoryView;
  }

  /**
   * Initializes the game board.
   *
   * @return the gameboard scene
   */
  public static Scene setupGameBoard(JSONArray players) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    //building the decks of cards
    VBox decksBox = new VBox();
    decksBox.setSpacing(3);
    decksBox.setLayoutX(screenSize.width / 6f);
    decksBox.setLayoutY(screenSize.height / 20f);
    
  //building the decks of cards
    VBox orientDecksBox = new VBox();
    orientDecksBox.setSpacing(3);
    orientDecksBox.setLayoutX(screenSize.width / 1.8f);
    orientDecksBox.setLayoutY(screenSize.height / 20f);


    //building the layout of the faceup cards
    HBox faceupCardsRow = new HBox();
    faceupCardsRow.setSpacing(3);
    faceupCardsRow.setLayoutX(screenSize.width / 6f + screenSize.width / 10f + 10);
    faceupCardsRow.setLayoutY(screenSize.height / 20f);
    VBox faceupCardsFirstColumn = new VBox();
    VBox faceupCardsSecondColumn = new VBox();
    VBox faceupCardsThirdColumn = new VBox();
    VBox faceupCardsFourthColumn = new VBox();
    VBox faceupCardsFifthColumn = new VBox();
    VBox faceupCardsSixthColumn = new VBox();
    List<CardView> cardViewAggregator = new ArrayList<>();
    List<VBox> columns = Arrays.asList(faceupCardsFirstColumn, faceupCardsSecondColumn,
        faceupCardsThirdColumn, faceupCardsFourthColumn, faceupCardsFifthColumn, faceupCardsSixthColumn);

    int columnCount = 0;
    for (VBox column : columns) {
      column.setSpacing(3);
      createCardFieldColumn(column, screenSize, cardViewAggregator, columnCount);
      ++columnCount;
    }
    faceupCardsRow.getChildren().addAll(columns);

    DeckView redDeckView = createDeckView(CardType.BASE3, screenSize);
    DeckView yellowDeckView = createDeckView(CardType.BASE2, screenSize);
    DeckView greenDeckView = createDeckView(CardType.BASE1, screenSize);
    //orientDecksBox
    DeckView orient3DeckView = createDeckView(CardType.ORIENT3, screenSize);
    DeckView orient2DeckView = createDeckView(CardType.ORIENT2, screenSize);
    DeckView orient1DeckView = createDeckView(CardType.ORIENT1, screenSize);

    decksBox.getChildren().addAll(getDeckPane(redDeckView),
        getDeckPane(yellowDeckView), getDeckPane(greenDeckView));

    orientDecksBox.getChildren().addAll(getDeckPane(orient3DeckView),
            getDeckPane(orient2DeckView), getDeckPane(orient1DeckView));

    final int nPlayers = players.length();
    //building all user inventories
    List<HBox> allUserInventoryViews = new ArrayList<HBox>();
    for (int i = 0; i < nPlayers; ++i) {
      HBox userInventoryView =
          buildUserInventoryView(i, screenSize, (String) players.get(i));
      allUserInventoryViews.add(userInventoryView);
    }

    //who's turn display
    whoseTurnField.setLayoutX(screenSize.getWidth()/2);
    whoseTurnField.setLayoutY(screenSize.getHeight()-10);
    whoseTurnField.setText("It's Here");

    //Temporary display for noble cards
    //Will replace rectangles with actual noble cards
    VBox nobleCards = new VBox();
    nobleCards.setLayoutY(screenSize.height / 20f);
    nobleCards.setLayoutX(screenSize.width / 12f);
    for (int i = 0; i < 5; i++) {
      Rectangle rectangle = new Rectangle(screenSize.height / 15f, screenSize.width / 15f);
      nobleCards.getChildren().add(rectangle);
    }
    nobleCards.setSpacing(3);

    //Creating token piles
    HBox tokenRow = new HBox();
    tokenRow.setSpacing(50);
    tokenRow.setLayoutY(5.25 * screenSize.height / 10f);
    tokenRow.setLayoutX(screenSize.width / 6f);
    populateGameBoardTokenPiles(tokenRow, screenSize, nPlayers);

    //adding to the scene graph
    Pane root = new Pane();
    root.getChildren().addAll(decksBox, orientDecksBox, faceupCardsRow, nobleCards, tokenRow);
    root.getChildren().addAll(allUserInventoryViews);
    root.getChildren().add(whoseTurnField);
    return new Scene(root, screenSize.width, screenSize.height);
  }
  
  public static void updateCardViews(int[] field) {
		cardViews.get(0).updateView(field[8]);
		cardViews.get(1).updateView(field[4]);
		cardViews.get(2).updateView(field[0]);
		cardViews.get(3).updateView(field[9]);
		cardViews.get(4).updateView(field[5]);
		cardViews.get(5).updateView(field[1]);
		cardViews.get(6).updateView(field[10]);
		cardViews.get(7).updateView(field[6]);
		cardViews.get(8).updateView(field[2]);
		cardViews.get(9).updateView(field[11]);
		cardViews.get(10).updateView(field[7]);
		cardViews.get(11).updateView(field[3]);
		cardViews.get(12).updateView(field[16]);
		cardViews.get(13).updateView(field[14]);
		cardViews.get(14).updateView(field[12]);
		cardViews.get(15).updateView(field[17]);
		cardViews.get(16).updateView(field[15]);
		cardViews.get(17).updateView(field[13]);
  }
}
