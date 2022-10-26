package model.Cards;

import gui.gameboard.CardView;

public interface CardObservable {
	
	public void addListener(CardView cardView);
	
	public void removeListener(CardView cardView);

}
