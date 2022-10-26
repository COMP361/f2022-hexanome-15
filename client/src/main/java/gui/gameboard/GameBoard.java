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
import javafx.stage.Stage;
import model.Cards.Deck;

public class GameBoard extends Application {
	
	private StackPane getDeckPane(Deck deck, Dimension screenSize) {
		StackPane deckPane = new StackPane();
		DeckView deckView = new DeckView(deck, screenSize.height/10f, screenSize.width/10f);
		Label cardCount = deckView.getNumCardsDisplay();
		deckPane.getChildren().addAll(deckView, cardCount);
		return deckPane;
	}
	
	private CardView createCardView(Dimension screenSize) {
		return new CardView(screenSize.height/10f, screenSize.width/10f);
	}
	
	private void populateCardColumn(VBox column, Dimension screenSize, List<Deck> decks) {
		//pretty sloppy but will do for now
		for (int i = 0; i < 3; ++i) {
			CardView view = createCardView(screenSize);
			decks.get(i).addListener(view);
			column.getChildren().add(view);
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
		Deck redDeck = new Deck(Color.BLUE);
		Deck yellowDeck = new Deck(Color.YELLOW);
		Deck greenDeck = new Deck(Color.GREEN);
		List<Deck> decks = Arrays.asList(new Deck[] {redDeck, yellowDeck, greenDeck} );
		decksBox.setLayoutX(screenSize.width/6f);
		decksBox.setLayoutY(screenSize.height/6f);
		decksBox.getChildren().addAll(getDeckPane(redDeck, screenSize), getDeckPane(yellowDeck, screenSize), getDeckPane(greenDeck, screenSize));
		
		//building the layout of the faceup cards
		HBox faceupCardsRow = new HBox();
		faceupCardsRow.setSpacing(3);
		faceupCardsRow.setLayoutX(screenSize.width/6f + screenSize.height/10f + 10);
		faceupCardsRow.setLayoutY(screenSize.height/6f);
		VBox faceupCardsFirstColumn = new VBox();
		VBox faceupCardsSecondColumn = new VBox();
		VBox faceupCardsThirdColumn = new VBox();
		List<VBox> columns = Arrays.asList(new VBox[] {faceupCardsFirstColumn, faceupCardsSecondColumn, faceupCardsThirdColumn});
		for (VBox column : columns) {
			column.setSpacing(3);
			populateCardColumn(column, screenSize, decks);
		}
		faceupCardsRow.getChildren().addAll(columns);
		for (Deck deck : decks) {
			deck.deal();
		}
		
		root.getChildren().addAll(decksBox, faceupCardsRow);
		
		stage.setScene(new Scene(root, screenSize.width, screenSize.height));
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
