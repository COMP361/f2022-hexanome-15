package gui.gameboard;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.ColorManager;
import model.Cards.CardType;
import model.Cards.Deck;
import model.Tokens.TokenDeck;
import model.Tokens.TokenType;

public class GameBoard extends Application {

	private StackPane getDeckPane(DeckView deckView, Dimension screenSize) {
		StackPane deckPane = new StackPane();
		Label cardCount = deckView.getNumCardsDisplay();
		deckPane.getChildren().addAll(deckView, cardCount);
		return deckPane;
	}

	private DeckView createDeckView(Deck deck, Dimension screenSize) {
		return new DeckView(deck, screenSize.height/15f, screenSize.width/15f);
	}
	
	private CardView createCardView(Dimension screenSize) {
		return new CardView(screenSize.height/15f, screenSize.width/15f);
	}
	
	private void populateCardColumn(VBox column, Dimension screenSize, List<Deck> decks, ArrayList<DeckView> deckViews,
									ArrayList<CardView> aggregator) {
		//pretty sloppy but will do for now
		for (int i = 0; i < 3; ++i) {
			CardView view = createCardView(screenSize);
			decks.get(i).addListener(view);
			column.getChildren().add(view);
			aggregator.add(view);
			view.addListener(deckViews.get(i));
		}
	}
	
	private void populateTokenDisplay(VBox tokenColumn, Dimension screenSize) {
		for (int i = 0; i < TokenType.values().length; ++i) {
			HBox tokenRow = new HBox();
			TokenDeck deck = new TokenDeck(TokenType.values()[i]);
			TokenDeckView deckView = new TokenDeckView((float)screenSize.height/55f, deck);
			Rectangle miniCard = new Rectangle(screenSize.height/45f, screenSize.width/50f);
			miniCard.setFill(ColorManager.getColor(deck.getType()));//.getColor());
			Counter cardCounter = new Counter(0);
			tokenRow.getChildren().addAll(deckView, deckView.getCounter(), miniCard, cardCounter);
			tokenColumn.getChildren().add(tokenRow);
		}
	}
	private void populateTokenPiles(HBox tokenRow, Dimension screenSize) {
		for (int i = 0; i < TokenType.values().length; ++i) {
			VBox tokenColumn = new VBox();
			TokenDeck deck = new TokenDeck(TokenType.values()[i]);
			TokenDeckView deckView = new TokenDeckView((float)screenSize.height/55f, deck);
			tokenColumn.getChildren().addAll(deckView, deckView.getCounter());
			tokenRow.getChildren().add(tokenColumn);
		}
	}

	private void populateHandView(HandView handView, Dimension screenSize) {
		for (int i = 0; i < TokenType.values().length-1; ++i) {
			handView.addHandColumn(new HandColumnView(TokenType.values()[i], screenSize));
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		//set up the assets of the gameboard
		stage.setTitle("H15-Splendor");
		Pane root = new Pane();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//building the decks of cards
		VBox decksBox = new VBox();
		decksBox.setSpacing(3);
		Deck redDeck = new Deck(CardType.BASE3);
		Deck yellowDeck = new Deck(CardType.BASE2);
		Deck greenDeck = new Deck(CardType.BASE1);
		List<Deck> decks = Arrays.asList(new Deck[] {redDeck, yellowDeck, greenDeck} );
		decksBox.setLayoutX(screenSize.width/6f);
		decksBox.setLayoutY(screenSize.height/20f);
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
		faceupCardsRow.setLayoutX(screenSize.width/6f + screenSize.width/10f + 10);
		faceupCardsRow.setLayoutY(screenSize.height/20f);
		VBox faceupCardsFirstColumn = new VBox();
		VBox faceupCardsSecondColumn = new VBox();
		VBox faceupCardsThirdColumn = new VBox();
		VBox faceupCardsFourthColumn = new VBox();
		ArrayList<CardView> cardViewAggregator = new ArrayList<CardView>();
		List<VBox> columns = Arrays.asList(new VBox[] {faceupCardsFirstColumn, faceupCardsSecondColumn, faceupCardsThirdColumn, faceupCardsFourthColumn});

		for (VBox column : columns) {
			column.setSpacing(3);
			populateCardColumn(column, screenSize, decks, deckViews, cardViewAggregator);
		}
		faceupCardsRow.getChildren().addAll(columns);
		for (DeckView deckView : deckViews) {
			deckView.setUp();
		}

		decksBox.getChildren().addAll(getDeckPane(redDeckView, screenSize), getDeckPane(yellowDeckView, screenSize), getDeckPane(greenDeckView, screenSize));

		//ignoring the pretty token display for now
		
		//building the user inventory
		float yOffset = 6 * screenSize.height/10f;
		float xOffset = screenSize.width/6f;
		HBox userInventory = new HBox();
		userInventory.setLayoutY(yOffset);
		userInventory.setLayoutX(xOffset);
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
		for (CardView cardView : cardViewAggregator) {
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
		nobleCards.setLayoutY(screenSize.height/20f);
		nobleCards.setLayoutX(screenSize.width/12f);
		for (int i=0; i<5; i++) {
			Rectangle rectangle = new Rectangle(screenSize.height/15f, screenSize.width/15f);
			nobleCards.getChildren().add(rectangle);
		}
		nobleCards.setSpacing(3);

		//Creating token piles
		HBox tokenRow = new HBox();
		tokenRow.setSpacing(50);
		tokenRow.setLayoutY(5.25*screenSize.height/10f);
		tokenRow.setLayoutX(xOffset);
		populateTokenPiles(tokenRow, screenSize);

		//adding to the scene graph
		root.getChildren().addAll(decksBox, faceupCardsRow, userInventory, nobleCards, tokenRow);
		
		stage.setScene(new Scene(root, screenSize.width, screenSize.height));
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
