package model.Cards;

import gui.gameboard.CardView;

public interface CardObservable {
	
	public void addListener(CardObserver cardView);
	
	public void removeListener(CardObserver cardView);

}
