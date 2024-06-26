package ca.mcgill.splendorclient.view.gameboard;

import ca.mcgill.splendorclient.control.ColorManager;
import ca.mcgill.splendorclient.control.GameController;
import ca.mcgill.splendorclient.control.LobbyServiceExecutor;
import ca.mcgill.splendorclient.control.SceneManager;
import ca.mcgill.splendorclient.control.Splendor;
import ca.mcgill.splendorclient.model.DeckType;
import ca.mcgill.splendorclient.model.TokenType;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

/**
 * Represents the view of the Splendor game board.
 */
public class GameBoardView {

  private static final ArrayList<CardView> cardViews = new ArrayList<CardView>();
  private static final ArrayList<DeckView> deckViews = new ArrayList<>();
  private static final ArrayList<NobleView> nobleViews = new ArrayList<>();
  private static final Map<String, UserInventoryView> userViews = new HashMap<>();
  private static final ArrayList<CityView> cityViews = new ArrayList<>();
  private static TradingView tradingView;
  private List<TokenPileView> tokenPileViews;
  private static GameBoardView instance = new GameBoardView();
  private static List<Label> labels = new ArrayList<Label>();
  //private static final float baseUnit_X = screenSize.height / 15f;
  private static final String rootPath = new File("").getAbsolutePath();
  private static float universalUnitX;
  private static float universalUnitY;
  private static float cardWidth;
  private static float cardHeight;
  private static final float spacer = 5f;
  private static int fontSize;
  //cards displayed in inventories are of size card * fraction
  private static final float inventoryCardSizeFraction = 0.6f;
  
  /**
   * Returns the horizontal universal unit, 1% of screen resolution.
   *
   * @return the horizontal universal unit, 1% of screen resolution
   */
  public static float getUniversalUnitX() {
    return universalUnitX;
  }

  /**
   * Returns the vertical universal unit, 1% of screen resolution.
   *
   * @return the vertical universal unit, 1% of screen resolution
   */
  public static float getUniversalUnitY() {
    return universalUnitY;
  }

  /**
   * Returns the currently used resolution-dependent card width.
   *
   * @return the currently used resolution-dependent card width
   */
  public static float getCardWidth() {
    return cardWidth;
  }

  /**
   * Returns the currently used resolution-dependent card height.
   *
   * @return the currently used resolution-dependent card height
   */
  public static float getCardHeight() {
    return cardHeight;
  }

  /**
   * Returns the minimum space between UI elements such as cards, in px.
   *
   * @return the minimum space between UI elements such as cards, in px
   */
  public static float getSpacer() {
    return spacer;
  }

  /**
   * Returns the fontSize.
   *
   * @return the fontSize
   */
  public static int getFontSize() {
    return fontSize;
  }

  /**
   * Creates a GameBoardView.
   */
  private GameBoardView() {
    tokenPileViews = new ArrayList<>();
  }

  /**
   * Returns this instance of GameBoardView.
   *
   * @return this instance of GameBoardView
   */
  public static GameBoardView getInstance() {
    return instance;
  }

  /**
   * Returns the token pile views in the game board view.
   *
   * @return the token pile views in the game board view
   */
  public List<TokenPileView> getTokenPileViews() {
    return tokenPileViews;
  }

  /**
   * Creates a stack pane for the given DeckView and returns it.
   *
   * @param deckView the DeckView that is being added to the stack pane
   * @return the stack pane for the given DeckView
   */
  private static StackPane getDeckPane(DeckView deckView) {
    StackPane deckPane = new StackPane();
    Text cardCount = deckView.getNumCardsDisplay();
    deckPane.getChildren().addAll(deckView, cardCount);
    return deckPane;
  }

  private static DeckView createDeckView(DeckType type, float x, float y) {
    DeckView newView = new DeckView(x, y,
        0, type);
    return newView;
  }

  private static CardView createCardView(float x, float y, String location) {
    CardView newView = new CardView(x, y, location);
    cardViews.add(newView);
    return newView;
  }

  private static NobleView createNobleView(float x, float y) {
    NobleView newView = new NobleView(x, y);
    nobleViews.add(newView);
    return newView;
  }

  private static void createCardFieldColumn(VBox column, float x, float y,
                                            List<CardView> aggregator, int columnCount) {
    int numRows = 3;
    for (int i = 0; i < numRows; ++i) {
      //flatten the 2d playing field for cardview location data.
      int location = columnCount * numRows + i;
      CardView cardView = createCardView(x, y, "C" + location);
      column.getChildren().add(cardView);
      aggregator.add(cardView);
    }
  }

  private static void populateUserInventoryDisplay(VBox tokenColumn, Dimension screenSize,
                                                   final UserInventoryView userInventoryView) {
    int i = 0;
    for (CardColumnView cardColumn : userInventoryView) {
      TokenPileView pileView =
          new TokenPileView((float) screenSize.height / 55f, TokenType.values()[i]);
      Rectangle miniCard = new Rectangle(screenSize.height / 45f, screenSize.width / 50f);
      miniCard.setFill(ColorManager.getColor(TokenType.values()[i]));
      HBox tokenRow = new HBox();
      Counter cardCounter = new Counter(0);
      tokenRow.getChildren().addAll(pileView, pileView.getCounter(), miniCard, cardCounter);
      userInventoryView.addTokenCounter(pileView.getCounter());
      userInventoryView.addCardCounter(cardCounter);
      tokenColumn.getChildren().add(tokenRow);
      ++i;
    }
    //  Token Display for gold tokens
    final HBox tokenRow = new HBox();
    TokenPileView pileView =
        new TokenPileView((float) screenSize.height / 55f, TokenType.values()[i]);
    Rectangle miniCard = new Rectangle(screenSize.height / 45f, screenSize.width / 50f);
    miniCard.setFill(ColorManager.getColor(TokenType.values()[i]));
    Counter cardCounter = new Counter(0);
    tokenRow.getChildren().addAll(pileView, pileView.getCounter(), miniCard, cardCounter);
    userInventoryView.addTokenCounter(pileView.getCounter());
    userInventoryView.addCardCounter(cardCounter);
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
      TokenPileView pileView =
          new TokenPileView((float) screenSize.height / 55f, TokenType.values()[i]);
      tokenRow.getChildren().add(pileView);
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
      CardColumnView newView = new CardColumnView(TokenType.values()[i], screenSize);
      //newView.setBackground(new Background(
      //    new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
      //newView.getChildren().add(new HBox(4));
      //newView.getChildren().add(new CardView(screenSize.height / 15f, screenSize.width / 15f));
      inventoryView.addCardColumn(newView);
    }
  }


  private static HBox buildUserInventoryView(int numPlayer,
                                             Dimension screenSize,
                                             float x,
                                             float y,
                                             String playerName) {
    //building the user inventory
    /*float yoffset = 6 * screenSize.height / 10f;
    float xoffset = screenSize.width * (numPlayer + 1) / 6f;
    if (numPlayer > 0) {
      xoffset = screenSize.width * (numPlayer + 2) / 6f;
    }*/
    
    float xoffset = 0; //= (float) (1 * universalUnitX + cardWidth * 1.2 + spacer);
    float yoffset = 0; //= 6 * screenSize.height / 10f;
    
    if (numPlayer == 0 || numPlayer == 1 || numPlayer == 2) {
      yoffset = 5.5f * screenSize.height / 10f;
      xoffset = (float) (1 * universalUnitX + cardWidth * 1.2 + spacer + x * numPlayer);
    }
    
    if (numPlayer == 3) {
      yoffset = universalUnitY;
      xoffset = (float) (1 * universalUnitX + (9.2) * cardWidth + 9 * spacer + 2);
    }
    
    HBox userInventoryView = new HBox();
    userInventoryView.setSpacing(spacer);
    userInventoryView.setLayoutY(yoffset);
    userInventoryView.setLayoutX(xoffset);
    VBox tokenColumn = new VBox();
    tokenColumn.setSpacing(spacer);
    userInventoryView.getChildren().add(tokenColumn);
    Label username = new Label(playerName);
    username.setTextFill(Color.BLACK);
    username.setFont(Font.font("Comic Sans MS", FontWeight.BOLD,
        FontPosture.REGULAR, fontSize / 1.5));
    labels.add(username);
    TotalTokenCountView tokenCountView = new TotalTokenCountView("Tokens: 0");
    TotalCardCountView cardCountView = new TotalCardCountView("Cards: 0");
    TotalPrestigeCountView prestigeCountView =
        new TotalPrestigeCountView("Prestige: 0");
    tokenColumn.getChildren().addAll(username, tokenCountView, cardCountView, prestigeCountView);
    UserInventoryView inventoryView = new UserInventoryView(playerName,
        cardWidth * inventoryCardSizeFraction,
        cardHeight * inventoryCardSizeFraction,
        tokenCountView,
        cardCountView,
        prestigeCountView);
    userViews.put(playerName, inventoryView);
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
   * @param players the players in the game
   * @param gameServer the game server of the game
   * @return the gameboard scene
   */
  public static Scene setupGameBoard(JSONArray players, String gameServer) {
    //since users can go in and out of games, we need to reset the static containers
    cardViews.clear();
    deckViews.clear();
    nobleViews.clear();
    userViews.clear();
    cityViews.clear();
    instance.tokenPileViews.clear();
    labels.clear();
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    universalUnitX = screenSize.width / 100f;
    universalUnitY = screenSize.height / 100f;
    cardHeight = universalUnitY * 16;
    cardWidth = universalUnitX * 6;
    //spacer is final and defined above
    fontSize = (int) (universalUnitX + universalUnitY);

    //building the decks of cards
    VBox decksBox = new VBox();
    decksBox.setSpacing(spacer);
    decksBox.setLayoutX(1 * universalUnitX + cardWidth * 1.2 + spacer);
    decksBox.setLayoutY(universalUnitY);

    //building the decks of orient cards
    VBox orientDecksBox = new VBox();
    orientDecksBox.setSpacing(spacer);
    orientDecksBox.setLayoutX(1 * universalUnitX + (8.2) * cardWidth + 8 * spacer + 2);
    orientDecksBox.setLayoutY(universalUnitY);


    //building the layout of the faceup cards
    HBox faceupCardsRow = new HBox();
    faceupCardsRow.setSpacing(spacer);
    faceupCardsRow.setLayoutX(1 * universalUnitX + cardWidth * 1.2 + spacer + cardWidth + spacer);
    faceupCardsRow.setLayoutY(universalUnitY);
    VBox faceupCardsFirstColumn = new VBox();
    VBox faceupCardsSecondColumn = new VBox();
    VBox faceupCardsThirdColumn = new VBox();
    VBox faceupCardsFourthColumn = new VBox();
    VBox faceupCardsFifthColumn = new VBox();
    VBox faceupCardsSixthColumn = new VBox();
    List<CardView> cardViewAggregator = new ArrayList<>();
    List<VBox> columns = Arrays.asList(faceupCardsFirstColumn, faceupCardsSecondColumn,
        faceupCardsThirdColumn, faceupCardsFourthColumn,
        faceupCardsFifthColumn, faceupCardsSixthColumn);

    int columnCount = 0;
    for (VBox column : columns) {
      column.setSpacing(spacer);
      createCardFieldColumn(column, cardWidth, cardHeight, cardViewAggregator, columnCount);
      ++columnCount;
    }
    faceupCardsRow.getChildren().addAll(columns);

    DeckView redDeckView = createDeckView(DeckType.BASE3, cardWidth, cardHeight);
    DeckView yellowDeckView = createDeckView(DeckType.BASE2, cardWidth, cardHeight);
    DeckView greenDeckView = createDeckView(DeckType.BASE1, cardWidth, cardHeight);

    DeckView orient3DeckView = createDeckView(DeckType.ORIENT3, cardWidth, cardHeight);
    DeckView orient2DeckView = createDeckView(DeckType.ORIENT2, cardWidth, cardHeight);
    DeckView orient1DeckView = createDeckView(DeckType.ORIENT1, cardWidth, cardHeight);

    //adding deckviews to list field for modification by GameController
    //these follow order b123,o123 as in state json
    deckViews.add(greenDeckView);
    deckViews.add(yellowDeckView);
    deckViews.add(redDeckView);
    deckViews.add(orient1DeckView);
    deckViews.add(orient2DeckView);
    deckViews.add(orient3DeckView);

    decksBox.getChildren().addAll(getDeckPane(redDeckView),
        getDeckPane(yellowDeckView), getDeckPane(greenDeckView));

    orientDecksBox.getChildren().addAll(getDeckPane(orient3DeckView),
        getDeckPane(orient2DeckView), getDeckPane(orient1DeckView));

    final int nPlayers = players.length();

    //building all user inventories
    List<HBox> allUserInventoryViews = new ArrayList<HBox>();
    for (int i = 0; i < nPlayers; ++i) {
      HBox userInventoryView =
          buildUserInventoryView(i, screenSize,
              3 * universalUnitX + 5 * (cardWidth * inventoryCardSizeFraction + spacer),
              screenSize.height / 3f,
              (String) players.get(i));
      allUserInventoryViews.add(userInventoryView);
    }

    
    //creating nobles
    VBox nobleCards = new VBox();
    nobleCards.setLayoutX(universalUnitX);
    nobleCards.setLayoutY(universalUnitY);
    int playerCount = players.length();
    for (int i = 0; i < playerCount + 1; i++) {
      NobleView nobleView = createNobleView(cardWidth * 1.2f, cardWidth * 1.2f);
      nobleCards.getChildren().add(nobleView);
    }
    nobleCards.setSpacing(spacer);


    //Creating token piles
    HBox tokenRow = new HBox();
    tokenRow.setSpacing(50);
    tokenRow.setLayoutY(3 * cardHeight + 3 * spacer + 1 * universalUnitY);
    tokenRow.setLayoutX(1 * universalUnitX + 1 * spacer + 1.2 * cardWidth);
    populateGameBoardTokenPiles(tokenRow, screenSize, nPlayers);

    //adding to the scene graph
    Pane root = new Pane();
    root.getChildren().addAll(decksBox, orientDecksBox, faceupCardsRow, nobleCards, tokenRow);
    root.getChildren().addAll(allUserInventoryViews);

    //creating Trade Routes expansion
    if (gameServer.equals("SplendorOrientTradingPosts")) {
      float tradingY = 37 * universalUnitY;
      float tradingX = 1 * universalUnitX + (9.2f) * cardWidth + 9 * spacer + 2;
      tradingView = new TradingView(screenSize,
          98 * universalUnitX - (9.2f) * cardWidth - 9 * spacer - 2,
          cardHeight);
      tradingView.setLayoutY(tradingY);
      tradingView.setLayoutX(tradingX);
      root.getChildren().addAll(tradingView);
    }
    //creating Cities expansion
    if (gameServer.equals("SplendorOrientCities")) {
      for (int i = 3; i > 0; i--) {
        CityView city = new CityView(1.5f * cardHeight, 0.8f * cardHeight);
        city.setLayoutX(universalUnitX * (99) - 1.5f * cardHeight);
        city.setLayoutY(universalUnitY * (95) - i * (0.8f * cardHeight + spacer));
        cityViews.add(city);
      }
      root.getChildren().addAll(cityViews);
    }

    //save and quit buttons
    HBox menu = new HBox();
    menu.setSpacing(10);
    Button saveButton = new Button("Save Game");
    saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        Long gameid = GameController.getInstance().getGameId();
        //send to server /api/games/{gameId}/savegame
        String location = LobbyServiceExecutor.SERVERLOCATION;
        String url = String.format("http://%s/api/games/%d/savegame", location, gameid);
        HttpResponse<JsonNode> response = 
            Unirest.put(url).asJson();
        System.out.println(response.getStatus());
      }
      
    });
    Button quitButton = new Button("Quit Game");
    quitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        GameController.stop();
        Splendor.transitionTo(SceneManager.getLobbyScreen(), Optional.of("Splendor Lobby"));
        
      }
      
    });
    menu.getChildren().addAll(saveButton, quitButton);
    root.getChildren().add(menu);
    Scene toReturn =  new Scene(root, screenSize.width, screenSize.height, Color.BLACK);
    Image newImage = new Image("file:///" + rootPath + "/resources/background_tile.jpg");
    if (newImage != null) {
      root.setBackground(new Background(
          new BackgroundFill(new ImagePattern(newImage), CornerRadii.EMPTY, Insets.EMPTY)));
    }
    return toReturn;
  }
  
  
  /**
   * Updates the turn labels to red when it's your turn.
   *
   * @param whoseTurn the current turn
   */
  public static void updateTurnLabels(String whoseTurn) {
    for (Label label : labels) {
      if (label.getText().equals(whoseTurn)) {
        label.setTextFill(Color.RED);
      } else {
        label.setTextFill(Color.BLACK);
      }
    }
  }

  /**
   * Updates the card views based on data received from server.
   *
   * @param field the card field
   */
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

  /**
   * Updates the noble views.
   *
   * @param field the game board field
   */
  public static void updateNobleViews(int[] field) {
    for (int i = 0; i < nobleViews.size() && i < field.length; i++) {
      nobleViews.get(i).updateView(field[i]);
    }
  }

  /**
   * Updates the given player's displayed inventory,
   * including their hand, token amounts, prestige,
   * nobles, and powers.
   *
   * @param playerName the name of the player
   * @param cards an int[] array representing cards in player's possession
   * @param reservedcards an int[] array representing reserved cards in player's possession
   * @param numOfDiamonds number of diamond tokens in player's possession
   * @param numOfSapphires number of sapphire tokens in player's possession
   * @param numOfEmeralds number of emerald tokens in player's possession
   * @param numOfRubies number of ruby tokens in player's possession
   * @param numOfOnyx number of onyx tokens in player's possession
   * @param numOfGolds number of gold tokens in player's possession
   * @param numWhiteCards number purchased white cards in player's possession
   * @param numBlueCards number purchased blue cards in player's possession
   * @param numGreenCards number purchased green cards in player's possession
   * @param numRedCards number purchased red cards in player's possession
   * @param numBlackCards number purchased black cards in player's possession
   * @param numGoldCards number purchased gold cards in player's possession
   * @param prestige player's current prestige score
   * @param visitingNobles IDs of nobles currently visiting player
   * @param powers int[] array representing unlocked Trading Posts powers
   * @param reservedNobles IDS of nobles currently reserved
   * @param acquiredCities IDS of cities currently acquired by a player
   */
  public static void updateInventories(String playerName,
                                       int[] cards,
                                       int[] reservedcards,
                                       int numOfDiamonds,
                                       int numOfSapphires,
                                       int numOfEmeralds,
                                       int numOfRubies,
                                       int numOfOnyx,
                                       int numOfGolds,
                                       int numWhiteCards,
                                       int numBlueCards,
                                       int numGreenCards,
                                       int numRedCards,
                                       int numBlackCards,
                                       int numGoldCards,
                                       int prestige,
                                       int[] visitingNobles,
                                       int[] powers,
                                       int[] reservedNobles,
                                       int[] acquiredCities) {
    userViews.get(playerName).updateCards(cards, reservedcards);
    userViews.get(playerName).updateTokens(numOfDiamonds,
        numOfSapphires,
        numOfEmeralds,
        numOfRubies,
        numOfOnyx,
        numOfGolds);
    userViews.get(playerName).updateMiniCards(numWhiteCards,
        numBlueCards,
        numGreenCards,
        numRedCards,
        numBlackCards,
        numGoldCards);
    userViews.get(playerName).updatePrestige(prestige);
    userViews.get(playerName).updateCardCount(cards.length);
    userViews.get(playerName).updateNoblesInventory(reservedNobles, visitingNobles, nobleViews);
    userViews.get(playerName).updateCitiesInventory(acquiredCities, cityViews);
  }

  /**
   * Updates the deck views.
   *
   * @param a the deck field
   */
  public static void updateDecks(int[] a) {
    for (int i = 0; i < a.length; i++) {
      deckViews.get(i).setNumCardsDisplay(a[i]);
    }
  }

  /**
   * Draws the correct coats of arms under the powers
   * as owned by the players.
   *
   * @param firstShields coat of arms for trading post slot 1
   * @param secondShields coat of arms for trading post slot 2
   * @param thirdShields coat of arms for trading post slot 3
   * @param fourthShields coat of arms for trading post slot 4
   * @param fifthShields coat of arms for trading post slot 5
   */
  public static void updatePowers(String[] firstShields,
                                  String[] secondShields,
                                  String[] thirdShields,
                                  String[] fourthShields,
                                  String[] fifthShields) {
    tradingView.updatePowers(firstShields, secondShields,
        thirdShields, fourthShields, fifthShields);
  }

  /**
   * Draws the correct cities as received from server state json.
   *
   * @param cities the array of int IDs representing cities.
   */
  public static void updateCityViews(int[] cities) {
    for (int i = 0; i < cities.length; i++) {
      cityViews.get(i).updateView(cities[i]);
    }
  }
}
