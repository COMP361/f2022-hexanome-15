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
import comp361.f2022hexanome15.splendorclient.model.userinventory.UserInventory;
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
public class GameBoardView {


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
  private static List<TokenPile> populateUserInventoryDisplay(VBox tokenColumn, Dimension screenSize) {
	List<TokenPile> piles = new ArrayList<TokenPile>();
    for (int i = 0; i < TokenType.values().length; ++i) {
      HBox tokenRow = new HBox();
      TokenPile deck = new TokenPile(TokenType.values()[i]);
      piles.add(deck);
      TokenPileView deckView = new TokenPileView((float) screenSize.height / 55f, deck.getType());
      deck.setUpDemo();
      Rectangle miniCard = new Rectangle(screenSize.height / 45f, screenSize.width / 50f);
      miniCard.setFill(ColorManager.getColor(deck.getType()));
      Counter cardCounter = new Counter(0);
      tokenRow.getChildren().addAll(deckView, deckView.getCounter(), miniCard, cardCounter);
      tokenColumn.getChildren().add(tokenRow);
    }
    return piles;
  }

  /**
   * Populates the token piles for the game board view.
   *
   * @param tokenRow the row of tokens
   * @param screenSize the size of the screen
   */
  private static List<TokenPile> populateGameBoardTokenPiles(HBox tokenRow, Dimension screenSize) {
	List<TokenPile> piles = new ArrayList<TokenPile>();
    for (int i = 0; i < TokenType.values().length; ++i) {
      VBox tokenColumn = new VBox();
      TokenPile deck = new TokenPile(TokenType.values()[i]);
      piles.add(deck);
      //TODO: make these a new kind of view for the game board displays (populate the piles instead of removing the piles)
      TokenPileView deckView = new TokenPileView((float) screenSize.height / 55f, deck.getType());
      deck.setUp();
      tokenColumn.getChildren().addAll(deckView, deckView.getCounter());
      tokenRow.getChildren().add(tokenColumn);
    }
    return piles;
  }
  
  /**
   * The gameboard token piles and the user inventory token piles need to be in communication
   * 
   * @param gameBoardPiles
   * @param userInventoryPiles
   */
  private static void linkGameboardAndUserInventoryTokenPiles(List<TokenPile> gameBoardPiles, List<TokenPile> userInventoryPiles) {
	for (TokenPile gameBoardPile : gameBoardPiles) {
		for (TokenPile userInventoryPile : userInventoryPiles) {
			
		}
	}
  }

  /**
   * Populates the view of the user's inventory.
   *
   * @param handView the view the user's inventory
   * @param screenSize the size of the screen
   */
  private static void populateHandView(UserInventoryView handView, Dimension screenSize) {
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

    //building the user inventory still only one
    float yoffset = 6 * screenSize.height / 10f;
    float xoffset = screenSize.width / 6f;
    HBox userInventoryView = new HBox();
    userInventoryView.setLayoutY(yoffset);
    userInventoryView.setLayoutX(xoffset);
    VBox tokenColumn = new VBox();
    tokenColumn.setSpacing(3);
    userInventoryView.getChildren().add(tokenColumn);
    populateUserInventoryDisplay(tokenColumn, screenSize);
    //these will have to be more sophisticated or at least listeners sometime soon
    Text totalTokens = new Text("Tokens: 0/10");
    Text totalCards = new Text("Cards: 0");
    Text prestige = new Text("Prestige: 0");
    tokenColumn.getChildren().addAll(totalTokens, totalCards, prestige);
    UserInventoryView handView = new UserInventoryView();
    populateHandView(handView, screenSize);
    for (CardView cardView : cardCardViewAggregator) {
      for (HandColumnView handColumn : handView) {
        cardView.addListener(handColumn);
      }
    }
    for (HandColumnView handColumn : handView) {
      userInventoryView.getChildren().add(handColumn);
    }
    userInventoryView.setSpacing(10);
    //so that we can figure out if we can afford the card, we need to check in UserInventory class. 
    UserInventory userInventory = new UserInventory();
    for (HandColumnView handColumn : handView) {
    	handColumn.addListener(userInventory);
    }

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
    populateGameBoardTokenPiles(tokenRow, screenSize);

    //adding to the scene graph
    Pane root = new Pane();
    root.getChildren().addAll(decksBox, faceupCardsRow, userInventoryView, nobleCards, tokenRow);
    return new Scene(root, screenSize.width, screenSize.height);
  }

}