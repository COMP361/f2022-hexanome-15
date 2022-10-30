package model.Cards;

import gui.gameboard.CardView;

public interface CardObservable {
	
	void addListener(CardObserver cardView);
	
	void removeListener(CardObserver cardView);

}
