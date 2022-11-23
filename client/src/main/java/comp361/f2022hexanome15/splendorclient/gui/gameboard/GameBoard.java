package comp361.f2022hexanome15.splendorclient.gui.gameboard;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import comp361.f2022hexanome15.splendorclient.model.ColorManager;
import comp361.f2022hexanome15.splendorclient.model.cards.CardType;
import comp361.f2022hexanome15.splendorclient.model.cards.Deck;
import comp361.f2022hexanome15.splendorclient.model.tokens.TokenPile;
import comp361.f2022hexanome15.splendorclient.model.tokens.TokenType;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Represents the view of the Splendor game board.
 */
public class GameBoard {


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
   * @param deck the deck that is being represented by the DeckView
   * @param screenSize the size of the screen
   */
  private static DeckView createDeckView(Deck deck, Dimension screenSize) {
    return new DeckView(deck, screenSize.height / 15f, screenSize.width / 15f);
  }

  /**
   * Creates a CardView and returns it.
   *
   * @param screenSize the size of the screen
   */
  private static CardView createCardView(Dimension screenSize) {
    return new CardView(screenSize.height / 15f, screenSize.width / 15f);
  }

  /**
   * Creates a column of cards for the game board view.
   *
   * @param column the column
   * @param screenSize the size of the screen
   * @param decks the list of decks that are used for this game
   * @param deckViews the list of deckViews that represent the decks in this game
   * @param aggregator the list of cardViews
   */
  private static void populateCardColumn(VBox column, Dimension screenSize,
                                         List<Deck> decks, ArrayList<DeckView> deckViews,
                                         ArrayList<CardView> aggregator) {
    //pretty sloppy but will do for now
    for (int i = 0; i < 3; ++i) {
      CardView cardView = createCardView(screenSize);
      decks.get(i).addListener(cardView);
      column.getChildren().add(cardView);
      aggregator.add(cardView);
      cardView.addListener(deckViews.get(i));
    }
  }

  /**
   * Creates a token display for the player's inventory.
   *
   * @param tokenColumn the column of tokens
   * @param screenSize the size of the screen
   */
  private static void populateTokenDisplay(VBox tokenColumn, Dimension screenSize) {
    for (int i = 0; i < TokenType.values().length; ++i) {
      HBox tokenRow = new HBox();
      TokenPile deck = new TokenPile(TokenType.values()[i]);
      TokenPileView deckView = new TokenPileView((float) screenSize.height / 55f, deck);
      deckView.setUpDemo();
      Rectangle miniCard = new Rectangle(screenSize.height / 45f, screenSize.width / 50f);
      miniCard.setFill(ColorManager.getColor(deck.getType()));
      Counter cardCounter = new Counter(0);
      tokenRow.getChildren().addAll(deckView, deckView.getCounter(), miniCard, cardCounter);
      tokenColumn.getChildren().add(tokenRow);
    }
  }

  /**
   * Populates the token piles for the game board view.
   *
   * @param tokenRow the row of tokens
   * @param screenSize the size of the screen
   */
  private static void populateTokenPiles(HBox tokenRow, Dimension screenSize) {
    for (int i = 0; i < TokenType.values().length; ++i) {
      VBox tokenColumn = new VBox();
      TokenPile deck = new TokenPile(TokenType.values()[i]);
      TokenPileView deckView = new TokenPileView((float) screenSize.height / 55f, deck);
      deckView.setUp();
      tokenColumn.getChildren().addAll(deckView, deckView.getCounter());
      tokenRow.getChildren().add(tokenColumn);
    }
  }

  /**
   * Populates the view of the user's inventory.
   *
   * @param handView the view the user's inventory
   * @param screenSize the size of the screen
   */
  private static void populateHandView(HandView handView, Dimension screenSize) {
    for (int i = 0; i < TokenType.values().length - 1; ++i) {
      handView.addHandColumn(new HandColumnView(TokenType.values()[i], screenSize));
    }
  }

  /**
   * Initializes the game board.
   */
  public static Scene setupGameBoard() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    //building the decks of cards
    VBox decksBox = new VBox();
    decksBox.setSpacing(3);
    Deck redDeck = new Deck(CardType.BASE3);
    Deck yellowDeck = new Deck(CardType.BASE2);
    Deck greenDeck = new Deck(CardType.BASE1);
    List<Deck> decks = Arrays.asList(redDeck, yellowDeck, greenDeck);
    decksBox.setLayoutX(screenSize.width / 6f);
    decksBox.setLayoutY(screenSize.height / 20f);
    DeckView redDeckView = createDeckView(redDeck, screenSize);
    DeckView yellowDeckView = createDeckView(yellowDeck, screenSize);
    DeckView greenDeckView = createDeckView(greenDeck, screenSize);
    ArrayList<DeckView> deckViews = new ArrayList<>();
    deckViews.add(redDeckView);
    deckViews.add(yellowDeckView);
    deckViews.add(greenDeckView);

    //building the layout of the faceup cards
    HBox faceupCardsRow = new HBox();
    faceupCardsRow.setSpacing(3);
    faceupCardsRow.setLayoutX(screenSize.width / 6f + screenSize.width / 10f + 10);
    faceupCardsRow.setLayoutY(screenSize.height / 20f);
    VBox faceupCardsFirstColumn = new VBox();
    VBox faceupCardsSecondColumn = new VBox();
    VBox faceupCardsThirdColumn = new VBox();
    VBox faceupCardsFourthColumn = new VBox();
    ArrayList<CardView> cardCardViewAggregator = new ArrayList<>();
    List<VBox> columns = Arrays.asList(faceupCardsFirstColumn, faceupCardsSecondColumn,
        faceupCardsThirdColumn, faceupCardsFourthColumn);

    for (VBox column : columns) {
      column.setSpacing(3);
      populateCardColumn(column, screenSize, decks, deckViews, cardCardViewAggregator);
    }
    faceupCardsRow.getChildren().addAll(columns);
    for (DeckView deckView : deckViews) {
      deckView.setUp();
    }

    decksBox.getChildren().addAll(getDeckPane(redDeckView),
        getDeckPane(yellowDeckView), getDeckPane(greenDeckView));

    //ignoring the pretty token display for now

    //building the user inventory
    float yoffset = 6 * screenSize.height / 10f;
    float xoffset = screenSize.width / 6f;
    HBox userInventory = new HBox();
    userInventory.setLayoutY(yoffset);
    userInventory.setLayoutX(xoffset);
    VBox tokenColumn = new VBox();
    tokenColumn.setSpacing(3);
    userInventory.getChildren().add(tokenColumn);
    populateTokenDisplay(tokenColumn, screenSize);
    //these will have to be more sophisticated or at least listeners sometime soon
    Text totalTokens = new Text("Tokens: 0/10");
    Text totalCards = new Text("Cards: 0");
    Text prestige = new Text("Prestige: 0");
    tokenColumn.getChildren().addAll(totalTokens, totalCards, prestige);
    HandView handView = new HandView();
    populateHandView(handView, screenSize);
    for (CardView cardView : cardCardViewAggregator) {
      for (HandColumnView handColumn : handView) {
        cardView.addListener(handColumn);
      }
    }
    for (HandColumnView handColumn : handView) {
      userInventory.getChildren().add(handColumn);
    }
    userInventory.setSpacing(10);

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
    tokenRow.setLayoutX(xoffset);
    populateTokenPiles(tokenRow, screenSize);

    //adding to the scene graph
    Pane root = new Pane();
    root.getChildren().addAll(decksBox, faceupCardsRow, userInventory, nobleCards, tokenRow);
    return new Scene(root, screenSize.width, screenSize.height);
  }

}