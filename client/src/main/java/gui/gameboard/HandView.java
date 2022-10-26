package gui.gameboard;

import java.util.ArrayList;
import java.util.Iterator;

public class HandView implements Iterable<HandColumnView> {
	
	private ArrayList<HandColumnView> handColumns;
	
	public HandView() {
		handColumns = new ArrayList<HandColumnView>();
	}

	@Override
	public Iterator<HandColumnView> iterator() {
		return handColumns.iterator();
	}
	
	public void addHandColumn(HandColumnView handColumn) {
		handColumns.add(handColumn);
	}

}
